package com.example.asm2.models;

import com.example.asm2.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Setter
@Getter
@Entity(name="orders")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="order_type",
        discriminatorType = DiscriminatorType.STRING)
public abstract class Order implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column @Enumerated(EnumType.ORDINAL)
    private StatusEnum status;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "updated_date")
    private Date updatedDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id",referencedColumnName = "id",insertable = false,updatable = false)
    private Set<OrderRecord> orderRecordSet;

    public void addOrderItem(OrderRecord orderRecord) {
        orderRecord.setOrder(this);
        this.orderRecordSet.add(orderRecord);
    }
}
