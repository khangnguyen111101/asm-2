package com.example.asm2.dtos;

import com.example.asm2.models.OrderRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRecordDto implements Serializable {
    private Long itemId;
    private String name;
    private int quantity;

    public OrderRecordDto(OrderRecord orderRecord) {
        this.itemId = orderRecord.getItemId();
        this.name = orderRecord.getItem().getName();
        this.quantity = orderRecord.getQuantity();
    }
}
