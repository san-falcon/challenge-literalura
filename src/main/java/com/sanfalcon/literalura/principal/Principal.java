package com.sanfalcon.literalura.principal;

import com.sanfalcon.literalura.dto.DatosApiDTO;
import com.sanfalcon.literalura.model.Autor;
import com.sanfalcon.literalura.model.Idioma;
import com.sanfalcon.literalura.model.Libro;
import com.sanfalcon.literalura.repository.AutorRepository;
import com.sanfalcon.literalura.repository.IdiomaRepository;
import com.sanfalcon.literalura.repository.LibroRepository;
import com.sanfalcon.literalura.service.ConsumoDeApi;
import com.sanfalcon.literalura.service.ConvertirDatos;
import jakarta.persistence.Id;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private int opcionAEscoger;
    private boolean statusEcoger = false;
    private boolean statusMenu = true;
    private ConsumoDeApi consumoDeApi = new ConsumoDeApi();
    private ConvertirDatos convertirDatos = new ConvertirDatos();
    private String url = "https://gutendex.com/books";
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;
    private IdiomaRepository idiomaRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository, IdiomaRepository idiomaRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
        this.idiomaRepository = idiomaRepository;
    }

    public void muestraElMenu() {
        while (this.statusMenu == true) {
            while (this.statusEcoger == false) {
                System.out.println("impresion");
                menu();
            }
            if (this.opcionAEscoger == 0) {
                System.out.println("Saliste con exito");
                this.statusMenu = false;
                break; //? Salimos del bucle
            } else if (this.opcionAEscoger == 1) {
                buscarLibroPorElTitulo();
            } else if (this.opcionAEscoger == 2) {
                listarLibrosRegistrados();
            } else if (this.opcionAEscoger == 3) {
                listarAutoresRegistrados();
            } else if (this.opcionAEscoger == 4) {
                listarAutoresVivoPorAnio();
            } else if (this.opcionAEscoger == 5) {
                listarLibrosPorIdioma();
            } else {
                System.out.println("La opcion escogida no es valida");
            }
            if (this.opcionAEscoger != 0) {
                this.statusEcoger = false; //? Lo reseteamos para que vuelva a pedir el valor del menu
            }
        }
        System.out.println("Programa finalizado");
    }

    private void menu() {
        System.out.println("Escoge una opción");
        System.out.println("""
                1. Busca libro por titulo
                2. Listar libros registrados
                3. Listar autores registrados
                4. Listar autores vivos en un año determinado
                5. Listar libros por idioma
                0. Salir del sistema
                """);
        try {
            this.opcionAEscoger = teclado.nextInt();
            teclado.nextLine(); //? Para que consuma el enter
            this.statusEcoger = true;
        } catch (InputMismatchException e) {
            System.out.println("Entrada no válida. Por favor, ingresa una opción del menu.");
            teclado.next(); //? Limpiamos el Scanner para que no almacene el valor invalido y pida otra entrada valida con normalidad
            this.statusEcoger = false;
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            teclado.next(); //? Limpiamos el Scanner para que no almacene el valor invalido y pida otra entrada valida valida con normalidad
            this.statusEcoger = false;
        }
    }

    private void buscarLibroPorElTitulo() {
        System.out.println("Ingresa el titulo del libro a buscar");
        String libroABuscar = teclado.nextLine();
        String datosApi = consumoDeApi.obtenerDatos(this.url + "?search=" + libroABuscar.replace(" ", "+"));
        DatosApiDTO conversionDeDatos = convertirDatos.obtenerDatos(datosApi, DatosApiDTO.class);
        Optional<Libro> libro = conversionDeDatos.resultados().stream()
                .limit(1)
                .map(l -> new Libro(l.titulo(), l.descargas()))
                .findFirst();
        if (libro.isPresent()) {
            Optional<Libro> validarLibro = libroRepository.findByTitulo(libro.get().getTitulo());
            Libro libroEcontrado;
            if (validarLibro.isPresent()) {
                //? Traemos los datos encontrados para que JPA actualice el libro
                libroEcontrado = validarLibro.get();
            } else {
                //? Usamos los datos de la API para que JPA registre el libro
                libroEcontrado = libro.get();
            }

            //? Se guarda o actualiza el libro dependiendo de los datos obtenidos en la condicion
            libroRepository.save(libroEcontrado);

            //? Autores
            Set<Autor> autores = conversionDeDatos.resultados()
                    .stream()
                    .filter(l -> l.titulo().equals(libroEcontrado.getTitulo()))
                    .flatMap(l -> l.autores().stream().map(a -> {
                        //? Validamos si existe el autor con el mismo libro
                        Optional<Autor> validarAutor = autorRepository.findByNombre(a.nombre());
                        Autor autorEncontrado;
                        if (validarAutor.isPresent()) {
                            //? Utilizamos el mismo autor de la base de datos
                            autorEncontrado = validarAutor.get();
                        } else {
                            //? Creamos un nuevo autor en la base de datos cuando se guarde toda la entidad de libro
                            autorEncontrado = new Autor(a.nombre(), a.fechaNacimiento(), a.fechaMuerte());
                        }
                        return autorEncontrado;
                    }))
                    .collect(Collectors.toSet());
            libroEcontrado.setAutores(autores); //? Agregamos los autores al libro

            //? Idiomas
            Set<Idioma> idiomas = conversionDeDatos.resultados().stream()
                    .filter(l -> l.titulo().equals(libroEcontrado.getTitulo()))
                    .flatMap(l -> l.idiomas().stream().map(i -> {
                        //? Validamos si existe la idioma
                        Optional<Idioma> validarIdioma = idiomaRepository.findByNombre(i);
                        Idioma idiomaEcontrado = new Idioma(i);
                        if (validarIdioma.isPresent()) {
                            idiomaEcontrado = validarIdioma.get();
                        }
                        return idiomaEcontrado;
                    }))
                    .collect(Collectors.toSet());
            libroEcontrado.setIdiomas(idiomas); //? Agregamos las idiomas al libro

            //? Volver a guardar el libro
            libroRepository.save(libroEcontrado);

            System.out.println(libroEcontrado.toString());
        } else {
            System.out.println("No se encontro ningun libro con el titulo '" + libroABuscar + "'");
        }

        /*
        if (conversionDeDatos.cantidadResultados() > 0) {
            Set<Libro> libros = conversionDeDatos.resultados().stream()
                    .limit(1)
                    .map(l -> {
                        Libro libro = new Libro(l.titulo(), l.descargas());

                        //? Listar autores
                        //? Consultamos si el autor ya existe en la base de datos
                        Set<Autor> autores = l.autores().stream()
                                .map(a -> new Autor(a.nombre(), a.fechaNacimiento(), a.fechaMuerte()))
                                .collect(Collectors.toSet());

                        //? Lista de idiomas
                        Set<Idioma> idiomas = l.idiomas().stream()
                                .map(i -> new Idioma(i))
                                .collect(Collectors.toSet());
                        libro.setIdiomas(idiomas);

                        //? Actualizamos el objecto libro
                        libro.setAutores(autores);
                        return libro;
                    }).collect(Collectors.toSet());

            //? Guardamos los libros
            libros.forEach(l -> {
                //? Validamos si existe un libro
                boolean buscarLibroDB = libroRepository.existsByTitulo(l.getTitulo());
                if (!buscarLibroDB) {
                    libroRepository.save(l);
                }
                System.out.println(l.toString());
            });
        } else {
            System.out.println("No se encontro ningun libro con el titulo '" + libroABuscar + "'");
        }
        */
    }

    private void listarLibrosRegistrados() {
        libroRepository.findAll().stream()
                .forEach(System.out::println);
    }

    private void listarAutoresRegistrados() {
        autorRepository.findAll().stream()
                .forEach(System.out::println);
    }

    private void listarAutoresVivoPorAnio() {
        try {
            System.out.println("Ingrese el año vivo del autor(res) que desea buscar");
            Integer anio = teclado.nextInt();
            List<Autor> autores = autorRepository.autoresVivosEnUnDeterminadoAnio(anio);
            if (autores.size() > 0) {
                autores.stream().forEach(System.out::println);
            } else {
                System.out.println("No se encontro autores en este año registrado");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada no válida. Por favor, vuelve a intentarlo");
            teclado.next(); //? Limpiamos el Scanner para que no almacene el valor invalido y pida otra entrada valida con normalidad
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            teclado.next(); //? Limpiamos el Scanner para que no almacene el valor invalido y pida otra entrada valida valida con normalidad
        }

    }

    private void listarLibrosPorIdioma() {
        System.out.println("Escriba el codigo del idioa para buscar los libros");
        System.out.println("""
                es - Español
                en - Ingles
                fr - Frances
                pt - Portugues
                """);
        String idioma = teclado.nextLine();
        List<Libro> librosPorIdioma = libroRepository.librosPorIdioma(idioma);
        if (librosPorIdioma.size() > 0) {
            librosPorIdioma.stream().forEach(System.out::println);
        } else {
            System.out.println("No se encontro libros con el codigo de idioma '" + idioma + "' ");
        }
    }
}
