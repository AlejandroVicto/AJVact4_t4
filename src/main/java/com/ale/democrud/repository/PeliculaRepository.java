package com.ale.democrud.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ale.democrud.models.Pelicula;

@Repository
public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {

}

