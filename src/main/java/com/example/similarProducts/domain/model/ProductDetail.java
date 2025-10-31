package com.example.similarProducts.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class ProductDetail {

    @Getter
    private final String id;
    private final String name;
    private final Double price;

    @JsonProperty("available")
    private boolean availability;

    public ProductDetail(String id, String name, Double price, boolean availability) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.availability = availability;
    }

    public String getId() {return id;}
    public String getName() {return name;}
    public Double getPrice() {return price;}
    public boolean isAvailability() {
        return availability;
    }
}
