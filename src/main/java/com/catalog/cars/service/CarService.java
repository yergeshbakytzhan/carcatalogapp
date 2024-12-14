package com.catalog.cars.service;

import com.catalog.cars.dto.CarFilterDto;
import com.catalog.cars.dto.CarRequest;
import com.catalog.cars.dto.CarResponse;

import java.util.List;
import java.util.UUID;

public interface CarService {
    List<CarResponse> search(CarFilterDto carFilterDto);
    List<CarResponse> getAll();
    CarResponse getById(UUID id);
    CarResponse save(CarRequest carRequest);
    CarResponse update(UUID id, CarRequest carRequest);
    void deleteById(UUID id);
}
