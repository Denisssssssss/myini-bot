package ru.itis.myini.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApartmentDto {

    private Long id;
    private Long number;
    private Boolean isLocked;
    private Long roomsNumber;
    private Long bedroomsNumber;

    private Long price;

    @Override
    public String toString() {
        String template = "ID: %s\nNumber: %s\nAvailable: %s\nTotal rooms: %s\nTotal bedrooms: %s\nPrice: %s\n";
        return String.format(template, id, number, isLocked, roomsNumber, bedroomsNumber, price);
    }
}
