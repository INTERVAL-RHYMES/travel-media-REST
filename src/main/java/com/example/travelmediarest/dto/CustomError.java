package com.example.travelmediarest.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class CustomError {
    public Map<String, String> customErrors = new HashMap<>();

    public void addError(String key, String value) {
        customErrors.put(key, value);
    }
}
