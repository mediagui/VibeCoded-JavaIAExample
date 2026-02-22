package com.example.iaexample.service;

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
                    .user(prompt)
                    .call()
                    .chatResponse();

            return response.getResult().getOutput().getContent();
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al comunicarse con Ollama: " + e.getMessage(), e);
        }
    }

    public ChatResponse consultarOllamaCompleto(String prompt) {
        try {
            return chatClient.prompt()
                    .user(prompt)
                    .call()
                    .chatResponse();
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al comunicarse con Ollama: " + e.getMessage(), e);
        }
    }
}
