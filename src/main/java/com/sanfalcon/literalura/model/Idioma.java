package com.sanfalcon.literalura.model;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
public class Idioma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    @ManyToMany(mappedBy = "idiomas")
    private Set<Libro> libros;

    public Idioma() {
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Idioma libro = (Idioma) obj;
        return Objects.equals(this.nombre, libro.nombre);
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.nombre);
    }

    public Idioma(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return this.nombre;
    }
}
