# Integración de Deepseek - Quick Start

## ⚡ Inicio Rápido (5 minutos)

### 1. Obtener API Key
- Ir a https://api.deepseek.com
- Crear cuenta y obtener API Key
- Copiar la clave

### 2. Configurar en Local

```bash
# En tu terminal, exportar la variable
export DEEPSEEK_API_KEY="sk-xxx..."

# Verificar que está set
echo $DEEPSEEK_API_KEY
```

### 3. Iniciar la Aplicación

```bash
cd /home/mediagui/Documents/Desarrollo/Java/IAExample

# Opción A: Con Maven
mvn spring-boot:run

# Opción B: Con Docker Compose
docker-compose up -d
```

### 4. Probar el Endpoint

```bash
curl -X POST http://localhost:8080/api/ia/consultar \
  -H "Content-Type: application/json" \
  -d '{"prompt":"¿Cuántos alumnos hay?"}'
```

✅ Respuesta esperada:
```json
{
  "prompt": "¿Cuántos alumnos hay?",
  "respuesta": "Basándome en el contexto proporcionado, hay 3 alumnos registrados en el sistema.",
  "tokens_utilizados": 87,
  "modelo_ia": "deepseek-chat"
}
```

---

## 📋 Resumen de Cambios

### Archivos Nuevos Creados

1. **DTOs para Deepseek**
   - `DeepseekMessage.java` - Mensaje de chat
   - `DeepseekRequest.java` - Solicitud a API
   - `DeepseekChoice.java` - Opción de respuesta
   - `DeepseekResponse.java` - Respuesta de la API
   - `PromptRequest.java` - DTO para recibir prompts
   - `ConsultaIAResponse.java` - DTO para respuesta estructurada

2. **Servicio IA**
   - `DeepseekService.java` - Lógica de integración con Deepseek
   - `DeepseekProperties.java` - Configuración desde properties

3. **Controlador**
   - `IAController.java` - Endpoint POST `/api/ia/consultar`

4. **Documentación**
   - `DEEPSEEK.md` - Guía completa
   - `sh/test-deepseek.sh` - Script de pruebas

5. **Configuración**
   - Actualizado `application.properties` con configuración de Deepseek
   - Actualizado `.env.example` con DEEPSEEK_API_KEY

### Archivos Modificados

- `application.properties` - Agregadas propiedades de Deepseek

---

## 🔌 Nuevo Endpoint

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/ia/consultar` | Procesar consulta con Deepseek |

### Request Body
```json
{
  "prompt": "Tu pregunta aquí"
}
```

### Response (200)
```json
{
  "prompt": "Tu pregunta aquí",
  "respuesta": "Respuesta de Deepseek",
  "tokens_utilizados": 123,
  "modelo_ia": "deepseek-chat"
}
```

---

## 🧪 Ejemplos de Consultas

```bash
# 1. Información de alumnos
curl -X POST http://localhost:8080/api/ia/consultar \
  -H "Content-Type: application/json" \
  -d '{"prompt":"Cuéntame sobre los alumnos del sistema"}'

# 2. Información de asignaturas
curl -X POST http://localhost:8080/api/ia/consultar \
  -H "Content-Type: application/json" \
  -d '{"prompt":"¿Qué asignaturas tenemos disponibles?"}'

# 3. Análisis de datos
curl -X POST http://localhost:8080/api/ia/consultar \
  -H "Content-Type: application/json" \
  -d '{"prompt":"Dame estadísticas del sistema escolar"}'

# 4. Información general
curl -X POST http://localhost:8080/api/ia/consultar \
  -H "Content-Type: application/json" \
  -d '{"prompt":"Resumen de todo el sistema"}'
```

---

## 🛠️ Configuración Avanzada

### Cambiar Modelo
En `application.properties`:
```properties
deepseek.api.model=deepseek-chat
```

### Ajustar Temperatura (Creatividad)
```properties
deepseek.api.temperature=0.7  # Rango: 0.1 (determinista) a 1.0 (creativo)
```

### Cambiar Límite de Tokens
```properties
deepseek.api.max-tokens=2000  # Máximo de tokens en respuesta
```

### Timeout
```properties
deepseek.api.timeout=30000  # Milisegundos
```

---

## ✅ Endpoints Existentes (Intactos)

Todos los endpoints anteriores siguen funcionando:

- `GET /api/alumnos` ✓
- `GET /api/alumnos/{id}` ✓
- `GET /api/alumnos/{alumnoId}/asignaturas/{asignaturaId}` ✓
- `GET /api/asignaturas` ✓
- `GET /api/asignaturas/{id}` ✓
- `GET /api/asignaturas/{id}/alumnos` ✓
- `POST /api/alumnos/{alumnoId}/asignaturas` ✓
- `PUT /api/alumnos/{alumnoId}/asignaturas/{asignaturaId}/nota` ✓

---

## 🚨 Troubleshooting

### Error: "DEEPSEEK_API_KEY no está configurada"
```bash
# Solución: Exportar la variable
export DEEPSEEK_API_KEY="tu_clave_aqui"

# Verificar
echo $DEEPSEEK_API_KEY
```

### Error: "Error al comunicarse con la API de Deepseek"
- Verificar conexión a internet
- Verificar que la API Key es válida
- Revisar cuotas en https://api.deepseek.com

### Respuesta vacía
- Aumentar `max-tokens` en properties
- Verificar que el modelo es correcto

---

## 📊 Contexto Automático

El endpoint automáticamente:
- Cuenta alumnos en el sistema
- Cuenta asignaturas disponibles
- Enriquece el prompt con contexto de escuela

Esto permite que Deepseek proporcione respuestas más precisas.

---

## 🔒 Seguridad

⚠️ **Nunca** comitear API Keys en el repositorio

Usar:
- Variables de entorno ✓
- Archivos `.env` (agregados a `.gitignore`) ✓
- Secrets managers en producción ✓

---

## 📚 Documentación Completa

Ver `DEEPSEEK.md` para:
- Ejemplos detallados
- Uso con Postman, Python, JavaScript
- Manejo de errores
- Monitorización
- Costos y cuotas

---

## ✨ Compilación y Testing

```bash
# Compilar
mvn clean compile

# Correr con Maven
mvn spring-boot:run

# Correr el script de tests
bash sh/test-deepseek.sh
```

---

**¡Listo!** 🎉
La aplicación ahora tiene integrada la IA de Deepseek para consultas inteligentes.
