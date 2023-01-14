package com.example.asm2.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;

public record ResponseDto(boolean success, String message) implements Serializable {
    public String getTimestamp() {
        return LocalDateTime.now().toString();
    }
}
