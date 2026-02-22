package com.example.iaexample.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.iaexample.entity.AlumnoAsignatura;

@Repository
public interface AlumnoAsignaturaRepository extends JpaRepository<AlumnoAsignatura, Long> {

    Optional<AlumnoAsignatura> findByAlumnoIdAndAsignaturaId(Long alumnoId, Long asignaturaId);
}
