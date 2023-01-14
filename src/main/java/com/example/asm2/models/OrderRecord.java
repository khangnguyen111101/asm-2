package com.example.asm2.models;

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
@Table(name = "order_record")
public class OrderRecord implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;
    @Column(name = "item_id", nullable = false)
    private Long itemId;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "order_id")
    private Long orderId;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "updated_date")
    private Date updatedDate;
    @Column @Enumerated(EnumType.ORDINAL)
    private StatusEnum status;

    @ManyToOne
    @JoinColumn(name = "order_id",referencedColumnName = "id",insertable = false,updatable = false)
    @JsonIgnore
    private Order order;

    @OneToOne
    @JoinColumn(name = "item_id",referencedColumnName = "id",insertable = false,updatable = false)
    private Item item;

    public OrderRecord(Long orderId, Long itemId, int quantity) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.status = StatusEnum.ORDERING;
        this.createdDate = new Date();
        this.updatedDate = new Date();
    }
}
