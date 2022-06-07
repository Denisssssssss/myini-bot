package ru.itis.myini.client.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.itis.myini.client.dto.AddressesDto;
import ru.itis.myini.client.dto.ApartmentDto;
import ru.itis.myini.client.dto.ApartmentsDto;
import ru.itis.myini.client.dto.BookingDto;
import ru.itis.myini.client.dto.HotelDto;
import ru.itis.myini.client.dto.HotelsDto;
import ru.itis.myini.client.dto.PriceDto;
import ru.itis.myini.client.dto.UserDto;

import java.util.List;

@FeignClient(name = "myini-server", url = "localhost:8080")
public interface MyiniClient {

    @PostMapping("/signUp")
    void signUp(@RequestBody UserDto userDto);

    @PostMapping("/signIn")
    void signIn(@RequestBody UserDto userDto);

    @GetMapping("/hotels")
    List<HotelDto> getHotels(@RequestHeader("chatId") String chatId);

    @GetMapping("/apartments/{hotelId}")
    ApartmentsDto getApartments(@RequestHeader("chatId")String chatId, @PathVariable Long hotelId);

    @GetMapping("/hotels/{hotelId}/cheap")
    PriceDto getCheap(@RequestHeader("chatId") String chatId, @PathVariable Long hotelId);

    @GetMapping("/hotels/{hotelId}/expensive")
    PriceDto getExpensive(@RequestHeader("chatId") String chatId, @PathVariable Long hotelId);

    @GetMapping("/hotels/{hotelId}/cheap/global")
    PriceDto getGlobalCheap(@RequestHeader("chatId") String chatId, @PathVariable Long hotelId);
    @GetMapping("/hotels/{hotelId}/expensive/global")
    PriceDto getGlobalExpensive(@RequestHeader("chatId") String chatId, @PathVariable Long hotelId);

    @GetMapping("/addresses")
    AddressesDto getAddresses(@RequestHeader("chatId") String chatId);
    @GetMapping("/hotels/addresses/{addressId}")
    HotelsDto getHotelsByAddress(@RequestHeader("chatId") String chatId, @PathVariable Long addressId);
    @PostMapping("/bookings")
    BookingDto book(@RequestHeader("chatId") String chatId, @RequestBody BookingDto bookingDto);

    @PostMapping("/hotels")
    void addHotel(@RequestHeader("chatId") String chatId, @RequestBody HotelDto hotelDto);

    @PostMapping("/apartments/{hotel-id}")
    void addApartment(@RequestHeader("chatId") String chatId, @RequestBody ApartmentDto apartmentDto, @PathVariable("hotel-id") Long hotelId);
}
