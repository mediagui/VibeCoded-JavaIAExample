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

import com.example.iaexample.dto.AlumnoDTO;
import com.example.iaexample.entity.Alumno;
import com.example.iaexample.repository.AlumnoRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("AlumnoService Unit Tests")
class AlumnoServiceTest {

    @Mock
    private AlumnoRepository alumnoRepository;

    @InjectMocks
    private AlumnoService alumnoService;

    private Alumno alumno1;
    private Alumno alumno2;

    @BeforeEach
    void setUp() {
        alumno1 = new Alumno();
        alumno1.setId(1L);
        alumno1.setNombre("Juan Pérez");
        alumno1.setEmail("juan@example.com");

        alumno2 = new Alumno();
        alumno2.setId(2L);
        alumno2.setNombre("María González");
        alumno2.setEmail("maria@example.com");
    }

    @Test
    @DisplayName("Should get all alumnos successfully")
    void testGetAllAlumnos() {
        // Arrange
        List<Alumno> alumnos = Arrays.asList(alumno1, alumno2);
        when(alumnoRepository.findAll()).thenReturn(alumnos);

        // Act
        List<AlumnoDTO> result = alumnoService.getAllAlumnos();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Juan Pérez", result.get(0).getNombre());
        assertEquals("maria@example.com", result.get(1).getEmail());
        verify(alumnoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no alumnos exist")
    void testGetAllAlumnosEmpty() {
        // Arrange
        when(alumnoRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<AlumnoDTO> result = alumnoService.getAllAlumnos();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(alumnoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should get alumno detail by id successfully")
    void testGetAlumnoDetalle() {
        // Arrange
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno1));

        // Act
        var result = alumnoService.getAlumnoDetalle(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Juan Pérez", result.get().getNombre());
        assertEquals("juan@example.com", result.get().getEmail());
        verify(alumnoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return empty Optional when alumno not found")
    void testGetAlumnoDetalleNotFound() {
        // Arrange
        when(alumnoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        var result = alumnoService.getAlumnoDetalle(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(alumnoRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should get alumnos by asignatura")
    void testGetAlumnosByAsignaturaId() {
        // Arrange
        List<Alumno> alumnos = Arrays.asList(alumno1);
        when(alumnoRepository.findByAsignaturaId(1L)).thenReturn(alumnos);

        // Act
        List<AlumnoDTO> result = alumnoService.getAlumnosByAsignaturaId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(alumnoRepository, times(1)).findByAsignaturaId(1L);
    }

    @Test
    @DisplayName("Should return empty list when no alumnos in asignatura")
    void testGetAlumnosByAsignaturaIdEmpty() {
        // Arrange
        when(alumnoRepository.findByAsignaturaId(999L)).thenReturn(Arrays.asList());

        // Act
        List<AlumnoDTO> result = alumnoService.getAlumnosByAsignaturaId(999L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(alumnoRepository, times(1)).findByAsignaturaId(999L);
    }
}
