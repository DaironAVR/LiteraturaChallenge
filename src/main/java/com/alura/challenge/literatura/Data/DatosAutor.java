package com.alura.challenge.literatura.Data;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosAutor(
        //nombre, nacimiento, fallecio
        @JsonAlias("name") String nombreAutor,
        @JsonAlias("birth_year") int fechaDeNacimiento,
        @JsonAlias("death_year") int fechaDeFallecimiento
) {

}