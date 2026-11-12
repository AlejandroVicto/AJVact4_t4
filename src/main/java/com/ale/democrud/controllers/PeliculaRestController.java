package com.ale.democrud.controllers;

import com.ale.democrud.dtos.PeliculaRequestDTO;
import com.ale.democrud.dtos.PeliculaResponseDTO;
import com.ale.democrud.models.Director;
import com.ale.democrud.models.Pelicula;
import com.ale.democrud.services.DirectorService;
import com.ale.democrud.services.PeliculaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PeliculaRestController {

    private final PeliculaService peliculaService;
    private final DirectorService directorService;

    public PeliculaRestController(PeliculaService peliculaService, DirectorService directorService) {
        this.peliculaService = peliculaService;
        this.directorService = directorService;
    }

    private PeliculaResponseDTO toDTO(Pelicula p) {
        return new PeliculaResponseDTO(
                p.getId(),
                p.getTitulo(),
                p.getGenero(),
                p.getAnio(),
                p.getDirector().getId(),
                p.getDirector().getNombre(),
                p.getDirector().getNacionalidad());
    }

    // a) GET para listar con paginación: /api/peliculas?page=0&size=5
    @GetMapping("/peliculas")
    public ResponseEntity<Page<PeliculaResponseDTO>> listarPeliculas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PeliculaResponseDTO> resultado = peliculaService.listarPaginado(pageable).map(this::toDTO);
        return ResponseEntity.ok(resultado);
    }

    // a) GET para ver un registro individual
    @GetMapping("/peliculas/{id}")
    public ResponseEntity<?> obtenerPelicula(@PathVariable Long id) {
        Optional<Pelicula> peliculaOpt = peliculaService.obtenerPorId(id);
        if (peliculaOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Película con ID " + id + " no encontrada"));
        }
        return ResponseEntity.ok(toDTO(peliculaOpt.get()));
    }

    // b) POST para crear con DTO y @Valid (Devuelve 201 Created)
    @PostMapping("/peliculas")
    public ResponseEntity<?> crearPelicula(@Valid @RequestBody PeliculaRequestDTO dto) {
        Optional<Director> directorOpt = directorService.obtenerPorId(dto.getDirectorId());
        if (directorOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "No existe un director con el ID " + dto.getDirectorId()));
        }

        Pelicula nueva = new Pelicula();
        nueva.setTitulo(dto.getTitulo());
        nueva.setGenero(dto.getGenero());
        nueva.setAnio(dto.getAnio());
        nueva.setDirector(directorOpt.get());

        Pelicula guardada = peliculaService.guardar(nueva);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(guardada));
    }

    // c) PUT para actualizar con DTO y @Valid (Devuelve 200 OK o 404 Not Found)
    @PutMapping("/peliculas/{id}")
    public ResponseEntity<?> actualizarPelicula(@PathVariable Long id, @Valid @RequestBody PeliculaRequestDTO dto) {
        Optional<Pelicula> existenteOpt = peliculaService.obtenerPorId(id);
        if (existenteOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Película con ID " + id + " no encontrada"));
        }

        Optional<Director> directorOpt = directorService.obtenerPorId(dto.getDirectorId());
        if (directorOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "No existe un director con el ID " + dto.getDirectorId()));
        }

        Pelicula pelicula = existenteOpt.get();
        pelicula.setTitulo(dto.getTitulo());
        pelicula.setGenero(dto.getGenero());
        pelicula.setAnio(dto.getAnio());
        pelicula.setDirector(directorOpt.get());

        Pelicula actualizada = peliculaService.guardar(pelicula);
        return ResponseEntity.ok(toDTO(actualizada));
    }

    // d) DELETE para eliminar (Devuelve 200 OK con mensaje o 404 Not Found)
    @DeleteMapping("/peliculas/{id}")
    public ResponseEntity<?> eliminarPelicula(@PathVariable Long id) {
        if (peliculaService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Película con ID " + id + " no encontrada"));
        }
        peliculaService.eliminar(id);
        return ResponseEntity.ok(Map.of("mensaje", "Película eliminada correctamente"));
    }

    // Endpoints auxiliares de Directores
    @GetMapping("/directores")
    public ResponseEntity<List<Director>> listarDirectores() {
        return ResponseEntity.ok(directorService.listarTodos());
    }

    @PostMapping("/directores")
    public ResponseEntity<Director> crearDirector(@RequestBody Director director) {
        return ResponseEntity.status(HttpStatus.CREATED).body(directorService.guardar(director));
    }
}