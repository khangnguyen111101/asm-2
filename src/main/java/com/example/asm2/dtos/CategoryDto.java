package com.example.asm2.dtos;

import com.example.asm2.models.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDto implements Serializable {
    private Long id;
    private String name;

    public CategoryDto (Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }
}
