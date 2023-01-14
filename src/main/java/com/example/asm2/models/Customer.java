package com.example.asm2.models;

import com.example.asm2.pojos.CustomerRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Customer implements Serializable {
    @Id private String id;
    @Column private String email;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id",referencedColumnName = "id",insertable = false,updatable = false)
    @JsonIgnore
    private List<ProductOrder> productOrderList;

    public Customer(CustomerRequest customerRequest) {
        this.id = customerRequest.getId();
        this.email = customerRequest.getEmail();
    }
}
