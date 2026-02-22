# IAExample - Spring Boot Project

Proyecto Spring Boot con base de datos en memoria H2.

## Tecnologías

- **Spring Boot 3.3.0**
- **Java 21**
- **Maven**
- **H2 Database** (en memoria)
- **Spring Data JPA**

## Dependencias Principales

- `spring-boot-starter-web`: Para crear APIs REST
- `spring-boot-starter-data-jpa`: Para acceso a datos con JPA
- `h2`: Base de datos en memoria

## Ejecución

```bash
mvn spring-boot:run
```

## Endpoints Disponibles

- `GET /api/users` - Obtener todos los usuarios
- `GET /api/users/{id}` - Obtener usuario por ID
- `POST /api/users` - Crear nuevo usuario
- `PUT /api/users/{id}` - Actualizar usuario
- `DELETE /api/users/{id}` - Eliminar usuario

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
