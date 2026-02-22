package com.example.iaexample.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.iaexample.dto.AsignaturaDTO;
import com.example.iaexample.entity.Asignatura;
import com.example.iaexample.repository.AsignaturaRepository;

@Service
@Transactional(readOnly = true)
public class AsignaturaService {

    private final AsignaturaRepository asignaturaRepository;

    public AsignaturaService(AsignaturaRepository asignaturaRepository) {
        this.asignaturaRepository = asignaturaRepository;
    }

    public List<AsignaturaDTO> getAllAsignaturas() {
        return asignaturaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<AsignaturaDTO> getAsignaturaById(Long id) {
        return asignaturaRepository.findById(id)
                .map(this::convertToDTO);
    }

    private AsignaturaDTO convertToDTO(Asignatura asignatura) {
        return new AsignaturaDTO(asignatura.getId(), asignatura.getNombre(), asignatura.getDescripcion());
    }
}
