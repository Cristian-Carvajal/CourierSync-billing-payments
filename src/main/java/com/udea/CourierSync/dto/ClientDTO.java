package com.udea.CourierSync.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
public class ClientDTO extends RepresentationModel<ClientDTO> {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String shipmentPreferences;
}