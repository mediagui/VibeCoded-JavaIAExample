-- Insertar alumnos de ejemplo
INSERT INTO
    alumno (nombre, email)
VALUES
    ('Juan Pérez', 'juan@example.com');

INSERT INTO
    alumno (nombre, email)
VALUES
    ('María González', 'maria@example.com');

INSERT INTO
    alumno (nombre, email)
VALUES
    ('Carlos Rodríguez', 'carlos@example.com');

-- Insertar asignaturas de ejemplo
INSERT INTO
    asignatura (nombre, descripcion)
VALUES
    (
        'Matemáticas',
        'Álgebra, geometría y cálculo básico'
    );

INSERT INTO
    asignatura (nombre, descripcion)
VALUES
    ('Lengua', 'Comprensión lectora y escritura');

INSERT INTO
    asignatura (nombre, descripcion)
VALUES
    ('Historia', 'Historia universal y local');

-- Matricular alumnos en asignaturas
INSERT INTO
    alumno_asignatura (alumno_id, asignatura_id)
VALUES
    (1, 1);

INSERT INTO
    alumno_asignatura (alumno_id, asignatura_id)
VALUES
    (1, 2);

INSERT INTO
    alumno_asignatura (alumno_id, asignatura_id)
VALUES
    (2, 2);

INSERT INTO
    alumno_asignatura (alumno_id, asignatura_id)
VALUES
    (3, 3);

-- Asignar notas a cada matricula
INSERT INTO
    nota (alumno_asignatura_id, calificacion)
VALUES
    (1, 9);

INSERT INTO
    nota (alumno_asignatura_id, calificacion)
VALUES
    (2, 8);

INSERT INTO
    nota (alumno_asignatura_id, calificacion)
VALUES
    (3, 7);

INSERT INTO
    nota (alumno_asignatura_id, calificacion)
VALUES
    (4, 10);
