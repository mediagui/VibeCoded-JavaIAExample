package com.example.iaexample.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.iaexample.config.DeepseekProperties;
import com.example.iaexample.dto.deepseek.DeepseekMessage;
import com.example.iaexample.dto.deepseek.DeepseekRequest;
import com.example.iaexample.dto.deepseek.DeepseekResponse;

@Service
public class DeepseekService {
    private final DeepseekProperties properties;
    private final RestTemplate restTemplate;

    public DeepseekService(DeepseekProperties properties, RestTemplateBuilder builder) {
        this.properties = properties;
        this.restTemplate = builder.build();
    }

    public DeepseekResponse consultarDeepseek(String prompt) {
        DeepseekRequest request = construirSolicitud(prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        try {
            ResponseEntity<DeepseekResponse> response = restTemplate.postForEntity(
                    properties.getUrl(),
                    new org.springframework.http.HttpEntity<>(request, headers),
                    DeepseekResponse.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new RuntimeException("Error al consultar Ollama: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al comunicarse con Ollama (¿está corriendo 'ollama serve'?): " + e.getMessage(), e);
        }
    }

    private DeepseekRequest construirSolicitud(String prompt) {
        DeepseekRequest request = new DeepseekRequest();
        request.setModel(properties.getModel());
        request.setTemperature(properties.getTemperature());
        request.setMaxTokens(properties.getMaxTokens());

        List<DeepseekMessage> messages = new ArrayList<>();
        messages.add(new DeepseekMessage("user", prompt));
        request.setMessages(messages);

        return request;
    }

    public String extraerRespuesta(DeepseekResponse response) {
        if (response.getChoices() != null && !response.getChoices().isEmpty()) {
            return response.getChoices().get(0).getMessage().getContent();
        }
        throw new RuntimeException("No se obtuvo respuesta válida de Ollama");
    }
}
