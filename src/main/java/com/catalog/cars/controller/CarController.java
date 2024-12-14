package com.catalog.cars.controller;

import com.catalog.cars.dto.CarFilterDto;
import com.catalog.cars.dto.CarRequest;
import com.catalog.cars.dto.CarResponse;
import com.catalog.cars.service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cars")
@Validated
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;


    @GetMapping("/search")
    public ResponseEntity<List<CarResponse>> search(
            @ModelAttribute CarFilterDto carFilterDto
            ){
        return ResponseEntity.ok(carService.search(carFilterDto));
    }

    @GetMapping
    public ResponseEntity<List<CarResponse>> getAll(){
        return ResponseEntity.ok(carService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> getById(@PathVariable UUID id){
        return ResponseEntity.ok(carService.getById(id));
    }

    @PostMapping
    public ResponseEntity<CarResponse> save(@Valid @RequestBody CarRequest carRequest){
        return ResponseEntity.ok(carService.save(carRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarResponse> update(@PathVariable UUID id, @Valid @RequestBody CarRequest carRequest){
        return ResponseEntity.ok(carService.update(id, carRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id){
        carService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
