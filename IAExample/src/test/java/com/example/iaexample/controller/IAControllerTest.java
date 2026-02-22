package com.example.iaexample.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

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
import com.example.iaexample.dto.deepseek.DeepseekChoice;
import com.example.iaexample.dto.deepseek.DeepseekMessage;
import com.example.iaexample.dto.deepseek.DeepseekResponse;
import com.example.iaexample.service.AlumnoService;
import com.example.iaexample.service.AsignaturaService;
import com.example.iaexample.service.DeepseekService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(IAController.class)
@DisplayName("IAController Integration Tests")
class IAControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeepseekService deepseekService;

    @MockBean
    private AlumnoService alumnoService;

    @MockBean
    private AsignaturaService asignaturaService;

    @Autowired
    private ObjectMapper objectMapper;

    private DeepseekResponse mockDeepseekResponse;

    @BeforeEach
    void setUp() {
        // Configurar mocks de servicios
        when(alumnoService.getAllAlumnos()).thenReturn(Arrays.asList(
                new AlumnoDTO(1L, "Juan Pérez", "juan@example.com"),
                new AlumnoDTO(2L, "María González", "maria@example.com")));

        when(asignaturaService.getAllAsignaturas()).thenReturn(Arrays.asList(
                new AsignaturaDTO(1L, "Matemáticas", "Álgebra"),
                new AsignaturaDTO(2L, "Lengua", "Escritura")));

        // Crear respuesta mock de Deepseek
        mockDeepseekResponse = new DeepseekResponse();
        mockDeepseekResponse.setId("mock-id");
        mockDeepseekResponse.setModel("deepseek-chat");

        DeepseekChoice choice = new DeepseekChoice();
        DeepseekMessage message = new DeepseekMessage();
        message.setRole("assistant");
        message.setContent("Hay 2 alumnos en el sistema");
        choice.setMessage(message);
        choice.setIndex(0);

        mockDeepseekResponse.setChoices(Arrays.asList(choice));

        DeepseekResponse.Usage usage = new DeepseekResponse.Usage();
        usage.setPromptTokens(15);
        usage.setCompletionTokens(10);
        usage.setTotalTokens(25);
        mockDeepseekResponse.setUsage(usage);
    }

    @Test
    @DisplayName("POST /api/ia/consultar should return IA response")
    void testConsultarIA() throws Exception {
        // Arrange
        when(deepseekService.consultarDeepseek(anyString()))
                .thenReturn(mockDeepseekResponse);
        when(deepseekService.extraerRespuesta(mockDeepseekResponse))
                .thenReturn("Hay 2 alumnos en el sistema");

        String requestBody = "{\"prompt\": \"¿Cuántos alumnos hay?\"}";

        // Act & Assert
        mockMvc.perform(post("/api/ia/consultar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prompt", is("¿Cuántos alumnos hay?")))
                .andExpect(jsonPath("$.respuesta", is("Hay 2 alumnos en el sistema")))
                .andExpect(jsonPath("$.tokens_utilizados", is(25)))
                .andExpect(jsonPath("$.modelo_ia", is("deepseek-chat")));

        verify(deepseekService, times(1)).consultarDeepseek(anyString());
    }

    @Test
    @DisplayName("POST /api/ia/consultar should reject empty prompt")
    void testConsultarIAEmptyPrompt() throws Exception {
        // Arrange
        String requestBody = "{\"prompt\": \"\"}";

        // Act & Assert
        mockMvc.perform(post("/api/ia/consultar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());

        verify(deepseekService, times(0)).consultarDeepseek(anyString());
    }

    @Test
    @DisplayName("POST /api/ia/consultar should reject null prompt")
    void testConsultarIANullPrompt() throws Exception {
        // Arrange
        String requestBody = "{\"prompt\": null}";

        // Act & Assert
        mockMvc.perform(post("/api/ia/consultar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());

        verify(deepseekService, times(0)).consultarDeepseek(anyString());
    }

    @Test
    @DisplayName("POST /api/ia/consultar should enrich prompt with context")
    void testConsultarIAWithContextEnrichment() throws Exception {
        // Arrange
        when(deepseekService.consultarDeepseek(contains("contexto")))
                .thenReturn(mockDeepseekResponse);

        String requestBody = "{\"prompt\": \"¿Qué asignaturas hay?\"}";

        // Act & Assert
        mockMvc.perform(post("/api/ia/consultar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());

        // Verify que se llamó con un prompt enriquecido que contiene contexto
        verify(deepseekService, times(1)).consultarDeepseek(argThat(
                prompt -> prompt.contains("Sistema de gestión escolar") ||
                        prompt.contains("contexto") ||
                        prompt.contains("alumnos") ||
                        prompt.contains("asignaturas")));
    }

    @Test
    @DisplayName("POST /api/ia/consultar should handle API errors gracefully")
    void testConsultarIAApiError() throws Exception {
        // Arrange
        when(deepseekService.consultarDeepseek(anyString()))
                .thenThrow(new RuntimeException("Error al comunicarse con la API de Deepseek"));

        String requestBody = "{\"prompt\": \"¿Cuántos alumnos hay?\"}";

        // Act & Assert
        mockMvc.perform(post("/api/ia/consultar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isInternalServerError());

        verify(deepseekService, times(1)).consultarDeepseek(anyString());
    }

    @Test
    @DisplayName("POST /api/ia/consultar with long prompt should work correctly")
    void testConsultarIALongPrompt() throws Exception {
        // Arrange
        String longPrompt = "¿Cuántos alumnos hay en el sistema y cuáles son sus asignaturas?";
        when(deepseekService.consultarDeepseek(anyString()))
                .thenReturn(mockDeepseekResponse);

        String requestBody = "{\"prompt\": \"" + longPrompt + "\"}";

        // Act & Assert
        mockMvc.perform(post("/api/ia/consultar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prompt", is(longPrompt)));

        verify(deepseekService, times(1)).consultarDeepseek(anyString());
    }

    @Test
    @DisplayName("POST /api/ia/consultar should include token usage info")
    void testConsultarIATokenUsage() throws Exception {
        // Arrange
        when(deepseekService.consultarDeepseek(anyString()))
                .thenReturn(mockDeepseekResponse);

        String requestBody = "{\"prompt\": \"Test prompt\"}";

        // Act & Assert
        mockMvc.perform(post("/api/ia/consultar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tokens_utilizados", is(25)))
                .andExpect(jsonPath("$.modelo_ia", notNullValue()));

        verify(deepseekService, times(1)).consultarDeepseek(anyString());
    }

    @Test
    @DisplayName("POST /api/ia/consultar should handle missing API key gracefully")
    void testConsultarIAMissingApiKey() throws Exception {
        // Arrange
        when(deepseekService.consultarDeepseek(anyString()))
                .thenThrow(new RuntimeException("DEEPSEEK_API_KEY no está configurada"));

        String requestBody = "{\"prompt\": \"¿Cuántos alumnos hay?\"}";

        // Act & Assert
        mockMvc.perform(post("/api/ia/consultar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isInternalServerError());

        verify(deepseekService, times(1)).consultarDeepseek(anyString());
    }
}
