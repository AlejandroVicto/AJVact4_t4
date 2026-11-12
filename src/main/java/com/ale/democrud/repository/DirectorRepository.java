package com.ale.democrud.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ale.democrud.models.Director;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Long> {

}
