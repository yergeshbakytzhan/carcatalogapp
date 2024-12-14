package com.catalog.cars.dto;


import com.catalog.cars.model.car.Car;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarResponse {
    private UUID id;
    private String make;
    private String model;
    private int year;
    private double price;
    private String vin;


    public static CarResponse fromModelToDto(Car car){
        return new CarResponse(
                car.getId(),
                car.getMake(),
                car.getModel(),
                car.getYear(),
                car.getPrice(),
                car.getVin()
        );
    }
}
