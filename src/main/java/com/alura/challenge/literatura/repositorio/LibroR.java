package com.alura.challenge.literatura.repositorio;

import com.alura.challenge.literatura.principal.Libro;
import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LibroR extends JpaRepository<Libro, Long> {
    Libro findByTitulo (String titulo);

    List<Libro> findByIdiomaContaining(String idiomas);

    @Query("SELECT l FROM Libro l LEFT JOIN FETCH l.idioma")
    List<Libro> findAllWithIdiomas();
}
