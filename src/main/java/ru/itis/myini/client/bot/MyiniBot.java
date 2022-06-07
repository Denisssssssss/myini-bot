package ru.itis.myini.client.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.itis.myini.client.client.MyiniClient;
import ru.itis.myini.client.dto.AddressDto;
import ru.itis.myini.client.dto.AddressesDto;
import ru.itis.myini.client.dto.ApartmentDto;
import ru.itis.myini.client.dto.BookingDto;
import ru.itis.myini.client.dto.HotelDto;
import ru.itis.myini.client.dto.HotelsDto;
import ru.itis.myini.client.dto.PriceDto;
import ru.itis.myini.client.dto.UserDto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

@Component
@RequiredArgsConstructor
public class MyiniBot extends TelegramLongPollingBot {

    private final MyiniClient myiniClient;

    @Value("${telegram.bot.name}")
    private String name;

    @Value("${telegram.bot.token}")
    private String token;

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message command = update.getMessage();
        Long chatId = command.getChatId();
        String text = command.getText();

        if (text.startsWith("/help")) {
            sendMessage(chatId, help());
        }

        if (text.startsWith("/info")) {
            sendMessage(chatId, info());
        }

        if (text.startsWith("/reg")) {
            signUp(chatId, text.split(" ")[1]);
            sendMessage(chatId, "Successfully registered");
        }

        if (text.startsWith("/login")) {
            signIn(chatId, text.split(" ")[1]);
            sendMessage(chatId, "Successfully logged in");
        }

        if (text.startsWith("/hotels")) {
            List<HotelDto> hotels = getHotels(chatId);
            sendMessage(chatId, listToString(hotels));
        }

        // apartments {hotel_id}
        if (text.startsWith("/apartments")) {
            Long id = Long.valueOf(text.split(" ")[1]);
            sendMessage(chatId, listToString(getApartments(id, chatId)));
        }

        if (text.startsWith("/book")) {
            Long id = Long.valueOf(text.split(" ")[1]);
            sendMessage(chatId, book(id, chatId).toString());
        }

        if (text.startsWith("/cheap")) {
            Long id = Long.valueOf(text.split(" ")[1]);
            sendMessage(chatId, getBestPriceByCriteria(true, id, chatId).toString());
        }

        if (text.startsWith("/expensive")) {
            Long id = Long.valueOf(text.split(" ")[1]);
            sendMessage(chatId, getBestPriceByCriteria(false, id, chatId).toString());
        }

        if (text.startsWith("/gcheap")) {
            Long id = Long.valueOf(text.split(" ")[1]);
            sendMessage(chatId, getGlobalBestByCriteria(true, id, chatId).toString());
        }

        if (text.startsWith("/gexpensive")) {
            Long id = Long.valueOf(text.split(" ")[1]);
            sendMessage(chatId, getGlobalBestByCriteria(false, id, chatId).toString());
        }

        if (text.startsWith("/alladdresses")) {
            sendMessage(chatId, listToString(getAddresses(chatId).getValues()));
        }

        if (text.startsWith("/address")) {
            Long id = Long.valueOf(text.split(" ")[1]);
            sendMessage(chatId, listToString(getHotelsByAddress(id, chatId).getValues()));
        }

        //addhotel name email phone street postcode region city buildingnumber
        if (text.startsWith("/addhotel")) {
            String[] args = text.split(" ");
            LocalTime source = LocalTime.now().truncatedTo(ChronoUnit.HOURS).minusHours(LocalTime.now().getHour());
            LocalTime checkIn = source.plusHours(6);
            LocalTime departure = source.plusHours(22);
            String name = args[1];
            String email = args[2];
            String phone = args[3];
            String street = args[4];
            String postcode = args[5];
            String region = args[6];
            String city = args[7];
            String buildingNumber = args[8];
            HotelDto hotelDto = HotelDto.builder()
                    .checkIn(checkIn)
                    .departure(departure)
                    .name(name)
                    .email(email)
                    .phoneNumber(phone)
                    .cheapest(0L)
                    .mostExpensive(0L)
                    .address(AddressDto.builder()
                            .postcode(postcode)
                            .region(region)
                            .city(city)
                            .street(street)
                            .buildingNumber(buildingNumber)
                            .build())
                    .build();
            addHotel(chatId, hotelDto);
        }


        if (text.startsWith("/addapartment")) {
            Long id = Long.valueOf(text.split(" ")[1]);
            Random random = new Random();
            long rooms = random.nextLong() % 4 + 2;
            ApartmentDto apartmentDto = ApartmentDto.builder()
                    .number(abs(random.nextLong() % 1000))
                    .price(abs(random.nextLong() % 10000))
                    .roomsNumber(rooms)
                    .bedroomsNumber(rooms / 2)
                    .isLocked(false)
                    .build();
            addApartment(apartmentDto, id, chatId);
        }
    }

    private void signUp(Long chatId, String password) {
        myiniClient.signUp(UserDto.builder()
                .username(String.valueOf(chatId))
                .password(password)
                .build());
    }

    private void signIn(Long chatId, String password) {
        myiniClient.signIn(UserDto.builder()
                .username(String.valueOf(chatId))
                .password(password)
                .build());
    }

    private List<HotelDto> getHotels(Long chatId) {
        return myiniClient.getHotels(String.valueOf(chatId));
    }

    private List<ApartmentDto> getApartments(Long hotelId, Long chatId) {
        return myiniClient.getApartments(String.valueOf(chatId), hotelId).getValues();
    }

    private BookingDto book(Long apartmentId, Long chatId) {
        return myiniClient.book(String.valueOf(chatId),
                BookingDto.builder()
                        .apartmentId(apartmentId)
                        .checkIn(LocalDateTime.now())
                        .departure(LocalDateTime.now().plusDays(10))
                        .build());
    }

    private PriceDto getBestPriceByCriteria(boolean cheapest, Long hotelId, Long chatId) {
        if (cheapest) {
            return myiniClient.getCheap(String.valueOf(chatId), hotelId);
        } else return myiniClient.getExpensive(String.valueOf(chatId), hotelId);
    }

    private PriceDto getGlobalBestByCriteria(boolean cheapest, Long hotelId, Long chatId) {
        if (cheapest) {
            return myiniClient.getGlobalCheap(String.valueOf(chatId), hotelId);
        } else return myiniClient.getGlobalExpensive(String.valueOf(chatId), hotelId);
    }

    private HotelsDto getHotelsByAddress(Long addressId, Long chatId) {
        return myiniClient.getHotelsByAddress(String.valueOf(chatId), addressId);
    }

    private AddressesDto getAddresses(Long chatId) {
        return myiniClient.getAddresses(String.valueOf(chatId));
    }

    private void addHotel(Long chatId, HotelDto hotelDto) {
        myiniClient.addHotel(String.valueOf(chatId), hotelDto);
    }

    private void addApartment(ApartmentDto apartmentDto, Long hotelId, Long chatId) {
        myiniClient.addApartment(String.valueOf(chatId), apartmentDto, hotelId);
    }

    private String help() {
        String help =
                "/info - information\n" +
                        "/reg [passwd] - registration\n" +
                        "/login [passwd] - sign in\n" +
                        "/hotels - list of hotels\n" +
                        "/apartments [hotel id] - all apartments in hotel with id = hotel id\n" +
                        "/book [apartment id] - book apartment with id = apartment id\n" +
                        "/cheap [hotel id] - cheapest price in hotel with id = hotel id\n" +
                        "/expensive [hotel id] - most expensive price in hotel with id = hotel id\n" +
                        "/gcheap - cheapest price in all hotels\n" +
                        "/gexpensive - most expensive price in all hotels\n" +
                        "/alladdresses - list of hotel addresses\n" +
                        "/address [address id] - list of hotels by address with id = address id\n" +
                        "/addhotel [name] [email] [phone] [street] [postcode] [region] [city] [buildingnumber] - add hotel with passed parameters\n" +
                        "/addapartment [hotel id] - add apartment to hotel with id = hotel id\n";
        return help;
    }

    private String info() {
        return "- Бот создан только для демо основных фичей, поэтому некоторый функционал недоступен\n" +
                "- В командах в квадратных скобках указаны параметры\n" +
                "- Авторизация сохраняется в рамках текущего чата в течение 7 дней, после чего надо будет снова авторизоваться\n" +
                "- При добавлении номера в отель, данные номера заполняются рандомно (сделано для удобства)";
    }

    private void sendMessage(Long chatId, String text) {
        try {
            execute(new SendMessage(String.valueOf(chatId), text));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String listToString(List<?> list) {
        return list.stream().map(o -> o.toString().concat("==============\n"))
                .collect(Collectors.joining());
    }
}
