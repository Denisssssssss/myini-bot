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
import ru.itis.myini.client.dto.HotelDto;
import ru.itis.myini.client.dto.UserDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        if (text.startsWith("/reg")) {
            signUp(chatId, text.split(" ")[1]);
        }

        if (text.startsWith("/login")) {
            signIn(chatId, text.split(" ")[1]);
        }

        if (text.startsWith("/hotels")) {
            List<HotelDto> hotels = getHotels(chatId);
            sendMessage(chatId, listToString(hotels));
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

    private void sendMessage(Long chatId, String text) {
        try {
            execute(new SendMessage(String.valueOf(chatId), text));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String listToString(List<?> list) {
        return list.stream().map(o -> o.toString().concat("=============="))
                .collect(Collectors.joining());
    }
}
