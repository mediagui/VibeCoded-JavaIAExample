package com.example.iaexample.dto.deepseek;

public class DeepseekChoice {
    private Integer index;
    private DeepseekMessage message;
    private String finishReason;

    public DeepseekChoice() {
    }

    public DeepseekChoice(Integer index, DeepseekMessage message, String finishReason) {
        this.index = index;
        this.message = message;
        this.finishReason = finishReason;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public DeepseekMessage getMessage() {
        return message;
    }

    public void setMessage(DeepseekMessage message) {
        this.message = message;
    }

    public String getFinishReason() {
        return finishReason;
    }

    public void setFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }
}
