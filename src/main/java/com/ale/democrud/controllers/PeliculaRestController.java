package com.ale.democrud.controllers;


import com.ale.democrud.models.Director;
import com.ale.democrud.models.Pelicula;
import com.ale.democrud.services.DirectorService;
import com.ale.democrud.services.PeliculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PeliculaRestController {

    @Autowired
    private PeliculaService peliculaService;

    @Autowired
    private DirectorService directorService;

    @GetMapping("/directores")
    public List<Director> listarDirectores() {
        return directorService.listarTodos();
    }

    @PostMapping("/directores")
    public Director crearDirector(@RequestBody Director director) {
        return directorService.guardar(director);
    }


    // 1. LEER 
    @GetMapping("/peliculas")
    public List<Pelicula> listarPeliculas() {
        return peliculaService.listarTodas();
    }

    // LEER UNA POR ID 
    @GetMapping("/peliculas/{id}")
    public ResponseEntity<Pelicula> obtenerPelicula(@PathVariable Long id) {
        return peliculaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 2. CREAR
    @PostMapping("/peliculas")
    public Pelicula crearPelicula(@RequestBody Pelicula pelicula) {
        if (pelicula.getDirector() != null && pelicula.getDirector().getId() != null) {
            Director dir = directorService.obtenerPorId(pelicula.getDirector().getId()).orElse(null);
            pelicula.setDirector(dir);
        }
        return peliculaService.guardar(pelicula);
    }

    // 3. ACTUALIZAR 
    @PutMapping("/peliculas/{id}")
    public ResponseEntity<Pelicula> actualizarPelicula(@PathVariable Long id, @RequestBody Pelicula detalles) {
        return peliculaService.obtenerPorId(id).map(peliculaExistente -> {
            peliculaExistente.setTitulo(detalles.getTitulo());
            peliculaExistente.setGenero(detalles.getGenero());
            peliculaExistente.setAnio(detalles.getAnio());
            if (detalles.getDirector() != null && detalles.getDirector().getId() != null) {
                Director dir = directorService.obtenerPorId(detalles.getDirector().getId()).orElse(null);
                peliculaExistente.setDirector(dir);
            }
            return ResponseEntity.ok(peliculaService.guardar(peliculaExistente));
        }).orElse(ResponseEntity.notFound().build());
    }

    // 4. ELIMINAR 
    @DeleteMapping("/peliculas/{id}")
    public ResponseEntity<Void> eliminarPelicula(@PathVariable Long id) {
        if (peliculaService.obtenerPorId(id).isPresent()) {
            peliculaService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}