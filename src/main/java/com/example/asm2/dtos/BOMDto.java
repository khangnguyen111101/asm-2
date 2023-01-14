package com.example.asm2.dtos;

import com.example.asm2.models.BOM;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BOMDto implements Serializable {
    private Long productId;
    private String productName;
    private Long componentId;
    private String componentName;
    private int unit;

    public BOMDto(BOM bom) {
        this.productId = bom.getId().getProductId();
        this.productName = bom.getProduct().getName();
        this.componentId = bom.getId().getComponentId();
        this.componentName = bom.getComponent().getName();
        this.unit = bom.getUnit();
    }
}
