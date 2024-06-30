package com.sanfalcon.literalura.repository;

import com.sanfalcon.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findByNombre(String nombre);

    @Query(value = "SELECT a FROM Autor a WHERE a.fechaNacimiento <= :anio AND (a.fechaMuerte >= :anio OR a.fechaMuerte IS NULL)")
    List<Autor> autoresVivosEnUnDeterminadoAnio(Integer anio);


}
