package com.sanfalcon.literalura.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    @Column(name = "fecha_nacimento")
    private Integer fechaNacimiento;
    @Column(name = "fecha_muerte")
    private Integer fechaMuerte;
    @ManyToMany(mappedBy = "autores", fetch = FetchType.EAGER)
    private Set<Libro> libros;

    public Autor() {
    }

    public Autor(String nombre, Integer fechaNacimento, Integer fechaMuerte) {
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimento;
        this.fechaMuerte = fechaMuerte;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(int fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getFechaMuerte() {
        return fechaMuerte;
    }

    public void setFechaMuerte(int fechaMuerte) {
        this.fechaMuerte = fechaMuerte;
    }

    public Set<Libro> getLibros() {
        return libros;
    }

    public void setLibros(Set<Libro> libros) {
        this.libros = libros;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Autor autor = (Autor) obj;
        return Objects.equals(this.nombre, autor.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.nombre);
    }

    @Override
    public String toString() {
        List<String> libros = this.libros.stream().map(l -> l.getTitulo()).collect(Collectors.toList());
        return String.format("""
                ======== Autor ========
                Nombre: %s
                Fecha de nacimiento: %s
                Fecha de muerte: %s
                Libros: %s
                ========================
                """, this.nombre, this.fechaNacimiento, this.fechaMuerte, libros);
    }
}
