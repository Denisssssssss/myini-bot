package ru.itis.myini.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {

    private Long apartmentId;
    private LocalDateTime checkIn;
    private LocalDateTime departure;
    private String code;

    @Override
    public String toString() {
        String template = "Apartment id: %s\nCheck in: %s\nDeparture: %s\nCode: %s\n";
        return String.format(template,
                apartmentId,
                checkIn.truncatedTo(ChronoUnit.SECONDS).toString().replace('T', ' '),
                departure.truncatedTo(ChronoUnit.SECONDS).toString().replace('T', ' '),
                code);
    }
}
