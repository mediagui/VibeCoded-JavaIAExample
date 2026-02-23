package com.example.iaexample.controller;

import java.util.List;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.iaexample.dto.AlumnoDTO;
import com.example.iaexample.dto.AsignaturaDTO;
import com.example.iaexample.dto.ConsultaIAResponse;
import com.example.iaexample.dto.PromptRequest;
import com.example.iaexample.service.AlumnoService;
import com.example.iaexample.service.AsignaturaService;
import com.example.iaexample.service.OllamaService;

@RestController
@RequestMapping("/api/ia")
public class IAController {
    private final OllamaService ollamaService;
    private final AlumnoService alumnoService;
    private final AsignaturaService asignaturaService;

    public IAController(
            OllamaService ollamaService,
            AlumnoService alumnoService,
            AsignaturaService asignaturaService) {
        this.ollamaService = ollamaService;
        this.alumnoService = alumnoService;
        this.asignaturaService = asignaturaService;
    }

    @PostMapping(value = "/consultar", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConsultaIAResponse> consultar(@RequestBody PromptRequest request) {
        try {
            if (request.getPrompt() == null || request.getPrompt().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            // Enriquecer el prompt con contexto de la base de datos
            String promptEnriquecido = enriquecerPrompt(request.getPrompt());

            // Consultar Ollama usando Spring AI
            ChatResponse chatResponse = ollamaService.consultarOllamaCompleto(promptEnriquecido);

            // Extraer la respuesta
            String respuesta = chatResponse.getResult().getOutput().getContent();

            // Obtener metadata
            String modelo = chatResponse.getMetadata().getModel();
            Integer tokensUtilizados = chatResponse.getMetadata().getUsage() != null
                    ? chatResponse.getMetadata().getUsage().getTotalTokens().intValue()
                    : 0;

            // Construir la respuesta estructurada
            ConsultaIAResponse response = new ConsultaIAResponse(
                    request.getPrompt(),
                    respuesta,
                    tokensUtilizados,
                    modelo);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String enriquecerPrompt(String prompt) {
        StringBuilder contexto = new StringBuilder();

        // Instrucciones para la IA
        contexto.append("Eres un asistente de un sistema de gestión escolar. ");
        contexto.append("Responde de forma precisa y concisa usando los datos proporcionados.\n\n");

        // Obtener datos de alumnos
        List<AlumnoDTO> alumnos = alumnoService.getAllAlumnos();
        contexto.append("=== ALUMNOS REGISTRADOS ===\n");
        if (alumnos.isEmpty()) {
            contexto.append("No hay alumnos registrados.\n");
        } else {
            contexto.append("Total: ").append(alumnos.size()).append(" alumnos\n");
            for (AlumnoDTO alumno : alumnos) {
                contexto.append("- ID: ").append(alumno.getId())
                        .append(", Nombre: ").append(alumno.getNombre())
                        .append(", Email: ").append(alumno.getEmail())
                        .append("\n");
            }
        }

        // Obtener datos de asignaturas
        List<AsignaturaDTO> asignaturas = asignaturaService.getAllAsignaturas();
        contexto.append("\n=== ASIGNATURAS DISPONIBLES ===\n");
        if (asignaturas.isEmpty()) {
            contexto.append("No hay asignaturas disponibles.\n");
        } else {
            contexto.append("Total: ").append(asignaturas.size()).append(" asignaturas\n");
            for (AsignaturaDTO asignatura : asignaturas) {
                contexto.append("- ID: ").append(asignatura.getId())
                        .append(", Nombre: ").append(asignatura.getNombre())
                        .append(", Descripción: ").append(asignatura.getDescripcion())
                        .append("\n");
            }
        }

        // Información sobre las capacidades del sistema
        contexto.append("\n=== FUNCIONALIDADES DEL SISTEMA ===\n");
        contexto.append("- Gestión de alumnos (alta, consulta)\n");
        contexto.append("- Gestión de asignaturas\n");
        contexto.append("- Matriculación de alumnos en asignaturas\n");
        contexto.append("- Registro y consulta de notas\n");
        contexto.append("- Generación de boletines\n");

        // Pregunta del usuario
        contexto.append("\n=== PREGUNTA DEL USUARIO ===\n");
        contexto.append(prompt).append("\n");

        return contexto.toString();
    }
}
