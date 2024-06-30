package com.sanfalcon.literalura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosApiDTO(
        @JsonAlias("count") Integer cantidadResultados,
        @JsonAlias("results") List<DatosLibrosDTO> resultados
) {
}
