package com.catalog.cars.service.impl;

import com.catalog.cars.dto.CarFilterDto;
import com.catalog.cars.dto.CarRequest;
import com.catalog.cars.dto.CarResponse;
import com.catalog.cars.exception.NotFoundException;
import com.catalog.cars.model.car.Car;
import com.catalog.cars.repository.CarRepository;
import com.catalog.cars.service.CarService;
import com.catalog.cars.specification.CarSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultCarService implements CarService {

    private final CarRepository carRepository;

    @Override
    public List<CarResponse> search(CarFilterDto carFilterDto) {
        var spec = buildSpecification(carFilterDto);
        return carRepository.findAll(spec).stream().map(CarResponse::fromModelToDto).toList();
    }

    @Override
    public List<CarResponse> getAll(){
        return carRepository.findAll().stream().map(CarResponse::fromModelToDto).toList();
    }

    @Override
    public CarResponse getById(UUID id) {
        var car = carRepository.findById(id).orElseThrow(()-> new NotFoundException("Car not found with id: "+id));

        return CarResponse.fromModelToDto(car);
    }

    @Override
    public CarResponse save(CarRequest carRequest) {
        try{
            var car = new Car();
            car.setMake(carRequest.getMake());
            car.setModel(carRequest.getModel());
            car.setYear(carRequest.getYear());
            car.setPrice(carRequest.getPrice());
            car.setVin(carRequest.getVin());
            var newCar = carRepository.save(car);
            log.info("Car created with id: " + newCar.getId());
            return CarResponse.fromModelToDto(car);
        }catch (Exception e){
            log.error("Couldn't create car: " + e);
            throw new RuntimeException("Couldn't create car" + e);
        }
    }

    @Override
    public CarResponse update(UUID id, CarRequest carRequest) {
        try{
            var car = carRepository.findById(id).orElseThrow(()-> new NotFoundException("Car not found with id: "+id));
            car.setMake(carRequest.getMake());
            car.setModel(carRequest.getModel());
            car.setYear(carRequest.getYear());
            car.setPrice(carRequest.getPrice());
            car.setVin(carRequest.getVin());
            carRepository.save(car);
            log.info("Car updated with id: "+id);
            return CarResponse.fromModelToDto(car);
        }catch (Exception e){
            log.error("Couldn't update car: " + e);
            throw new RuntimeException("Couldn't update car" + e);
        }
    }

    @Override
    public void deleteById(UUID id) {
        log.info("Car deleted with id: "+id);
        carRepository.deleteById(id);
    }

    private Specification<Car> buildSpecification(CarFilterDto carFilterDto){
        return Specification.where(CarSpecification.hasMake(carFilterDto.getMake()))
                .and(CarSpecification.hasModel(carFilterDto.getModel()))
                .and(CarSpecification.hasYear(carFilterDto.getYear()));
    }
}
