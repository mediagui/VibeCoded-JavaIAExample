# IAExample - Spring Boot Project con IA Local

Proyecto Spring Boot con base de datos en memoria H2 integrado con **Ollama** para ejecutar modelos de IA de forma local.

## Tecnologías

- **Spring Boot 3.3.0**
- **Java 21**
- **Maven**
- **H2 Database** (en memoria)
- **Spring Data JPA**
- **Ollama** (Modelos IA Locales)

## Dependencias Principales

- `spring-boot-starter-web`: Para crear APIs REST
- `spring-boot-starter-data-jpa`: Para acceso a datos con JPA
- `h2`: Base de datos en memoria

## Ejecución

```bash
mvn spring-boot:run
```

## Endpoints Disponibles

### Usuarios
- `GET /api/users` - Obtener todos los usuarios
- `GET /api/users/{id}` - Obtener usuario por ID
- `POST /api/users` - Crear nuevo usuario
- `PUT /api/users/{id}` - Actualizar usuario
- `DELETE /api/users/{id}` - Eliminar usuario

### Alumnos
- `GET /api/alumnos` - Obtener todos los alumnos
- `GET /api/alumnos/{id}` - Obtener alumno por ID
- `POST /api/alumnos` - Crear nuevo alumno
- `PUT /api/alumnos/{id}` - Actualizar alumno
- `DELETE /api/alumnos/{id}` - Eliminar alumno

### Asignaturas
- `GET /api/asignaturas` - Obtener todas las asignaturas
- `GET /api/asignaturas/{id}` - Obtener asignatura por ID
- `POST /api/asignaturas` - Crear nueva asignatura
- `PUT /api/asignaturas/{id}` - Actualizar asignatura
- `DELETE /api/asignaturas/{id}` - Eliminar asignatura

### IA (Ollama)
- `POST /api/ia/consultar` - Consultar al modelo IA con contexto de la base de datos

## Integración con IA Local (Ollama)

Este proyecto está integrado con **Ollama** para ejecutar modelos de IA localmente. Por defecto usa el modelo **qwen2**.

### Requisitos Previos

1. **Instalar Ollama**:
```bash
curl -fsSL https://ollama.com/install.sh | sh
```

2. **Descargar modelo**:
```bash
ollama pull qwen2
```

3. **Iniciar Ollama**:
```bash
ollama serve
```

### Documentación Completa

Ver [OLLAMA_SETUP.md](doc/OLLAMA_SETUP.md) para:
- Guía completa de instalación en Linux
- Modelos recomendados y comparativas
- Configuración del proyecto
- Troubleshooting
- Optimización de rendimiento

## Consola H2

La consola H2 está habilitada y disponible en:
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Usuario: `sa`
- Contraseña: (vacía)

## Ejemplo de Uso

Crear un usuario:
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Juan Pérez","email":"juan@example.com"}'
```

Obtener todos los usuarios:
```bash
curl http://localhost:8080/api/users
```

## Ejemplo de Consulta IA

Consultar al modelo IA sobre los datos:
```bash
curl -X POST http://localhost:8080/api/ia/consultar \
  -H "Content-Type: application/json" \
  -d '{
    "prompt": "¿Cuáles son las asignaturas disponibles?"
  }'
```

El sistema enriquece automáticamente el prompt con el contexto de la base de datos (alumnos y asignaturas) para que el modelo IA pueda responder con información actualizada.

## Documentación Adicional

- [OLLAMA_SETUP.md](doc/OLLAMA_SETUP.md) - Guía completa de Ollama
- [API_ENDPOINTS.md](doc/API_ENDPOINTS.md) - Documentación completa de endpoints
- [DATABASE_MODEL.md](doc/DATABASE_MODEL.md) - Modelo de base de datos
- [DOCKER.md](doc/DOCKER.md) - Instrucciones de Docker
- [TEST_SUMMARY.md](doc/TEST_SUMMARY.md) - Resumen completo de tests
