package com.catalog.cars.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarRequest {
    @NotNull
    @Size(min = 1, max = 255)
    private String make;

    @NotNull
    @Size(min = 1, max = 255)
    private String model;

    @Min(1886)
    @Max(2023)
    private int year;

    @Positive
    private double price;

    @Size(min = 17, max = 17)
    private String vin;
}
