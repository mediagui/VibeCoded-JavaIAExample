package com.example.iaexample.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.iaexample.dto.AlumnoDTO;
import com.example.iaexample.dto.AsignaturaDTO;
import com.example.iaexample.service.AlumnoService;
import com.example.iaexample.service.AsignaturaService;

@RestController
@RequestMapping(value = "/api/asignaturas", produces = MediaType.APPLICATION_JSON_VALUE)
public class AsignaturaController {

    private final AsignaturaService asignaturaService;
    private final AlumnoService alumnoService;

    public AsignaturaController(AsignaturaService asignaturaService, AlumnoService alumnoService) {
        this.asignaturaService = asignaturaService;
        this.alumnoService = alumnoService;
    }

    @GetMapping
    public List<AsignaturaDTO> getAllAsignaturas() {
        return asignaturaService.getAllAsignaturas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsignaturaDTO> getAsignaturaById(@PathVariable Long id) {
        return asignaturaService.getAsignaturaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/alumnos")
    public List<AlumnoDTO> getAlumnosByAsignaturaId(@PathVariable Long id) {
        return alumnoService.getAlumnosByAsignaturaId(id);
    }
}
