package com.example.asm2.dtos;

import com.example.asm2.models.ProductOrder;
import com.example.asm2.models.OrderRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrderDto extends OrderDto implements Serializable {
    private String customerId;
    private String customerEmail;
    private Date expectedEndDate;

    private OrderDto componentOrder;

    public ProductOrderDto(ProductOrder productOrder) {
        this.setId(productOrder.getId());
        this.setCreatedDate(productOrder.getCreatedDate());
        this.customerEmail = productOrder.getCustomer().getEmail();
        this.customerId = productOrder.getCustomerId();
        this.expectedEndDate = productOrder.getExpectedEndDate();

        Set<OrderRecordDto> orderItemResponseSet = new HashSet<>();

        for (OrderRecord orderRecord : productOrder.getOrderRecordSet()) {
            orderItemResponseSet.add(new OrderRecordDto((orderRecord)));
        }
        this.setOrderSet(orderItemResponseSet);
        if (productOrder.getComponentOrder() != null) {
            this.setComponentOrder(new OrderDto(productOrder.getComponentOrder()));
        }
    }
}
