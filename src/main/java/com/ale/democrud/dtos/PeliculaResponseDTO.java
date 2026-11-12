package com.ale.democrud.dtos;

public class PeliculaResponseDTO {
    private Long id;
    private String titulo;
    private String genero;
    private Integer anio;
    private Long directorId;
    private String directorNombre;
    private String directorNacionalidad;

    public PeliculaResponseDTO(Long id, String titulo, String genero, Integer anio,
            Long directorId, String directorNombre, String directorNacionalidad) {
        this.id = id;
        this.titulo = titulo;
        this.genero = genero;
        this.anio = anio;
        this.directorId = directorId;
        this.directorNombre = directorNombre;
        this.directorNacionalidad = directorNacionalidad;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getGenero() {
        return genero;
    }

    public Integer getAnio() {
        return anio;
    }

    public Long getDirectorId() {
        return directorId;
    }

    public String getDirectorNombre() {
        return directorNombre;
    }

    public String getDirectorNacionalidad() {
        return directorNacionalidad;
    }
}