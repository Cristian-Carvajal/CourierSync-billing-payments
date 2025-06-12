package com.udea.CourierSync.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
public class RouteDTO extends RepresentationModel<RouteDTO> {
    private Long id;
    private String origin;
    private String destination;
    private Double distance;
    private Integer estimatedTime;
    private String trafficLevel;
}