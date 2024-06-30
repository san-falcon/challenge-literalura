package com.sanfalcon.literalura.repository;

import com.sanfalcon.literalura.model.Idioma;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdiomaRepository extends JpaRepository<Idioma, Long> {

    Optional<Idioma> findByNombre(String idioma);
}
