package ru.itis.myini.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {

    private Long id;
    private String postcode;
    private String region;
    private String city;
    private String street;
    private String buildingNumber;

    @Override
    public String toString() {
        return String.format("ID: %s\nPostcode: %s\nRegion: %s\nCity: %s\nStreet: %s\nBuilding number: %s\n", id, postcode, region, city, street, buildingNumber);
    }

    public String payloadString() {
        return String.format("Postcode: %s\nRegion: %s\nCity: %s\nStreet: %s\nBuilding number: %s\n", postcode, region, city, street, buildingNumber);
    }
}
