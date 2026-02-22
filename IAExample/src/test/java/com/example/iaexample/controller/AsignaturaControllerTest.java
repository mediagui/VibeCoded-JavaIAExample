package com.example.iaexample.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.iaexample.dto.AlumnoDTO;
import com.example.iaexample.dto.AsignaturaDTO;
import com.example.iaexample.service.AlumnoService;
import com.example.iaexample.service.AsignaturaService;

@WebMvcTest(AsignaturaController.class)
@DisplayName("AsignaturaController Integration Tests")
class AsignaturaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AsignaturaService asignaturaService;

    @MockBean
    private AlumnoService alumnoService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("GET /api/asignaturas should return all asignaturas")
    void testGetAllAsignaturas() throws Exception {
        // Arrange
        List<AsignaturaDTO> asignaturas = Arrays.asList(
                new AsignaturaDTO(1L, "Matemáticas", "Álgebra, geometría y cálculo básico"),
                new AsignaturaDTO(2L, "Lengua", "Comprensión lectora y escritura"),
                new AsignaturaDTO(3L, "Historia", "Historia universal y local"));
        when(asignaturaService.getAllAsignaturas()).thenReturn(asignaturas);

        // Act & Assert
        mockMvc.perform(get("/api/asignaturas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].nombre", is("Matemáticas")))
                .andExpect(jsonPath("$[1].descripcion", is("Comprensión lectora y escritura")))
                .andExpect(jsonPath("$[2].id", is(3)));

        verify(asignaturaService, times(1)).getAllAsignaturas();
    }

    @Test
    @DisplayName("GET /api/asignaturas/{id} should return asignatura detail")
    void testGetAsignaturaById() throws Exception {
        // Arrange
        AsignaturaDTO asignatura = new AsignaturaDTO(
                1L, "Matemáticas", "Álgebra, geometría y cálculo básico");
        when(asignaturaService.getAsignaturaById(1L)).thenReturn(Optional.of(asignatura));

        // Act & Assert
        mockMvc.perform(get("/api/asignaturas/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Matemáticas")))
                .andExpect(jsonPath("$.descripcion", containsString("Álgebra")));

        verify(asignaturaService, times(1)).getAsignaturaById(1L);
    }

    @Test
    @DisplayName("GET /api/asignaturas/{id} should return 204 when asignatura not found")
    void testGetAsignaturaByIdNotFound() throws Exception {
        // Arrange
        when(asignaturaService.getAsignaturaById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/asignaturas/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(asignaturaService, times(1)).getAsignaturaById(999L);
    }

    @Test
    @DisplayName("GET /api/asignaturas/{id}/alumnos should return students in asignatura")
    void testGetAlumnosByAsignatura() throws Exception {
        // Arrange
        List<AlumnoDTO> alumnos = Arrays.asList(
                new AlumnoDTO(1L, "Juan Pérez", "juan@example.com"),
                new AlumnoDTO(2L, "María González", "maria@example.com"));
        when(alumnoService.getAlumnosByAsignaturaId(1L)).thenReturn(alumnos);

        // Act & Assert
        mockMvc.perform(get("/api/asignaturas/1/alumnos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Juan Pérez")))
                .andExpect(jsonPath("$[1].email", is("maria@example.com")));

        verify(alumnoService, times(1)).getAlumnosByAsignaturaId(1L);
    }

    @Test
    @DisplayName("GET /api/asignaturas/{id}/alumnos should return empty list when no students")
    void testGetAlumnosByAsignaturaEmpty() throws Exception {
        // Arrange
        when(alumnoService.getAlumnosByAsignaturaId(999L)).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/asignaturas/999/alumnos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(alumnoService, times(1)).getAlumnosByAsignaturaId(999L);
    }
}
