package com.example.iaexample;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("End-to-End Integration Tests")
class ApplicationE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("E2E: Get all alumnos flow")
    void testE2EGetAllAlumnos() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/alumnos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].nombre", notNullValue()))
                .andExpect(jsonPath("$[0].email", notNullValue()));
    }

    @Test
    @DisplayName("E2E: Get all asignaturas flow")
    void testE2EGetAllAsignaturas() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/asignaturas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].nombre", notNullValue()));
    }

    @Test
    @DisplayName("E2E: Get alumno detail flow")
    void testE2EGetAlumnoDetail() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/alumnos/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", notNullValue()))
                .andExpect(jsonPath("$.email", notNullValue()))
                .andExpect(jsonPath("$.asignaturas", instanceOf(java.util.List.class)));
    }

    @Test
    @DisplayName("E2E: Get asignatura detail flow")
    void testE2EGetAsignaturaDetail() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/asignaturas/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", notNullValue()))
                .andExpect(jsonPath("$.descripcion", notNullValue()));
    }

    @Test
    @DisplayName("E2E: Get students in asignatura flow")
    void testE2EGetAlumnosByAsignatura() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/asignaturas/1/alumnos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", instanceOf(java.util.List.class)));
    }

    @Test
    @DisplayName("E2E: Matriculate student flow")
    void testE2EMatricularAlumno() throws Exception {
        // Arrange
        String requestBody = "{\"asignaturaId\": 2}";

        // Act & Assert
        mockMvc.perform(post("/api/alumnos/2/asignaturas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.alumnoId", is(2)))
                .andExpect(jsonPath("$.asignaturaId", is(2)))
                .andExpect(jsonPath("$.alumnoNombre", notNullValue()))
                .andExpect(jsonPath("$.asignaturaNombre", notNullValue()));
    }

    @Test
    @DisplayName("E2E: Register grade flow")
    void testE2ERegistrarNota() throws Exception {
        // Arrange
        // Primero matricular
        String matriculaBody = "{\"asignaturaId\": 2}";
        mockMvc.perform(post("/api/alumnos/2/asignaturas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(matriculaBody))
                .andExpect(status().isCreated());

        // Luego registrar nota
        String notaBody = "{\"nota\": 8}";

        // Act & Assert
        mockMvc.perform(put("/api/alumnos/2/asignaturas/2/nota")
                .contentType(MediaType.APPLICATION_JSON)
                .content(notaBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nota", is(8)))
                .andExpect(jsonPath("$.alumnoId", is(2)))
                .andExpect(jsonPath("$.asignaturaId", is(2)));
    }

    @Test
    @DisplayName("E2E: Get nota from boletín flow")
    void testE2EGetBoletinAsignatura() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/alumnos/1/asignaturas/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.alumnoId", is(1)))
                .andExpect(jsonPath("$.asignaturaId", is(1)))
                .andExpect(jsonPath("$.nota", notNullValue()));
    }

    @Test
    @DisplayName("E2E: Complex flow - Multiple operations")
    void testE2EComplexFlow() throws Exception {
        // 1. Get all alumnos
        mockMvc.perform(get("/api/alumnos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // 2. Get all asignaturas
        mockMvc.perform(get("/api/asignaturas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // 3. Get alumno detail
        mockMvc.perform(get("/api/alumnos/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // 4. Get asignatura detail
        mockMvc.perform(get("/api/asignaturas/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // 5. Get students in asignatura
        mockMvc.perform(get("/api/asignaturas/1/alumnos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("E2E: Invalid alumno ID flow")
    void testE2EInvalidAlumnoId() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/alumnos/9999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("E2E: Invalid asignatura ID flow")
    void testE2EInvalidAsignaturaId() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/asignaturas/9999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("E2E: Multiple grade registration flow")
    void testE2EMultipleGradeRegistrations() throws Exception {
        // Register multiple grades
        for (int nota = 7; nota <= 9; nota++) {
            String requestBody = "{\"nota\": " + nota + "}";

            mockMvc.perform(put("/api/alumnos/1/asignaturas/1/nota")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nota", is(nota)));
        }
    }

    @Test
    @DisplayName("E2E: Verify data consistency after operations")
    void testE2EDataConsistency() throws Exception {
        // Get alumno detail
        mockMvc.perform(get("/api/alumnos/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.asignaturas", not(empty())))
                .andExpect(jsonPath("$.asignaturas[0].nota", notNullValue()));

        // Verify the same data with boletín endpoint
        mockMvc.perform(get("/api/alumnos/1/asignaturas/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.alumnoId", is(1)))
                .andExpect(jsonPath("$.asignaturaId", is(1)));
    }

    @Test
    @DisplayName("E2E: Content type validation")
    void testE2EContentType() throws Exception {
        // All responses should be JSON
        mockMvc.perform(get("/api/alumnos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/api/asignaturas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("E2E: Retrieve full alumno profile")
    void testE2EFullAlumnoProfile() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/alumnos/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", notNullValue()))
                .andExpect(jsonPath("$.email", notNullValue()))
                .andExpect(jsonPath("$.asignaturas", notNullValue()))
                .andExpect(jsonPath("$.asignaturas[*].asignaturaId", not(empty())))
                .andExpect(jsonPath("$.asignaturas[*].asignaturaNombre", not(empty())));
    }
}
