package com.catalog.cars.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarFilterDto {
    private String make;
    private String model;
    private int year;
}
