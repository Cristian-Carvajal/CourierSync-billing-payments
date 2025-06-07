package com.udea.CourierSync.dto;

public class AuthResponseDTO {
    private String token;

    public AuthResponseDTO() {} // Constructor vacío necesario para deserialización

    public AuthResponseDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
