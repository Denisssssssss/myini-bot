package ru.itis.myini.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class HotelDto {

    private final LocalTime checkIn;
    private final LocalTime departure;
    private final String name;
    private final String email;
    private final String phoneNumber;

    private final AddressDto address;

    @Override
    public String toString() {
        return String.format("Name: %s\n" +
                "Email: %s\n" +
                "Phone: %s\n" +
                "Check in: %s\n" +
                "Departure: %s\n" +
                "Address: %s\n\n",
                name, email, phoneNumber, checkIn, departure, address.toString());
    }
}
