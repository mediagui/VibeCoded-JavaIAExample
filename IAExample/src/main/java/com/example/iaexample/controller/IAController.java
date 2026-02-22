package com.example.iaexample.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.iaexample.dto.ConsultaIAResponse;
import com.example.iaexample.dto.PromptRequest;
import com.example.iaexample.dto.deepseek.DeepseekResponse;
import com.example.iaexample.service.AlumnoService;
import com.example.iaexample.service.AsignaturaService;
import com.example.iaexample.service.DeepseekService;

@RestController
@RequestMapping("/api/ia")
public class IAController {
    private final DeepseekService deepseekService;
    private final AlumnoService alumnoService;
    private final AsignaturaService asignaturaService;

    public IAController(
            DeepseekService deepseekService,
            AlumnoService alumnoService,
            AsignaturaService asignaturaService) {
        this.deepseekService = deepseekService;
        this.alumnoService = alumnoService;
        this.asignaturaService = asignaturaService;
    }

    @PostMapping(value = "/consultar", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ConsultaIAResponse> consultar(@RequestBody PromptRequest request) {
        try {
            if (request.getPrompt() == null || request.getPrompt().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            // Enriquecer el prompt con contexto de la información disponible
            String promptEnriquecido = enriquecerPrompt(request.getPrompt());

            // Consultar Deepseek
            DeepseekResponse deepseekResponse = deepseekService.consultarDeepseek(promptEnriquecido);

            // Extraer la respuesta
            String respuesta = deepseekService.extraerRespuesta(deepseekResponse);

            // Construir la respuesta estructurada
            ConsultaIAResponse response = new ConsultaIAResponse(
                    request.getPrompt(),
                    respuesta,
                    deepseekResponse.getUsage().getTotalTokens(),
                    deepseekResponse.getModel());

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String enriquecerPrompt(String prompt) {
        StringBuilder promptEnriquecido = new StringBuilder();
        promptEnriquecido.append(prompt).append("\n\n");
        promptEnriquecido.append("Contexto de la aplicación:\n");
        promptEnriquecido.append("- Sistema de gestión escolar con alumnos, asignaturas y notas.\n");
        promptEnriquecido.append("- Alumnos en el sistema: ")
                .append(alumnoService.getAllAlumnos().size()).append("\n");
        promptEnriquecido.append("- Asignaturas disponibles: ")
                .append(asignaturaService.getAllAsignaturas().size()).append("\n");
        promptEnriquecido.append("Proporciona respuestas estructuradas y útiles basadas en este contexto.");

        return promptEnriquecido.toString();
    }
}
