package com.alura.challenge.literatura.principal;

import com.alura.challenge.literatura.Data.DatosLibro;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id", nullable = false)
    private Autor autor;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "libro_idiomas", joinColumns = @JoinColumn(name = "libro_id"))
    @Column(name = "idioma")
    private List<String> idioma = new ArrayList<>();

    private Integer numeroDeDescargas;

    // Constructores, getters y setters

    public Libro() {}

    public Libro(DatosLibro datosLibros, Autor autor) {
        this.titulo = datosLibros.titulo();
        this.idioma = datosLibros.idiomas();
        this.numeroDeDescargas = datosLibros.numeroDeDescargas();
        this.autor = autor;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public List<String> getIdioma() {
        return idioma;
    }

    public void setIdioma(List<String> idioma) {
        this.idioma = idioma;
    }

    public Integer getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Integer numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    @Override
    public String toString() {
        return  " Titulo: " + titulo + '\n' +
                " Autor: " + autor.getNombre() + '\n' +
                " Idioma: " + idioma.get(0) + '\n' +
                " Numero de Descargas: " + numeroDeDescargas;
    }
}