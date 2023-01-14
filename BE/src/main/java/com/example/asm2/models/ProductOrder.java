package com.example.asm2.models;

import com.example.asm2.pojos.ProductOrderRequest;
import com.example.asm2.enums.StatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("product")
public class ProductOrder extends Order implements Serializable {
    @Column(name = "customer_id") @JsonIgnore
    private String customerId;

    @Column(name = "expected_end_date")
    private Date expectedEndDate;

    @Column(name = "purchase_order_id")
    private Long purchaseOrderId;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Customer customer;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "component_order_id",referencedColumnName = "id",insertable = false,updatable = false)
    private ComponentOrder componentOrder;

    public ProductOrder(ProductOrderRequest productOrderRequest) {
        super();
        this.customerId = productOrderRequest.getCustomerId();
        this.expectedEndDate = productOrderRequest.getExpectedEndDate();
        this.setStatus(StatusEnum.ORDERING);
        this.setCreatedDate(new Date());
        this.setUpdatedDate(new Date());
    }
}
