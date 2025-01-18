package com.alura.challenge.literatura.principal;

import com.alura.challenge.literatura.Data.DatosAutor;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Integer fechaDeNacimiento;
    private Integer fechaDeFallecimiento;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Libro> librosDelAutor = new ArrayList<>();



    public Autor() {}

    public Autor(DatosAutor datosAutor) {
        this.nombre = datosAutor.nombreAutor();
        this.fechaDeNacimiento = datosAutor.fechaDeNacimiento();
        this.fechaDeFallecimiento = datosAutor.fechaDeFallecimiento();
    }

    public void addLibro(Libro libro) {
        librosDelAutor.add(libro);
        libro.setAutor(this);
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(Integer fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public Integer getFechaDeFallecimiento() {
        return fechaDeFallecimiento;
    }

    public void setFechaDeFallecimiento(Integer fechaDeFallecimiento) {
        this.fechaDeFallecimiento = fechaDeFallecimiento;
    }

    @Override
    public String toString() {
        String librosTitulos = librosDelAutor.stream()
                .map(Libro::getTitulo)
                .collect(Collectors.joining(", "));

        return "Autor: " + nombre + '\n' +
                "Fecha De Nacimiento: " + fechaDeNacimiento + '\n' +
                "Fecha De Fallecimiento: " + fechaDeFallecimiento + '\n' +
                "Libros: " + librosTitulos + '\n';
    }
}