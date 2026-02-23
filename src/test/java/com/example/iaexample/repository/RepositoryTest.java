package com.example.iaexample.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.iaexample.entity.Alumno;
import com.example.iaexample.entity.AlumnoAsignatura;
import com.example.iaexample.entity.Asignatura;

@DataJpaTest
@DisplayName("Repository Integration Tests")
class RepositoryTest {

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    @Autowired
    private AlumnoAsignaturaRepository alumnoAsignaturaRepository;

    private Alumno alumno;
    private Asignatura asignatura;

    @BeforeEach
    void setUp() {
        alumno = new Alumno();
        alumno.setNombre("Test Alumno");
        alumno.setEmail("test@example.com");

        asignatura = new Asignatura();
        asignatura.setNombre("Test Asignatura");
        asignatura.setDescripcion("Test Description");
    }

    @Test
    @DisplayName("Should save and retrieve alumno")
    void testSaveAndRetrieveAlumno() {
        // Arrange & Act
        Alumno savedAlumno = alumnoRepository.save(alumno);

        // Assert
        assertNotNull(savedAlumno.getId());
        Optional<Alumno> retrievedAlumno = alumnoRepository.findById(savedAlumno.getId());
        assertTrue(retrievedAlumno.isPresent());
        assertEquals("Test Alumno", retrievedAlumno.get().getNombre());
        assertEquals("test@example.com", retrievedAlumno.get().getEmail());
    }

    @Test
    @DisplayName("Should save and retrieve asignatura")
    void testSaveAndRetrieveAsignatura() {
        // Arrange & Act
        Asignatura savedAsignatura = asignaturaRepository.save(asignatura);

        // Assert
        assertNotNull(savedAsignatura.getId());
        Optional<Asignatura> retrievedAsignatura = asignaturaRepository.findById(savedAsignatura.getId());
        assertTrue(retrievedAsignatura.isPresent());
        assertEquals("Test Asignatura", retrievedAsignatura.get().getNombre());
    }

    @Test
    @DisplayName("Should retrieve all alumnos")
    void testFindAllAlumnos() {
        // Arrange
        alumnoRepository.save(alumno);
        Alumno alumno2 = new Alumno();
        alumno2.setNombre("Test Alumno 2");
        alumno2.setEmail("test2@example.com");
        alumnoRepository.save(alumno2);

        // Act
        List<Alumno> alumnos = alumnoRepository.findAll();

        // Assert
        assertNotNull(alumnos);
        assertTrue(alumnos.size() >= 2);
    }

    @Test
    @DisplayName("Should retrieve all asignaturas")
    void testFindAllAsignaturas() {
        // Arrange
        asignaturaRepository.save(asignatura);
        Asignatura asignatura2 = new Asignatura();
        asignatura2.setNombre("Test Asignatura 2");
        asignatura2.setDescripcion("Test Description 2");
        asignaturaRepository.save(asignatura2);

        // Act
        List<Asignatura> asignaturas = asignaturaRepository.findAll();

        // Assert
        assertNotNull(asignaturas);
        assertTrue(asignaturas.size() >= 2);
    }

    @Test
    @DisplayName("Should update alumno")
    void testUpdateAlumno() {
        // Arrange
        Alumno savedAlumno = alumnoRepository.save(alumno);
        Long id = savedAlumno.getId();

        // Act
        savedAlumno.setNombre("Updated Name");
        savedAlumno.setEmail("updated@example.com");
        alumnoRepository.save(savedAlumno);

        // Assert
        Optional<Alumno> updatedAlumno = alumnoRepository.findById(id);
        assertTrue(updatedAlumno.isPresent());
        assertEquals("Updated Name", updatedAlumno.get().getNombre());
        assertEquals("updated@example.com", updatedAlumno.get().getEmail());
    }

    @Test
    @DisplayName("Should delete alumno")
    void testDeleteAlumno() {
        // Arrange
        Alumno savedAlumno = alumnoRepository.save(alumno);
        Long id = savedAlumno.getId();

        // Act
        alumnoRepository.delete(savedAlumno);

        // Assert
        Optional<Alumno> deletedAlumno = alumnoRepository.findById(id);
        assertFalse(deletedAlumno.isPresent());
    }

    @Test
    @DisplayName("Should find alumnos by asignatura")
    void testFindByAsignaturaId() {
        // Arrange
        Alumno savedAlumno = alumnoRepository.save(alumno);
        Asignatura savedAsignatura = asignaturaRepository.save(asignatura);

        AlumnoAsignatura alumnoAsignatura = new AlumnoAsignatura();
        alumnoAsignatura.setAlumno(savedAlumno);
        alumnoAsignatura.setAsignatura(savedAsignatura);
        alumnoAsignaturaRepository.save(alumnoAsignatura);

        // Act
        List<Alumno> alumnos = alumnoRepository.findByAsignaturaId(savedAsignatura.getId());

        // Assert
        assertNotNull(alumnos);
        assertTrue(alumnos.size() >= 1);
        assertTrue(alumnos.stream().anyMatch(a -> a.getId().equals(savedAlumno.getId())));
    }

    @Test
    @DisplayName("Should find alumno-asignatura relationship")
    void testFindAlumnoAsignaturaRelationship() {
        // Arrange
        Alumno savedAlumno = alumnoRepository.save(alumno);
        Asignatura savedAsignatura = asignaturaRepository.save(asignatura);

        AlumnoAsignatura alumnoAsignatura = new AlumnoAsignatura();
        alumnoAsignatura.setAlumno(savedAlumno);
        alumnoAsignatura.setAsignatura(savedAsignatura);
        AlumnoAsignatura savedRelationship = alumnoAsignaturaRepository.save(alumnoAsignatura);

        // Act
        Optional<AlumnoAsignatura> foundRelationship = alumnoAsignaturaRepository
                .findByAlumnoIdAndAsignaturaId(savedAlumno.getId(), savedAsignatura.getId());

        // Assert
        assertTrue(foundRelationship.isPresent());
        assertEquals(savedAlumno.getId(), foundRelationship.get().getAlumno().getId());
        assertEquals(savedAsignatura.getId(), foundRelationship.get().getAsignatura().getId());
    }

    @Test
    @DisplayName("Should not find non-existent alumno")
    void testFindNonExistentAlumno() {
        // Act
        Optional<Alumno> alumno = alumnoRepository.findById(9999L);

        // Assert
        assertFalse(alumno.isPresent());
    }

    @Test
    @DisplayName("Should not find non-existent asignatura")
    void testFindNonExistentAsignatura() {
        // Act
        Optional<Asignatura> asignatura = asignaturaRepository.findById(9999L);

        // Assert
        assertFalse(asignatura.isPresent());
    }

    @Test
    @DisplayName("Should return empty list for non-existent asignatura alumnos")
    void testFindAlumnosByNonExistentAsignatura() {
        // Act
        List<Alumno> alumnos = alumnoRepository.findByAsignaturaId(9999L);

        // Assert
        assertNotNull(alumnos);
        assertTrue(alumnos.isEmpty());
    }
}
