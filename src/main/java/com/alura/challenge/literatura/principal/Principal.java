package com.alura.challenge.literatura.principal;



import com.alura.challenge.literatura.Data.Datos;
import com.alura.challenge.literatura.Data.DatosAutor;
import com.alura.challenge.literatura.Data.DatosLibro;
import com.alura.challenge.literatura.repositorio.AutorR;
import com.alura.challenge.literatura.repositorio.LibroR;
import com.alura.challenge.literatura.servicio.ConsumoAPI;
import com.alura.challenge.literatura.servicio.ConvierteDatos;

import java.util.*;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private ConvierteDatos conversor = new ConvierteDatos();
    private final AutorR autorRepositorio;
    private final LibroR libroRepositorio;

    public Principal(AutorR autoresRepositorio, LibroR librosRepositorio) {
        this.autorRepositorio = autoresRepositorio;
        this.libroRepositorio = librosRepositorio;
    }

    public void muestraElMenu() {

        var opcion = -1;
        var menuInicio = """
                \n Elija la opción a través de su número:
                 1- Buscar libro por titulo.
                 2- Buscar libros registrados en la base de datos.
                 3- Listar autores registrados
                 4- Listar autores vivos en un determinado año
                 5- Listar libros por idioma
                 0- Salir
                 \nSeleccione una de las opciones disponibles""";

        while (opcion != 0) {
            System.out.println("\n------ Bienvenido al challenge de literatura -----");
            System.out.println(menuInicio);

            try {
                opcion = teclado.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Error: Solo numeros enteros");

            } finally {
                teclado.nextLine();
            }

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresPorAño();
                    break;
                case 5:
                    listarLibrosPorIdiomas();
                    break;
                case 0:
                    System.out.println("Programa finalizado, Hasta pronto!....");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opcion no valida, por favor vuelva a intentar.");
                    break;
            }

        }
    }

    private Datos getDatosLibros() {
        var tituloLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatosAPI(URL_BASE + tituloLibro.replace(" ", "+"));
        Datos datosLibros = conversor.obtenerDatos(json, Datos.class);
        return datosLibros;
    }

    private Libro crearLibro(DatosLibro datosLibros, Autor autor) {
        if (autor != null) {
            return new Libro(datosLibros, autor);
        } else {
            System.out.println("El autor es null, no se puede crear el libro");
            return null;
        }
    }
    private void buscarLibroPorTitulo() {
        System.out.println("Ingrese el nombre del libro que desea buscar");
        Datos datos = getDatosLibros();
        if (!datos.resultados().isEmpty()) {
            DatosLibro datosLibros = datos.resultados().get(0);
            DatosAutor datosAutores = datosLibros.autor().get(0);

            Libro libroEncontrado = libroRepositorio.findByTitulo(datosLibros.titulo());
            if (libroEncontrado != null) {
                System.out.println("Este libro ya se encuentra en la base de datos");
                System.out.println(libroEncontrado.toString());
            } else {
                Autor autorEncontrado = autorRepositorio.findByNombreIgnoreCase(datosAutores.nombreAutor());
                if (autorEncontrado != null) {
                    Libro nuevoLibro = crearLibro(datosLibros, autorEncontrado);
                    libroRepositorio.save(nuevoLibro);
                    System.out.println("----- LIBRO AGREGADO -----\n" + nuevoLibro);
                    System.out.println("----- -------------- -----\n");
                } else {
                    Autor nuevoAutor = new Autor(datosAutores);
                    nuevoAutor = autorRepositorio.save(nuevoAutor);
                    Libro nuevoLibro = crearLibro(datosLibros, nuevoAutor);
                    libroRepositorio.save(nuevoLibro);
                    System.out.println("----- LIBRO -----\n" + nuevoLibro + "\n");
                    System.out.println("----- -------------- -----\n");
                }
            }
        } else {
            System.out.println("Libro No Encontrado, intenta de nuevo");
        }
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = libroRepositorio.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados");
            return;
        }
        System.out.println("----- LOS LIBROS REGISTRADOS SON: -----\n");
        libros.stream()
                .sorted(Comparator.comparing(Libro::getTitulo))
                .forEach(libro -> {
                    System.out.println("------------LIBRO----------------");
                    System.out.println(libro);
                    System.out.println("----------------------------\n");
                });
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepositorio.findAll();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados");
            return;
        }
        System.out.println("----- LOS AUTORES REGISTRADOS SON: -----\n");
        autores.stream()
                .sorted(Comparator.comparing(Autor::getNombre))
                .forEach(System.out::println);
    }

    private void listarAutoresPorAño() {
        System.out.println("Escribe el año en el que deseas buscar: ");
        var anio = teclado.nextInt();
        teclado.nextLine();
        if(anio < 0) {
            System.out.println("El año debe ser mayor a cero");
            return;
        }
        List<Autor> autoresPorAnio = autorRepositorio.findByFechaDeNacimientoLessThanEqualAndFechaDeFallecimientoGreaterThanEqual(anio, anio);
        if (autoresPorAnio.isEmpty()) {
            System.out.println("No se encontraron autores en ese año");
            return;
        }
        System.out.println("----- LOS AUTORES VIVOS REGISTRADOS EN EL AÑO " + anio + " SON: -----\n");
        autoresPorAnio.stream()
                .sorted(Comparator.comparing(Autor::getNombre))
                .forEach(System.out::println);
    }

    private void listarLibrosPorIdiomas() {
        System.out.println("Escribe el idioma por el que deseas buscar: ");
        String menu = """
                es - Español
                en - Inglés
                fr - Francés
                pt - Portugués
                \nIngrese el idioma de la forma (es-en-fr-pt).""";

        System.out.println(menu);
        var idioma = teclado.nextLine();
        if (!idioma.equals("es") && !idioma.equals("en") && !idioma.equals("fr") && !idioma.equals("pt")) {
            System.out.println("Idioma no válido, intenta de nuevo");
            return;
        }
        List<Libro> librosPorIdioma = libroRepositorio.findByIdiomaContaining(idioma);
        if (librosPorIdioma.isEmpty()) {
            System.out.println("No hay libros registrados en el idioma: " + idioma);
            return;
        }
        System.out.println("----- LOS LIBROS REGISTRADOS EN EL IDIOMA SELECCIONADO SON: -----\n");
        librosPorIdioma.stream()
                .sorted(Comparator.comparing(Libro::getTitulo))
                .forEach(System.out::println);
    }



}