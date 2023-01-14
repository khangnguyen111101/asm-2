package com.example.asm2.pojos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BOMRequest implements Serializable {
    private Long productId;
    private Long componentId;
    private int unit;
}
