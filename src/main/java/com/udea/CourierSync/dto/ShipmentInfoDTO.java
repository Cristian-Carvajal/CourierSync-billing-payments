package com.udea.CourierSync.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShipmentInfoDTO {
    private Long id;
    private Double weight;
}