package com.catalog.cars.specification;

import com.catalog.cars.model.car.Car;
import org.springframework.data.jpa.domain.Specification;

public class CarSpecification {

    public static Specification<Car> hasMake(String make) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("make"), make);
    }

    public static Specification<Car> hasModel(String model) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("model"), model);
    }

    public static Specification<Car> hasYear(Integer year) {
        return (root, query, criteriaBuilder) -> {
            if (year == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("year"), year);
        };
    }
}
