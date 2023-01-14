package com.example.asm2.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BOM implements Serializable {
    @EmbeddedId
    BOMKey id;

    @ManyToOne
    @MapsId("product_id")
    @JoinColumn(name = "product_id")
    @JsonBackReference
    Item product;

    @ManyToOne
    @MapsId("component_id")
    @JoinColumn(name = "component_id")
    @JsonBackReference
    Item component;

    private int unit;
}