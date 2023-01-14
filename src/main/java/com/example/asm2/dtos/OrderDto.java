package com.example.asm2.dtos;

import com.example.asm2.models.ComponentOrder;
import com.example.asm2.models.OrderRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto implements Serializable {
    private Long id;
    private Date createdDate;
    private Set<OrderRecordDto> orderSet = new HashSet<>();

    public OrderDto(ComponentOrder order) {
        this.id = order.getId();
        this.createdDate = order.getCreatedDate();

        if (order.getOrderRecordSet() != null) {
            for (OrderRecord orderRecord : order.getOrderRecordSet()) {
                orderSet.add(new OrderRecordDto((orderRecord)));
            }
        }
    }
}
