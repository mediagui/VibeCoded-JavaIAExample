package com.example.iaexample.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

import com.example.iaexample.dto.AlumnoAsignaturaNotaDTO;
import com.example.iaexample.dto.AlumnoBoletinAsignaturaDTO;
import com.example.iaexample.dto.AlumnoDTO;
import com.example.iaexample.dto.AlumnoDetalleDTO;
import com.example.iaexample.service.AlumnoService;
import com.example.iaexample.service.AsignaturaService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AlumnoController.class)
@DisplayName("AlumnoController Integration Tests")
class AlumnoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlumnoService alumnoService;

    @MockBean
    private AsignaturaService asignaturaService;

    @Autowired
    private ObjectMapper objectMapper;

    private AlumnoDTO alumnoDTO;
    private AlumnoDetalleDTO alumnoDetalleDTO;

    @BeforeEach
    void setUp() {
        alumnoDTO = new AlumnoDTO(1L, "Juan Pérez", "juan@example.com");

        List<AlumnoAsignaturaNotaDTO> asignaturas = Arrays.asList(
                new AlumnoAsignaturaNotaDTO(1L, "Matemáticas", "Álgebra", 9),
                new AlumnoAsignaturaNotaDTO(2L, "Lengua", "Escritura", 8));

        alumnoDetalleDTO = new AlumnoDetalleDTO(1L, "Juan Pérez", "juan@example.com", asignaturas);
    }

    @Test
    @DisplayName("GET /api/alumnos should return all alumnos")
    void testGetAllAlumnos() throws Exception {
        // Arrange
        List<AlumnoDTO> alumnos = Arrays.asList(
                new AlumnoDTO(1L, "Juan Pérez", "juan@example.com"),
                new AlumnoDTO(2L, "María González", "maria@example.com"));
        when(alumnoService.getAllAlumnos()).thenReturn(alumnos);

        // Act & Assert
        mockMvc.perform(get("/api/alumnos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Juan Pérez")))
                .andExpect(jsonPath("$[1].email", is("maria@example.com")));

        verify(alumnoService, times(1)).getAllAlumnos();
    }

    @Test
    @DisplayName("GET /api/alumnos/{id} should return alumno detail")
    void testGetAlumnoDetalle() throws Exception {
        // Arrange
        when(alumnoService.getAlumnoDetalle(1L)).thenReturn(Optional.of(alumnoDetalleDTO));

        // Act & Assert
        mockMvc.perform(get("/api/alumnos/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Juan Pérez")))
                .andExpect(jsonPath("$.asignaturas", hasSize(2)))
                .andExpect(jsonPath("$.asignaturas[0].nota", is(9)));

        verify(alumnoService, times(1)).getAlumnoDetalle(1L);
    }

    @Test
    @DisplayName("GET /api/alumnos/{id} should return 404 when alumno not found")
    void testGetAlumnoDetalleNotFound() throws Exception {
        // Arrange
        when(alumnoService.getAlumnoDetalle(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/alumnos/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(alumnoService, times(1)).getAlumnoDetalle(999L);
    }

    @Test
    @DisplayName("POST /api/alumnos/{alumnoId}/asignaturas should matriculate student")
    void testMatricularAlumno() throws Exception {
        // Arrange
        AlumnoBoletinAsignaturaDTO result = new AlumnoBoletinAsignaturaDTO(
                1L, "Juan Pérez", "juan@example.com",
                2L, "Lengua", "Comprensión lectora", null);
        when(alumnoService.matricularAlumno(1L, 2L))
                .thenReturn(Optional.of(result));

        String requestBody = "{\"asignaturaId\": 2}";

        // Act & Assert
        mockMvc.perform(post("/api/alumnos/1/asignaturas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.alumnoNombre", is("Juan Pérez")))
                .andExpect(jsonPath("$.asignaturaNombre", is("Lengua")))
                .andExpect(jsonPath("$.nota", nullValue()));

        verify(alumnoService, times(1)).matricularAlumno(1L, 2L);
    }

    @Test
    @DisplayName("PUT /api/alumnos/{alumnoId}/asignaturas/{asignaturaId}/nota should update grade")
    void testRegistrarNota() throws Exception {
        // Arrange
        AlumnoBoletinAsignaturaDTO result = new AlumnoBoletinAsignaturaDTO(
                1L, "Juan Pérez", "juan@example.com",
                2L, "Lengua", "Comprensión lectora", 9);
        when(alumnoService.registrarNota(1L, 2L, 9))
                .thenReturn(Optional.of(result));

        String requestBody = "{\"nota\": 9}";

        // Act & Assert
        mockMvc.perform(put("/api/alumnos/1/asignaturas/2/nota")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nota", is(9)))
                .andExpect(jsonPath("$.asignaturaNombre", is("Lengua")));

        verify(alumnoService, times(1)).registrarNota(1L, 2L, 9);
    }

    @Test
    @DisplayName("GET /api/alumnos/{alumnoId}/asignaturas/{asignaturaId} should return grade")
    void testGetBoletinAsignatura() throws Exception {
        // Arrange
        AlumnoBoletinAsignaturaDTO result = new AlumnoBoletinAsignaturaDTO(
                1L, "Juan Pérez", "juan@example.com",
                1L, "Matemáticas", "Álgebra", 9);
        when(alumnoService.getBoletinAsignatura(1L, 1L))
                .thenReturn(Optional.of(result));

        // Act & Assert
        mockMvc.perform(get("/api/alumnos/1/asignaturas/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nota", is(9)))
                .andExpect(jsonPath("$.asignaturaNombre", is("Matemáticas")));

        verify(alumnoService, times(1)).getBoletinAsignatura(1L, 1L);
    }

    @Test
    @DisplayName("POST /api/alumnos/{alumnoId}/asignaturas should return 400 for invalid input")
    void testMatricularAlumnoInvalidInput() throws Exception {
        // Arrange
        String requestBody = "{\"asignaturaId\": \"invalid\"}";

        // Act & Assert
        mockMvc.perform(post("/api/alumnos/1/asignaturas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}
