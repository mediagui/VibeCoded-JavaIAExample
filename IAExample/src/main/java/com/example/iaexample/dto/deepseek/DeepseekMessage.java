package com.example.iaexample.dto.deepseek;

public class DeepseekMessage {
    private String role;
    private String content;

    public DeepseekMessage() {
    }

    public DeepseekMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
