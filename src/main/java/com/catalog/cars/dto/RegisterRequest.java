package com.catalog.cars.dto;

import com.catalog.cars.enums.RoleType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String email;
    private String password;
    private RoleType role;
}
