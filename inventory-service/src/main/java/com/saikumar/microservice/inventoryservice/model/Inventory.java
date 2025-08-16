package com.saikumar.microservice.inventoryservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Entity
@Table(name="t_inventory")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Service
public class Inventory{

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;
    private String skuCode;
    private Integer quantity;
}
