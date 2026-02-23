package com.example.iaexample.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "nota")
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alumno_asignatura_id", nullable = false)
    private AlumnoAsignatura alumnoAsignatura;

    private Integer calificacion;
    private LocalDateTime fechaRegistro;

    public Nota() {
        this.fechaRegistro = LocalDateTime.now();
    }

    public Nota(AlumnoAsignatura alumnoAsignatura, Integer calificacion) {
        this.alumnoAsignatura = alumnoAsignatura;
        this.calificacion = calificacion;
        this.fechaRegistro = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AlumnoAsignatura getAlumnoAsignatura() {
        return alumnoAsignatura;
    }

    public void setAlumnoAsignatura(AlumnoAsignatura alumnoAsignatura) {
        this.alumnoAsignatura = alumnoAsignatura;
    }

    public Integer getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
        this.fechaRegistro = LocalDateTime.now();
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
