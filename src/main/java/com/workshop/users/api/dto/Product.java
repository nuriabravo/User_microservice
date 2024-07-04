package com.workshop.users.api.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class Product implements Serializable {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer categoryId;
    private Double weight;
    private Integer currentStock;
    private Integer minStock;
}
