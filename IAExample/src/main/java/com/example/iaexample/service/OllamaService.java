package com.example.iaexample.service;

import java.util.Objects;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

@Service
public class OllamaService {

    private final ChatClient chatClient;

    public OllamaService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String consultarOllama(String prompt) {
        try {
            ChatResponse response = chatClient.prompt()
                    .user(Objects.requireNonNull(prompt, "prompt"))
                    .call()
                    .chatResponse();

            if (response == null) {
                throw new RuntimeException("No se recibió respuesta de Ollama");
            }

            return response.getResult().getOutput().getContent();
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al comunicarse con Ollama: " + e.getMessage(), e);
        }
    }

    public ChatResponse consultarOllamaCompleto(String prompt) {
        try {
            return chatClient.prompt()
                    .user(Objects.requireNonNull(prompt, "prompt"))
                    .call()
                    .chatResponse();
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al comunicarse con Ollama: " + e.getMessage(), e);
        }
    }
}
