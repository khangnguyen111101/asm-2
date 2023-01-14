package com.example.asm2.dtos;

import com.example.asm2.models.BOM;
import com.example.asm2.models.Category;
import com.example.asm2.models.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ItemDto implements Serializable {
    private Long id;
    private String name;
    private String description;
    private boolean is_product;
    private int quantity;
    private List<CategoryDto> categories = new ArrayList<>();
    private List<ComponentDto> components = new ArrayList<>();

    public ItemDto(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.is_product = item.isProduct();
        this.quantity = item.getQuantity();

        Set<Category> categorySet = item.getCategories();

        Set<BOM> componentOfSet = item.getProductOf();

        for (BOM bom: componentOfSet) {
            ComponentDto componentDto = new ComponentDto(bom.getComponent(), bom.getUnit());
            components.add(componentDto);
        }

        for (Category category: categorySet) {
            CategoryDto categoryDto = new CategoryDto(category);
            categories.add(categoryDto);
        }
    }
}
