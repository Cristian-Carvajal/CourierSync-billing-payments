package com.udea.CourierSync.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientInfoDTO {
    private Long id;
    private String name;
    private String email;
}