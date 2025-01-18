package com.alura.challenge.literatura.repositorio;

import com.alura.challenge.literatura.principal.Autor;
import org.springframework.data.jpa.repository.JpaRepository;



import java.util.List;

public interface AutorR extends JpaRepository<Autor, Long> {
    Autor findByNombreIgnoreCase(String nombre);
    List<Autor> findByFechaDeNacimientoLessThanEqualAndFechaDeFallecimientoGreaterThanEqual(int anioInicial, int anioFinal);

}

