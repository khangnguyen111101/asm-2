package com.example.asm2.models;

import com.example.asm2.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@Entity
@DiscriminatorValue("component")
public class ComponentOrder extends Order implements Serializable {
    public ComponentOrder() {
        this.setCreatedDate(new Date());
        this.setUpdatedDate(new Date());
        setStatus(StatusEnum.ORDERING);
    }
}
