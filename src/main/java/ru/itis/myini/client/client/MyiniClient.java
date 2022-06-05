package ru.itis.myini.client.client;

import feign.HeaderMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.itis.myini.client.dto.HotelDto;
import ru.itis.myini.client.dto.UserDto;

import java.util.List;
import java.util.Map;

@FeignClient(name = "myini-server", url = "localhost:8080")
public interface MyiniClient {

    @PostMapping("/signUp")
    void signUp(@RequestBody UserDto userDto);

    @PostMapping("/signIn")
    void signIn(@RequestBody UserDto userDto);

    @GetMapping("/hotels")
    List<HotelDto> getHotels(@RequestHeader("chatId") String chatId);

}
