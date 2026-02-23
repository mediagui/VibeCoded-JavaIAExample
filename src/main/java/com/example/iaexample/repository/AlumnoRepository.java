package com.example.iaexample.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.iaexample.entity.Alumno;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

    @Query("SELECT DISTINCT a FROM Alumno a " +
            "JOIN a.matriculas m " +
            "WHERE m.asignatura.id = :asignaturaId")
    List<Alumno> findByAsignaturaId(@Param("asignaturaId") Long asignaturaId);
}
