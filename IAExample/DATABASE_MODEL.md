# Modelo de Base de Datos

## Esquema de Relaciones

```
┌─────────────┐          ┌──────────────────┐          ┌─────────────┐
│   USUARIO   │          │ USUARIO_PERFIL   │          │   PERFIL    │
├─────────────┤          ├──────────────────┤          ├─────────────┤
│ id (PK)     │◄────────┤│ id (PK)          │├────────►│ id (PK)     │
│ nombre      │   1:N    │ usuario_id (FK)  │   N:1   │ nombre      │
│ email       │          │ perfil_id (FK)   │          │ descripcion │
└─────────────┘          │ fecha_asignacion │          └─────────────┘
                         └──────────────────┘
                                  │ 1:1
                                  ▼
                         ┌──────────────────┐
                         │    CREDITOS      │
                         ├──────────────────┤
                         │ id (PK)          │
                         │ usuario_perfil_id│
                         │ cantidad_creditos│
                         │ fecha_actualiz.. │
                         └──────────────────┘
```

## Tablas

### USUARIO
Almacena la información de los usuarios del sistema.
- `id`: Identificador único
- `nombre`: Nombre del usuario
- `email`: Email único del usuario

### PERFIL
Define los diferentes perfiles o roles disponibles en el sistema.
- `id`: Identificador único
- `nombre`: Nombre único del perfil (ej: Administrador, Usuario, Supervisor)
- `descripcion`: Descripción del perfil

### USUARIO_PERFIL
Tabla intermedia que relaciona usuarios con perfiles (relación muchos a muchos).
Un usuario puede tener múltiples perfiles asignados.
- `id`: Identificador único
- `usuario_id`: Referencia al usuario
- `perfil_id`: Referencia al perfil
- `fecha_asignacion`: Fecha en que se asignó el perfil al usuario
- Restricción única: Un usuario no puede tener el mismo perfil duplicado

### CREDITOS
Almacena la cantidad de créditos asignados a cada par usuario-perfil.
Cada combinación usuario-perfil tiene su propia asignación de créditos.
- `id`: Identificador único
- `usuario_perfil_id`: Referencia única a la relación usuario-perfil
- `cantidad_creditos`: Número de créditos disponibles
- `fecha_actualizacion`: Última fecha de modificación de créditos

## Lógica de Negocio

1. **Asignación de Perfiles**: Un usuario puede tener uno o más perfiles asociados
2. **Créditos por Contexto**: Los créditos se asignan específicamente a cada combinación usuario-perfil, no al usuario en general
3. **Gestión Independiente**: Cada par usuario-perfil tiene su propia cantidad de créditos que puede ser gestionada independientemente

## Ejemplo de Uso

Usuario "Juan" (id=1) tiene dos perfiles:
- Perfil "Administrador" (id=1) con 1000 créditos
- Perfil "Usuario" (id=2) con 500 créditos

Esto permite que Juan tenga diferentes cantidades de créditos según el contexto o rol con el que esté operando.

## Datos de Ejemplo

La base de datos se inicializa con:
- 3 usuarios
- 3 perfiles
- 4 asignaciones usuario-perfil
- 4 registros de créditos
