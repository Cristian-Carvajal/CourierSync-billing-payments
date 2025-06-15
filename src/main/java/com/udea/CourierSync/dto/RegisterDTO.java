package com.udea.CourierSync.dto;

import com.udea.CourierSync.entity.UserRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterDTO {
    private String name;
    private String lastName;
    private String email;
    private String password;
    private UserRole role;

}
