package com.example.asm2.models;

import com.example.asm2.pojos.ItemRequest;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Item implements Serializable  {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    @Column private String name;
    @Column private String description;
    @Column private int quantity;
    @Column(name = "is_product") private boolean isProduct;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "item_category",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<BOM> productOf;

    @OneToMany(mappedBy = "component", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<BOM> componentOf;

    public Item(ItemRequest itemRequest) {
        this.name = itemRequest.getName();
        this.description = itemRequest.getDescription();
        this.quantity = itemRequest.getQuantity();
        this.isProduct = itemRequest.isProduct();
        this.categories = new HashSet<>();
        this.productOf = new HashSet<>();
        this.componentOf = new HashSet<>();
    }

}
