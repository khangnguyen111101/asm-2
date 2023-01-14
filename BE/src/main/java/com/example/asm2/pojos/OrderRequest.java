package com.example.asm2.pojos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest implements Serializable {
    private Set<OrderRecordRequest> orderRecordRequestList;
}
