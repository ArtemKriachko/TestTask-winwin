package com.winwin.data_api.controller;

import com.winwin.data_api.dto.TransformationRequest;
import com.winwin.data_api.service.TransformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TransformationController {

    private final TransformationService transformationService;

    @Value("${internal.token}")
    private String internalToken;

    @PostMapping("/transform")
    public ResponseEntity<?> transform(@RequestBody TransformationRequest request,
                                       @RequestHeader(value = "X-Internal-Token", required = false) String token) {

        if (token == null || !token.equals(internalToken)) {
            return ResponseEntity.status(403).body("Access Denied");
        }

        String result = transformationService.processText(request.getText());
        return ResponseEntity.ok(Map.of("result", result));
    }
}