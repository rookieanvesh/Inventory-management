package com.inventory.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {
    @Version
    private Long version;
    //Getters and setters
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Getter
    @Setter
    @Column(unique = true)
    private String sku;

    @Setter
    @Getter
    private String name;

    @Setter
    @Getter
    private BigDecimal price;

    @Setter
    @Getter
    private Integer quantityInStock;

    @Getter
    @Setter
    private boolean active = true;

}