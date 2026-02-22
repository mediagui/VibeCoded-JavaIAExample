package com.example.iaexample.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.iaexample.dto.AlumnoBoletinAsignaturaDTO;
import com.example.iaexample.dto.AlumnoDTO;
import com.example.iaexample.dto.AlumnoDetalleDTO;
import com.example.iaexample.dto.MatriculaRequest;
import com.example.iaexample.dto.NotaRequest;
import com.example.iaexample.service.AlumnoService;

@RestController
@RequestMapping(value = "/api/alumnos", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlumnoController {

    private final AlumnoService alumnoService;

    public AlumnoController(AlumnoService alumnoService) {
        this.alumnoService = alumnoService;
    }

    @GetMapping
    public List<AlumnoDTO> getAllAlumnos() {
        return alumnoService.getAllAlumnos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlumnoDetalleDTO> getAlumnoDetalle(@PathVariable Long id) {
        return alumnoService.getAlumnoDetalle(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{alumnoId}/asignaturas/{asignaturaId}")
    public ResponseEntity<AlumnoBoletinAsignaturaDTO> getBoletinAsignatura(
            @PathVariable Long alumnoId,
            @PathVariable Long asignaturaId) {
        return alumnoService.getBoletinAsignatura(alumnoId, asignaturaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/{alumnoId}/asignaturas", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AlumnoBoletinAsignaturaDTO> matricularAlumno(
            @PathVariable Long alumnoId,
            @RequestBody MatriculaRequest request) {
        return alumnoService.matricularAlumno(alumnoId, request.getAsignaturaId())
                .map(result -> ResponseEntity.status(HttpStatus.CREATED).body(result))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/{alumnoId}/asignaturas/{asignaturaId}/nota", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AlumnoBoletinAsignaturaDTO> registrarNota(
            @PathVariable Long alumnoId,
            @PathVariable Long asignaturaId,
            @RequestBody NotaRequest request) {
        return alumnoService.registrarNota(alumnoId, asignaturaId, request.getNota())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
