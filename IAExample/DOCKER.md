# Docker Deployment Guide

## Descripción

Esta guía explica cómo desplegar la aplicación Spring Boot en Docker.

## Requisitos

- Docker 20.10+
- Docker Compose 2.0+ (opcional, para usar docker-compose)

## Opción 1: Usar Docker Compose (Recomendado)

Es la forma más simple de ejecutar la aplicación.

### Pasos:

```bash
# Navegar al directorio del proyecto
cd /home/mediagui/Documents/Desarrollo/Java/IAExample

# Construir e iniciar la aplicación
docker-compose up -d

# Verificar que la aplicación está corriendo
docker-compose logs -f app

# Probar los endpoints
curl http://localhost:8080/api/alumnos

# Detener la aplicación
docker-compose down
```

## Opción 2: Usar Docker directamente

Si prefieres usar Docker sin Docker Compose.

### Pasos:

```bash
# Construir la imagen
docker build -t iaexample:latest .

# Ejecutar la imagen
docker run -d \
  --name iaexample-app \
  -p 8080:8080 \
  -e JAVA_OPTS="-Xmx512m -Xms256m" \
  iaexample:latest

# Ver logs
docker logs -f iaexample-app

# Detener el contenedor
docker stop iaexample-app

# Remover el contenedor
docker rm iaexample-app
```

## Características

- **Multi-stage build**: Usa Maven en el builder para compilar, y una imagen JRE alpine ligera para runtime
- **Health check**: Monitorea la salud de la aplicación cada 30 segundos
- **Optimización de memoria**: JAVA_OPTS configurado para limitar uso de memoria
- **Puerto expuesto**: 8080 para acceder a los endpoints

## Testing de Endpoints

Una vez que la aplicación está corriendo:

```bash
# Listar alumnos
curl http://localhost:8080/api/alumnos

# Obtener detalles de un alumno
curl http://localhost:8080/api/alumnos/1

# Matricular un alumno en una asignatura
curl -X POST http://localhost:8080/api/alumnos/1/asignaturas \
  -H "Content-Type: application/json" \
  -d '{"asignaturaId":2}'

# Registrar una nota
curl -X PUT http://localhost:8080/api/alumnos/1/asignaturas/2/nota \
  -H "Content-Type: application/json" \
  -d '{"nota":9}'
```

## Variables de Entorno

Puedes configurar las siguientes variables en `docker-compose.yml` o al ejecutar `docker run`:

- `JAVA_OPTS`: Opciones de la JVM (ej: `-Xmx512m -Xms256m`)

## Troubleshooting

### La aplicación no inicia
```bash
# Ver logs detallados
docker-compose logs app
```

### Puerto 8080 ya está en uso
Cambiar el puerto en `docker-compose.yml`:
```yaml
ports:
  - "9090:8080"  # Cambiar a 9090
```

### La aplicación consume mucha memoria
Ajustar `JAVA_OPTS` en `docker-compose.yml`:
```yaml
environment:
  - JAVA_OPTS=-Xmx1024m -Xms512m
```

## Base de Datos

La aplicación actualmente usa H2 (in-memory). El esquema y datos se crean automáticamente al iniciar.

Si en el futuro quieres usar PostgreSQL u otra BD externa:
1. Agrega un servicio en `docker-compose.yml`
2. Actualiza `application.properties` o `application.yml` con la conexión
3. Asegúrate que la app espere a que la BD esté lista antes de conectar
