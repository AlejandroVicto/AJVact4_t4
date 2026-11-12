package com.ale.democrud.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ale.democrud.models.Director;
import com.ale.democrud.repository.DirectorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DirectorService {

    @Autowired
    private DirectorRepository directorRepository;

    public List<Director> listarTodos() {
        return directorRepository.findAll();
    }

    public Optional<Director> obtenerPorId(Long id) {
        return directorRepository.findById(id);
    }

    public Director guardar(Director director) {
        return directorRepository.save(director);
    }

    public void eliminar(Long id) {
        directorRepository.deleteById(id);
    }
}