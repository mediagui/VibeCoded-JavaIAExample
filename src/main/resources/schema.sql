-- Tabla Alumno
CREATE TABLE IF NOT EXISTS
    alumno (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        nombre VARCHAR (100) NOT NULL,
        email VARCHAR (100) NOT NULL UNIQUE
    );

-- Tabla Asignatura
CREATE TABLE IF NOT EXISTS
    asignatura (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        nombre VARCHAR (80) NOT NULL UNIQUE,
        descripcion VARCHAR (255)
    );

-- Tabla intermedia Alumno-Asignatura (matricula)
CREATE TABLE IF NOT EXISTS
    alumno_asignatura (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        alumno_id BIGINT NOT NULL,
        asignatura_id BIGINT NOT NULL,
        fecha_matricula TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY (alumno_id) REFERENCES alumno (id) ON DELETE CASCADE,
        FOREIGN KEY (asignatura_id) REFERENCES asignatura (id) ON DELETE CASCADE,
        CONSTRAINT unique_alumno_asignatura UNIQUE (alumno_id, asignatura_id)
    );

-- Tabla Nota asociada a cada par alumno-asignatura
CREATE TABLE IF NOT EXISTS
    nota (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        alumno_asignatura_id BIGINT NOT NULL UNIQUE,
        calificacion INT DEFAULT 0,
        fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY (alumno_asignatura_id) REFERENCES alumno_asignatura (id) ON DELETE CASCADE
    );
