package com.example.asm2.pojos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrderRequest extends OrderRequest {
    private String customerId;
    private Date expectedEndDate;
}
