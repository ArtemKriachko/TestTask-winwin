package com.winwin.data_api.service;

import org.springframework.stereotype.Service;

@Service
public class TransformationService {
    public String processText(String input) {
        if (input == null) return "";

        return new StringBuilder(input).reverse().toString().toUpperCase();
    }
}
