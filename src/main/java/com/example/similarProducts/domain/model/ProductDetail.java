package com.example.similarProducts.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class ProductDetail {

    private final String id;
    private final String name;
    private final Double price;
    private final boolean availability;

    @JsonCreator
    public ProductDetail(@JsonProperty("id") String id,
                         @JsonProperty("name") String name,
                         @JsonProperty("price") Double price,
                         @JsonProperty("availability") boolean availability) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.availability = availability;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public Double getPrice() { return price; }
    public boolean isAvailability() { return availability; }
}

