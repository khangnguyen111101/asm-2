package com.example.asm2.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BOMKey  implements Serializable {
    @Column(name = "product_id") Long productId;
    @Column(name = "component_id") Long componentId;
}
