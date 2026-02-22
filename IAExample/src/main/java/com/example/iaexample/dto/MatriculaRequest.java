package com.example.iaexample.dto;

public class MatriculaRequest {
    private Long asignaturaId;

    public MatriculaRequest() {
    }

    public MatriculaRequest(Long asignaturaId) {
        this.asignaturaId = asignaturaId;
    }

    public Long getAsignaturaId() {
        return asignaturaId;
    }

    public void setAsignaturaId(Long asignaturaId) {
        this.asignaturaId = asignaturaId;
    }
}
