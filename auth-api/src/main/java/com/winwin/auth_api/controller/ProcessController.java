package com.winwin.auth_api.controller;

import com.winwin.auth_api.entity.LogEntity;
import com.winwin.auth_api.entity.User;
import com.winwin.auth_api.repository.LogRepository;
import com.winwin.auth_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/process")
@RequiredArgsConstructor
public class ProcessController {

    private final LogRepository logRepository;
    private final UserRepository userRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping
    public ResponseEntity<?> processText(@RequestBody Map<String, String> request,
                                         @AuthenticationPrincipal User user) {

        String textToProcess = request.get("text");
        System.out.println("1. Получен текст: " + textToProcess);
        System.out.println("2. Пользователь: " + user.getEmail());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Internal-Token", "super-secret-key-123");

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        String serviceBUrl = "http://data-api:8081/api/transform";

        try {
            System.out.println("3. Отправляю запрос в Service B: " + serviceBUrl);

            ResponseEntity<Map> response = restTemplate.postForEntity(serviceBUrl, entity, Map.class);

            String resultText = (String) response.getBody().get("result");
            System.out.println("4. Ответ от Service B: " + resultText);

            LogEntity log = new LogEntity();
            log.setInputText(textToProcess);
            log.setOutputText(resultText);
            log.setCreatedAt(LocalDateTime.now());
            log.setUser(user);

            logRepository.save(log);
            System.out.println("5. Сохранено в БД!");

            return ResponseEntity.ok(Map.of("result", resultText));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}
