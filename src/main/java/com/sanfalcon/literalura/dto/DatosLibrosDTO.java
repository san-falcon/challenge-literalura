package com.sanfalcon.literalura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibrosDTO(
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<DatosAutoresDTO> autores,
        @JsonAlias("languages") List<String> idiomas,
        @JsonAlias("download_count") Integer descargas
) {
}
