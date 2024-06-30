package com.sanfalcon.literalura.repository;

import com.sanfalcon.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    Optional<Libro> findByTitulo(String titulo);

    @Query(value = "SELECT l FROM Libro l JOIN l.idiomas i WHERE i.nombre = :idioma")
    List<Libro> librosPorIdioma(String idioma);

}
