package com.example.asm2.dtos;

import com.example.asm2.models.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class ComponentDto implements Serializable {
    private Long id;
    private String name;
    private int unit;

    public ComponentDto(Item item, int unit) {
        this.id = item.getId();
        this.name = item.getName();
        this.unit = unit;
    }
}
