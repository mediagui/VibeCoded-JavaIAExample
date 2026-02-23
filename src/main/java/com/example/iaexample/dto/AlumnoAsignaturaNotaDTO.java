package com.example.iaexample.dto;

public class AlumnoAsignaturaNotaDTO {
    private Long asignaturaId;
    private String asignaturaNombre;
    private String asignaturaDescripcion;
    private Integer nota;

    public AlumnoAsignaturaNotaDTO() {
    }

    public AlumnoAsignaturaNotaDTO(Long asignaturaId, String asignaturaNombre, String asignaturaDescripcion,
            Integer nota) {
        this.asignaturaId = asignaturaId;
        this.asignaturaNombre = asignaturaNombre;
        this.asignaturaDescripcion = asignaturaDescripcion;
        this.nota = nota;
    }

    public Long getAsignaturaId() {
        return asignaturaId;
    }

    public void setAsignaturaId(Long asignaturaId) {
        this.asignaturaId = asignaturaId;
    }

    public String getAsignaturaNombre() {
        return asignaturaNombre;
    }

    public void setAsignaturaNombre(String asignaturaNombre) {
        this.asignaturaNombre = asignaturaNombre;
    }

    public String getAsignaturaDescripcion() {
        return asignaturaDescripcion;
    }

    public void setAsignaturaDescripcion(String asignaturaDescripcion) {
        this.asignaturaDescripcion = asignaturaDescripcion;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }
}
