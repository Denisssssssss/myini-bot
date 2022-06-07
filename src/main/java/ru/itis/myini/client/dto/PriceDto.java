package ru.itis.myini.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceDto {

    private Long price;

    @Override
    public String toString() {
        String template = "Price: %s\n";
        return String.format(template, price);
    }
}
