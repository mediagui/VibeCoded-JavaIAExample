package com.example.iaexample.dto;

public class NotaRequest {
    private Integer nota;

    public NotaRequest() {
    }

    public NotaRequest(Integer nota) {
        this.nota = nota;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }
}
