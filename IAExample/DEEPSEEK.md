# Integración de Deepseek - Guía de Uso

## Overview

La aplicación ahora integra **Deepseek AI** para procesar consultas inteligentes sobre los datos del sistema escolar. El nuevo endpoint permite realizar preguntas en lenguaje natural que serán procesadas por la IA.

## Configuración

### 1. Obtener API Key de Deepseek

1. Acceder a <https://api.deepseek.com>
2. Crear una cuenta y obtener tu API Key
3. Configurar la clave en tu ambiente

### 2. Configuración de la Aplicación

#### Opción A: Variable de Entorno

```bash
export DEEPSEEK_API_KEY="tu_api_key_aqui"
```

#### Opción B: Docker Compose

Crear/Editar `.env`:

```
DEEPSEEK_API_KEY=tu_api_key_aqui
```

Luego en `docker-compose.yml` o `docker-compose.dev.yml`, asegurar que la variable esté configurada:

```yaml
environment:
  - DEEPSEEK_API_KEY=${DEEPSEEK_API_KEY}
```

#### Opción C: application.properties

```properties
deepseek.api.key=tu_api_key_aqui
```

## Nuevo Endpoint

### POST `/api/ia/consultar`

Recibe un prompt en lenguaje natural y devuelve una respuesta procesada por Deepseek.

#### Request

```json
{
  "prompt": "¿Cuántos alumnos hay en el sistema?"
}
```

#### Response (200 OK)

```json
{
  "prompt": "¿Cuántos alumnos hay en el sistema?",
  "respuesta": "Según la información del sistema, hay 3 alumnos registrados en total.",
  "tokens_utilizados": 145,
  "modelo_ia": "deepseek-chat"
}
```

#### Response (500 Error - API Key no configurada)

```
Internal Server Error
```

#### Response (400 Bad Request - Prompt vacío)

```
Bad Request
```

## Ejemplos de Uso

### Con cURL

```bash
# Consulta simple
curl -X POST http://localhost:8080/api/ia/consultar \
  -H "Content-Type: application/json" \
  -d '{"prompt":"¿Cuántas asignaturas hay en el sistema?"}'

# Consulta sobre alumnos
curl -X POST http://localhost:8080/api/ia/consultar \
  -H "Content-Type: application/json" \
  -d '{"prompt":"Dame información sobre los alumnos registrados"}'

# Consulta sobre notas
curl -X POST http://localhost:8080/api/ia/consultar \
  -H "Content-Type: application/json" \
  -d '{"prompt":"¿Cuál es el promedio de calificaciones del sistema?"}'
```

### Con JavaScript/Fetch

```javascript
const prompt = "¿Cuántos alumnos hay en el sistema?";

fetch('http://localhost:8080/api/ia/consultar', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({ prompt: prompt })
})
.then(response => response.json())
.then(data => {
  console.log('Respuesta de IA:', data.respuesta);
  console.log('Tokens usados:', data.tokens_utilizados);
})
.catch(error => console.error('Error:', error));
```

### Con Python/Requests

```python
import requests
import json

url = 'http://localhost:8080/api/ia/consultar'
headers = {'Content-Type': 'application/json'}
data = {'prompt': '¿Cuántos alumnos hay en el sistema?'}

response = requests.post(url, headers=headers, json=data)
resultado = response.json()

print(f"Respuesta: {resultado['respuesta']}")
print(f"Tokens: {resultado['tokens_utilizados']}")
```

## Contexto Enriquecido

El endpoint **enriquece automáticamente** el prompt del usuario con información sobre:

- Número total de alumnos en el sistema
- Número total de asignaturas disponibles
- Información de que se trata de un sistema de gestión escolar

Esto permite que Deepseek proporcione respuestas más contextualizadas.

## Endpoints Existentes (Mantienen su Funcionalidad)

- `GET /api/alumnos` - Listar todos los alumnos
- `GET /api/alumnos/{id}` - Obtener detalles de un alumno
- `GET /api/alumnos/{alumnoId}/asignaturas/{asignaturaId}` - Ver nota de alumno en asignatura
- `GET /api/asignaturas` - Listar asignaturas
- `GET /api/asignaturas/{id}` - Obtener detalles de asignatura
- `GET /api/asignaturas/{id}/alumnos` - Listar alumnos en una asignatura
- `POST /api/alumnos/{alumnoId}/asignaturas` - Matricular alumno
- `PUT /api/alumnos/{alumnoId}/asignaturas/{asignaturaId}/nota` - Registrar/actualizar nota

## Modelo de IA

La aplicación está configurada para usar **deepseek-chat** por defecto.

Parámetros configurables en `application.properties`:

- `deepseek.api.model` - Modelo a usar (defecto: deepseek-chat)
- `deepseek.api.temperature` - Control creativo (0-1, defecto: 0.7)
- `deepseek.api.max-tokens` - Máximo de tokens en respuesta (defecto: 2000)
- `deepseek.api.timeout` - Timeout en ms (defecto: 30000)

## Manejo de Errores

### API Key no configurada

- **Código**: 500
- **Mensaje**: `Internal Server Error`
- **Solución**: Configurar variable de entorno `DEEPSEEK_API_KEY`

### Prompt vacío

- **Código**: 400
- **Mensaje**: Bad Request
- **Solución**: Enviar prompt no vacío

### Conexión fallida con Deepseek

- **Código**: 500
- **Mensaje**: `RuntimeException: Error al comunicarse con la API de Deepseek`
- **Solución**: Verificar conectividad, API Key, y cuotas de uso

### Rate Limit

- **Código**: 429 (si Deepseek lo retorna)
- **Solución**: Esperar e intentar de nuevo

## Monitorización

Para ver los logs de la aplicación:

```bash
# Si está corriendo con Docker Compose
docker-compose logs -f app

# Si está corriendo localmente
mvn spring-boot:run
```

Buscar líneas que contengan `DeepseekService` o `IAController` en los logs.

## Costos y Cuotas

- Verificar saldo y uso en dashboard de Deepseek API
- Cada consulta consume tokens (prompt_tokens + completion_tokens)
- La respuesta incluye `tokens_utilizados` para seguimiento

## Troubleshooting

### "DEEPSEEK_API_KEY no está configurada"

- Verificar que la variable de entorno está set: `echo $DEEPSEEK_API_KEY`
- Si no existe, exportar: `export DEEPSEEK_API_KEY="tu_clave"`

### "Error al comunicarse con la API de Deepseek"

- Verificar conexión a internet
- Verificar que la API Key es válida
- Comprobar que Deepseek está en funcionamiento (<https://status.deepseek.com>)

### Respuestas muy cortas o incompletas

- Aumentar `deepseek.api.max-tokens` en application.properties
- Ajustar `deepseek.api.temperature` para mayor determinismo (0.1-0.3)

## Seguridad

⚠️ **Importante**: Nunca comitear la API Key en el repositorio

- Usar variables de entorno
- Usar archivos `.env` locales (agregados a `.gitignore`)
- Para producción, usar un secrets manager (AWS Secrets Manager, HashiCorp Vault, etc.)

Estructura segura en `docker-compose.yml`:

```yaml
environment:
  - DEEPSEEK_API_KEY=${DEEPSEEK_API_KEY}  # Viene de .env local, no del repo
```
