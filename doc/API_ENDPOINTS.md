# API REST - Endpoints Disponibles

## Usuarios

### Obtener todos los usuarios
```http
GET /api/users
```
Retorna una lista simple de todos los usuarios con id, nombre y email.

**Respuesta:**
```json
[
  {
    "id": 1,
    "nombre": "Juan Pérez",
    "email": "juan@example.com"
  }
]
```

### Obtener datos detallados de un usuario
```http
GET /api/users/{id}
```
Retorna información completa del usuario incluyendo todos sus perfiles y créditos asociados.

**Ejemplo:** `GET /api/users/1`

**Respuesta:**
```json
{
  "id": 1,
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "perfiles": [
    {
      "perfilId": 1,
      "perfilNombre": "Administrador",
      "perfilDescripcion": "Acceso completo al sistema",
      "cantidadCreditos": 1000
    },
    {
      "perfilId": 2,
      "perfilNombre": "Usuario",
      "perfilDescripcion": "Acceso básico al sistema",
      "cantidadCreditos": 500
    }
  ]
}
```

### Obtener usuarios por cantidad de créditos
```http
GET /api/users/creditos?cantidad={cantidad}
```
Retorna usuarios que tienen exactamente la cantidad especificada de créditos en alguno de sus perfiles.

**Ejemplo:** `GET /api/users/creditos?cantidad=1000`

**Respuesta:**
```json
[
  {
    "id": 1,
    "nombre": "Juan Pérez",
    "email": "juan@example.com"
  }
]
```

## Perfiles

### Obtener todos los perfiles
```http
GET /api/perfiles
```
Retorna una lista de todos los perfiles disponibles.

**Respuesta:**
```json
[
  {
    "id": 1,
    "nombre": "Administrador",
    "descripcion": "Acceso completo al sistema"
  },
  {
    "id": 2,
    "nombre": "Usuario",
    "descripcion": "Acceso básico al sistema"
  }
]
```

### Obtener un perfil específico
```http
GET /api/perfiles/{id}
```
Retorna la información de un perfil específico.

**Ejemplo:** `GET /api/perfiles/1`

### Obtener usuarios asignados a un perfil
```http
GET /api/perfiles/{id}/usuarios
```
Retorna todos los usuarios que tienen asignado el perfil especificado.

**Ejemplo:** `GET /api/perfiles/1/usuarios`

**Respuesta:**
```json
[
  {
    "id": 1,
    "nombre": "Juan Pérez",
    "email": "juan@example.com"
  }
]
```

## Créditos

### Obtener información detallada de usuarios con cantidad de créditos
```http
GET /api/creditos?cantidad={cantidad}
```
Retorna información completa de usuarios y perfiles que tienen exactamente la cantidad especificada de créditos.

**Ejemplo:** `GET /api/creditos?cantidad=1000`

**Respuesta:**
```json
[
  {
    "userId": 1,
    "userNombre": "Juan Pérez",
    "userEmail": "juan@example.com",
    "perfilId": 1,
    "perfilNombre": "Administrador",
    "cantidadCreditos": 1000
  }
]
```

## Ejemplos de Uso con curl

```bash
# Obtener todos los usuarios
curl http://localhost:8080/api/users

# Obtener datos de un usuario específico
curl http://localhost:8080/api/users/1

# Obtener usuarios con 1000 créditos
curl http://localhost:8080/api/users/creditos?cantidad=1000

# Obtener todos los perfiles
curl http://localhost:8080/api/perfiles

# Obtener usuarios del perfil Administrador (id=1)
curl http://localhost:8080/api/perfiles/1/usuarios

# Obtener información detallada de usuarios con 750 créditos
curl http://localhost:8080/api/creditos?cantidad=750
```
