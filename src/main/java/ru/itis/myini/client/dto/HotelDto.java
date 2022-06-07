package ru.itis.myini.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelDto {

    private Long id;
    private LocalTime checkIn;
    private LocalTime departure;
    private String name;
    private String email;
    private String phoneNumber;

    private AddressDto address;

    private Long cheapest;

    private Long mostExpensive;

    @Override
    public String toString() {
        return String.format("ID: %s\nName: %s\n" +
                "Email: %s\n" +
                "Phone: %s\n" +
                "Check in: %s\n" +
                "Departure: %s\n" +
                "%s\n\n",
                id, name, email, phoneNumber, checkIn, departure, address.payloadString());
    }
}
