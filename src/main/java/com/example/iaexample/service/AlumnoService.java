package com.example.iaexample.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.iaexample.dto.AlumnoAsignaturaNotaDTO;
import com.example.iaexample.dto.AlumnoBoletinAsignaturaDTO;
import com.example.iaexample.dto.AlumnoDTO;
import com.example.iaexample.dto.AlumnoDetalleDTO;
import com.example.iaexample.entity.Alumno;
import com.example.iaexample.entity.AlumnoAsignatura;
import com.example.iaexample.entity.Asignatura;
import com.example.iaexample.entity.Nota;
import com.example.iaexample.repository.AlumnoAsignaturaRepository;
import com.example.iaexample.repository.AlumnoRepository;
import com.example.iaexample.repository.AsignaturaRepository;

@Service
@Transactional(readOnly = true)
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;
    private final AlumnoAsignaturaRepository alumnoAsignaturaRepository;
    private final AsignaturaRepository asignaturaRepository;

    public AlumnoService(AlumnoRepository alumnoRepository,
            AlumnoAsignaturaRepository alumnoAsignaturaRepository,
            AsignaturaRepository asignaturaRepository) {
        this.alumnoRepository = alumnoRepository;
        this.alumnoAsignaturaRepository = alumnoAsignaturaRepository;
        this.asignaturaRepository = asignaturaRepository;
    }

    public List<AlumnoDTO> getAllAlumnos() {
        return alumnoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<AlumnoDetalleDTO> getAlumnoDetalle(Long id) {
        return alumnoRepository.findById(id)
                .map(this::convertToDetalleDTO);
    }

    public List<AlumnoDTO> getAlumnosByAsignaturaId(Long asignaturaId) {
        return alumnoRepository.findByAsignaturaId(asignaturaId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<AlumnoBoletinAsignaturaDTO> getBoletinAsignatura(Long alumnoId, Long asignaturaId) {
        return alumnoAsignaturaRepository.findByAlumnoIdAndAsignaturaId(alumnoId, asignaturaId)
                .map(this::convertToBoletinAsignaturaDTO);
    }

    @Transactional
    public Optional<AlumnoBoletinAsignaturaDTO> matricularAlumno(Long alumnoId, Long asignaturaId) {
        Optional<Alumno> alumno = alumnoRepository.findById(alumnoId);
        Optional<Asignatura> asignatura = asignaturaRepository.findById(asignaturaId);
        if (alumno.isEmpty() || asignatura.isEmpty()) {
            return Optional.empty();
        }

        Optional<AlumnoAsignatura> existente = alumnoAsignaturaRepository
                .findByAlumnoIdAndAsignaturaId(alumnoId, asignaturaId);
        if (existente.isPresent()) {
            return existente.map(this::convertToBoletinAsignaturaDTO);
        }

        AlumnoAsignatura matricula = new AlumnoAsignatura(alumno.get(), asignatura.get());
        AlumnoAsignatura guardada = alumnoAsignaturaRepository.save(matricula);
        return Optional.of(convertToBoletinAsignaturaDTO(guardada));
    }

    @Transactional
    public Optional<AlumnoBoletinAsignaturaDTO> registrarNota(Long alumnoId, Long asignaturaId, Integer nota) {
        return alumnoAsignaturaRepository.findByAlumnoIdAndAsignaturaId(alumnoId, asignaturaId)
                .map(matricula -> {
                    if (matricula.getNota() == null) {
                        Nota nueva = new Nota(matricula, nota);
                        matricula.setNota(nueva);
                    } else {
                        matricula.getNota().setCalificacion(nota);
                    }
                    AlumnoAsignatura guardada = alumnoAsignaturaRepository.save(matricula);
                    return convertToBoletinAsignaturaDTO(guardada);
                });
    }

    private AlumnoDTO convertToDTO(Alumno alumno) {
        return new AlumnoDTO(alumno.getId(), alumno.getNombre(), alumno.getEmail());
    }

    private AlumnoDetalleDTO convertToDetalleDTO(Alumno alumno) {
        List<AlumnoAsignaturaNotaDTO> asignaturas = alumno.getMatriculas().stream()
                .map(this::convertToAsignaturaNotaDTO)
                .collect(Collectors.toList());

        return new AlumnoDetalleDTO(alumno.getId(), alumno.getNombre(), alumno.getEmail(), asignaturas);
    }

    private AlumnoAsignaturaNotaDTO convertToAsignaturaNotaDTO(AlumnoAsignatura matricula) {
        Integer nota = matricula.getNota() != null ? matricula.getNota().getCalificacion() : null;
        return new AlumnoAsignaturaNotaDTO(
                matricula.getAsignatura().getId(),
                matricula.getAsignatura().getNombre(),
                matricula.getAsignatura().getDescripcion(),
                nota);
    }

    private AlumnoBoletinAsignaturaDTO convertToBoletinAsignaturaDTO(AlumnoAsignatura matricula) {
        Integer nota = matricula.getNota() != null ? matricula.getNota().getCalificacion() : null;
        return new AlumnoBoletinAsignaturaDTO(
                matricula.getAlumno().getId(),
                matricula.getAlumno().getNombre(),
                matricula.getAlumno().getEmail(),
                matricula.getAsignatura().getId(),
                matricula.getAsignatura().getNombre(),
                matricula.getAsignatura().getDescripcion(),
                nota);
    }
}
