package com.example.demo.dto;

import lombok.Data;

@Data
public class ResponseDTO<T> {
    private T       data = null;
    private String  message = "error";
    private boolean result = false;
}
