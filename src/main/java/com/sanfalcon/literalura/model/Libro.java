package com.sanfalcon.literalura.model;

import com.sanfalcon.literalura.dto.DatosAutoresDTO;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private Integer descargas;
    /*?
       @JoinTable se usa para especificar la tabla intermedia que JPA debe crear.
       joinColumns define la clave foránea en la tabla intermedia que referencia a la entidad actual.
       inverseJoinColumns define la clave foránea en la tabla intermedia que referencia a la otra entidad en la relación.
     */
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "libro_autor",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private Set<Autor> autores;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "libro_idioma",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "idioma_id")
    )
    private Set<Idioma> idiomas;

    public Libro() {
    }

    public Libro(String titulo, Integer descargas) {
        this.titulo = titulo;
        this.descargas = descargas;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getDescargas() {
        return descargas;
    }

    public void setDescargas(Integer descargas) {
        this.descargas = descargas;
    }

    public Set<Autor> getAutores() {
        return autores;
    }

    public void setAutores(Set<Autor> autores) {
        this.autores = autores;
    }

    public Set<Idioma> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(Set<Idioma> idiomas) {
        this.idiomas = idiomas;
    }

    @Override
    public String toString() {
        String autores = null;
        if (this.autores != null) {
            autores = this.autores.stream()
                    .map(a -> a.getNombre())
                    .collect(Collectors.joining("; "));
        }
        String idiomas = null;
        if (this.idiomas != null) {
            idiomas = this.idiomas.stream()
                    .map(i -> i.getNombre())
                    .collect(Collectors.joining("; "));
        }

        return String.format("""
                ======== Libro ========
                Titulo: %s
                Autor(es): %s
                Idioma(s): %s
                Descargas: %s
                ========================
                """, this.titulo, autores, idiomas, this.descargas);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Libro libro = (Libro) obj;
        return Objects.equals(this.titulo, libro.titulo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.titulo);
    }
}
