package com.example.iaexample.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConsultaIAResponse {
    private String prompt;
    private String respuesta;

    @JsonProperty("tokens_utilizados")
    private Integer tokensUtilizados;

    @JsonProperty("modelo_ia")
    private String modeloIA;

    public ConsultaIAResponse() {
    }

    public ConsultaIAResponse(String prompt, String respuesta, Integer tokensUtilizados, String modeloIA) {
        this.prompt = prompt;
        this.respuesta = respuesta;
        this.tokensUtilizados = tokensUtilizados;
        this.modeloIA = modeloIA;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public Integer getTokensUtilizados() {
        return tokensUtilizados;
    }

    public void setTokensUtilizados(Integer tokensUtilizados) {
        this.tokensUtilizados = tokensUtilizados;
    }

    public String getModeloIA() {
        return modeloIA;
    }

    public void setModeloIA(String modeloIA) {
        this.modeloIA = modeloIA;
    }
}
