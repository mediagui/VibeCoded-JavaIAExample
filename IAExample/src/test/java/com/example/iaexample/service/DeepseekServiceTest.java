package com.example.iaexample.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.example.iaexample.config.DeepseekProperties;
import com.example.iaexample.dto.deepseek.DeepseekChoice;
import com.example.iaexample.dto.deepseek.DeepseekMessage;
import com.example.iaexample.dto.deepseek.DeepseekResponse;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("DeepseekService Unit Tests")
class DeepseekServiceTest {

    @Mock
    private DeepseekProperties properties;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    @Mock
    private RestTemplate restTemplate;

    private DeepseekService deepseekService;

    private DeepseekResponse mockResponse;

    @BeforeEach
    void setUp() {
        // Configurar RestTemplateBuilder para devolver el mock de RestTemplate
        when(restTemplateBuilder.build()).thenReturn(restTemplate);

        // Crear instancia del servicio con mocks
        deepseekService = new DeepseekService(properties, restTemplateBuilder);

        // Configurar propiedades
        when(properties.getUrl()).thenReturn("https://api.deepseek.com/chat/completions");
        when(properties.getKey()).thenReturn("test-api-key");
        when(properties.getModel()).thenReturn("deepseek-chat");
        when(properties.getTemperature()).thenReturn(0.7);
        when(properties.getMaxTokens()).thenReturn(2000);

        // Crear respuesta mock
        mockResponse = new DeepseekResponse();
        mockResponse.setId("mock-id-123");
        mockResponse.setModel("deepseek-chat");

        DeepseekChoice choice = new DeepseekChoice();
        DeepseekMessage message = new DeepseekMessage();
        message.setRole("assistant");
        message.setContent("Test response from Deepseek");
        choice.setMessage(message);
        choice.setIndex(0);

        mockResponse.setChoices(java.util.Arrays.asList(choice));

        DeepseekResponse.Usage usage = new DeepseekResponse.Usage();
        usage.setPromptTokens(10);
        usage.setCompletionTokens(20);
        usage.setTotalTokens(30);
        mockResponse.setUsage(usage);
    }

    @Test
    @DisplayName("Should successfully call Deepseek API")
    void testConsultarDeepseek() {
        // Arrange
        when(restTemplate.postForEntity(anyString(), any(), eq(DeepseekResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // Act
        DeepseekResponse result = deepseekService.consultarDeepseek("Test prompt");

        // Assert
        assertNotNull(result);
        assertEquals("deepseek-chat", result.getModel());
        assertEquals(1, result.getChoices().size());
        assertEquals(30, result.getUsage().getTotalTokens());
        verify(restTemplate, times(1)).postForEntity(anyString(), any(), eq(DeepseekResponse.class));
    }

    @Test
    @DisplayName("Should throw exception when API key is not configured")
    void testConsultarDeepseekNoApiKey() {
        // Arrange
        when(properties.getKey()).thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deepseekService.consultarDeepseek("Test prompt");
        });
        assertTrue(exception.getMessage().contains("DEEPSEEK_API_KEY no está configurada"));
    }

    @Test
    @DisplayName("Should throw exception when API returns error")
    void testConsultarDeepseekApiError() {
        // Arrange
        when(restTemplate.postForEntity(anyString(), any(), eq(DeepseekResponse.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deepseekService.consultarDeepseek("Test prompt");
        });
        assertTrue(exception.getMessage().contains("Error al consultar Deepseek"));
    }

    @Test
    @DisplayName("Should extract response content correctly")
    void testExtraerRespuesta() {
        // Act
        String result = deepseekService.extraerRespuesta(mockResponse);

        // Assert
        assertNotNull(result);
        assertEquals("Test response from Deepseek", result);
    }

    @Test
    @DisplayName("Should throw exception when choices are empty")
    void testExtraerRespuestaEmpty() {
        // Arrange
        mockResponse.setChoices(java.util.Arrays.asList());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deepseekService.extraerRespuesta(mockResponse);
        });
        assertTrue(exception.getMessage().contains("No se obtuvo respuesta válida"));
    }

    @Test
    @DisplayName("Should throw exception when choices are null")
    void testExtraerRespuestaNull() {
        // Arrange
        mockResponse.setChoices(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deepseekService.extraerRespuesta(mockResponse);
        });
        assertTrue(exception.getMessage().contains("No se obtuvo respuesta válida"));
    }
}
