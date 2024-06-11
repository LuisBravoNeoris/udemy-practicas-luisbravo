package com.udemy.practicas.luisbravo.client.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.udemy.practicas.luisbravo.client.models.Pokemon;

@Repository
public interface PokemonRepository extends JpaRepository<Pokemon, Long> {
    
    Pokemon findByName(String name);

}