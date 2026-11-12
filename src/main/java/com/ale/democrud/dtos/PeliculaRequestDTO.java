package com.ale.democrud.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PeliculaRequestDTO {

    @NotBlank(message = "El título de la película es obligatorio")
    private String titulo;

    @NotBlank(message = "El género es obligatorio")
    private String genero;

    @NotNull(message = "El año es obligatorio")
    @Min(value = 1888, message = "El año debe ser mayor o igual a 1888")
    private Integer anio;

    @NotNull(message = "El ID del director (directorId) es obligatorio")
    private Long directorId;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Long getDirectorId() {
        return directorId;
    }

    public void setDirectorId(Long directorId) {
        this.directorId = directorId;
    }
}