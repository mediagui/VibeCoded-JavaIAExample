package com.example.iaexample.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.iaexample.dto.AsignaturaDTO;
import com.example.iaexample.entity.Asignatura;
import com.example.iaexample.repository.AsignaturaRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("AsignaturaService Unit Tests")
class AsignaturaServiceTest {

    @Mock
    private AsignaturaRepository asignaturaRepository;

    @InjectMocks
    private AsignaturaService asignaturaService;

    private Asignatura asignatura1;
    private Asignatura asignatura2;

    @BeforeEach
    void setUp() {
        asignatura1 = new Asignatura();
        asignatura1.setId(1L);
        asignatura1.setNombre("Matemáticas");
        asignatura1.setDescripcion("Álgebra, geometría y cálculo básico");

        asignatura2 = new Asignatura();
        asignatura2.setId(2L);
        asignatura2.setNombre("Lengua");
        asignatura2.setDescripcion("Comprensión lectora y escritura");
    }

    @Test
    @DisplayName("Should get all asignaturas successfully")
    void testGetAllAsignaturas() {
        // Arrange
        List<Asignatura> asignaturas = Arrays.asList(asignatura1, asignatura2);
        when(asignaturaRepository.findAll()).thenReturn(asignaturas);

        // Act
        List<AsignaturaDTO> result = asignaturaService.getAllAsignaturas();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Matemáticas", result.get(0).getNombre());
        verify(asignaturaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no asignaturas exist")
    void testGetAllAsignaturasEmpty() {
        // Arrange
        when(asignaturaRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<AsignaturaDTO> result = asignaturaService.getAllAsignaturas();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(asignaturaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should get asignatura by id successfully")
    void testGetAsignaturaById() {
        // Arrange
        when(asignaturaRepository.findById(1L)).thenReturn(Optional.of(asignatura1));

        // Act
        Optional<AsignaturaDTO> result = asignaturaService.getAsignaturaById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Matemáticas", result.get().getNombre());
        assertEquals("Álgebra, geometría y cálculo básico", result.get().getDescripcion());
        verify(asignaturaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return empty Optional when asignatura not found")
    void testGetAsignaturaByIdNotFound() {
        // Arrange
        when(asignaturaRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<AsignaturaDTO> result = asignaturaService.getAsignaturaById(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(asignaturaRepository, times(1)).findById(999L);
    }
}
