package com.example.asm2.pojos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ItemRequest implements Serializable {
    private String name;
    private String description;
    private int quantity;
    private boolean isProduct;
    private List<Long> categories;
}
