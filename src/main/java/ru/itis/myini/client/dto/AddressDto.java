package ru.itis.myini.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddressDto {

    private final String postcode;
    private final String region;
    private final String city;
    private final String street;
    private final String buildingNumber;

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s %s", postcode, region, city, street, buildingNumber);
    }
}
