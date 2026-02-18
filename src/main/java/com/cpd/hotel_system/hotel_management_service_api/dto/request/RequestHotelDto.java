package com.cpd.hotel_system.hotel_management_service_api.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RequestHotelDto {

    private String description;
    private String hotelName;
    private int starRating;
    private BigDecimal startingForm;

}
