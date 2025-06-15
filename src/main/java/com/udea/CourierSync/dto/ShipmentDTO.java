package com.udea.CourierSync.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Data
@Builder
public class ShipmentDTO extends RepresentationModel<ShipmentDTO> {
    private Long id;
    private Double weight;
    private String priority;
    private LocalDateTime creationDate;
    private LocalDateTime estimatedDeliveryDate;
}