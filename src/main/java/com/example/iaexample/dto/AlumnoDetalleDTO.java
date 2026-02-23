package com.example.iaexample.dto;

import java.util.List;

public class AlumnoDetalleDTO {
    private Long id;
    private String nombre;
    private String email;
    private List<AlumnoAsignaturaNotaDTO> asignaturas;

    public AlumnoDetalleDTO() {
    }

    public AlumnoDetalleDTO(Long id, String nombre, String email, List<AlumnoAsignaturaNotaDTO> asignaturas) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.asignaturas = asignaturas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<AlumnoAsignaturaNotaDTO> getAsignaturas() {
        return asignaturas;
    }

    public void setAsignaturas(List<AlumnoAsignaturaNotaDTO> asignaturas) {
        this.asignaturas = asignaturas;
    }
}
