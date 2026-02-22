package com.example.iaexample.dto;

public class AlumnoBoletinAsignaturaDTO {
    private Long alumnoId;
    private String alumnoNombre;
    private String alumnoEmail;
    private Long asignaturaId;
    private String asignaturaNombre;
    private String asignaturaDescripcion;
    private Integer nota;

    public AlumnoBoletinAsignaturaDTO() {
    }

    public AlumnoBoletinAsignaturaDTO(Long alumnoId, String alumnoNombre, String alumnoEmail, Long asignaturaId,
            String asignaturaNombre, String asignaturaDescripcion, Integer nota) {
        this.alumnoId = alumnoId;
        this.alumnoNombre = alumnoNombre;
        this.alumnoEmail = alumnoEmail;
        this.asignaturaId = asignaturaId;
        this.asignaturaNombre = asignaturaNombre;
        this.asignaturaDescripcion = asignaturaDescripcion;
        this.nota = nota;
    }

    public Long getAlumnoId() {
        return alumnoId;
    }

    public void setAlumnoId(Long alumnoId) {
        this.alumnoId = alumnoId;
    }

    public String getAlumnoNombre() {
        return alumnoNombre;
    }

    public void setAlumnoNombre(String alumnoNombre) {
        this.alumnoNombre = alumnoNombre;
    }

    public String getAlumnoEmail() {
        return alumnoEmail;
    }

    public void setAlumnoEmail(String alumnoEmail) {
        this.alumnoEmail = alumnoEmail;
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
