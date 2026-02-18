package com.cpd.hotel_system.hotel_management_service_api.util;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StandardResponseDto {
    private int statusCode;
    private String message;

    private Object data;
}
