package com.udemy.practicas.luisbravo.client.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.udemy.practicas.luisbravo.client.services.PokeApiService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pokemon")
public class PokemonController {

    @Autowired
    private PokeApiService pokeApiService;

    @GetMapping("/{name}")
    public Map<String, Object> getPokemon(@PathVariable String name) throws Exception {
        return pokeApiService.getLocalEncryptedPokemon(name);
    }

    @GetMapping("/decrypted/{name}")
    public Map<String, Object> getDecryptedPokemon(@PathVariable String name) throws Exception {
        return pokeApiService.getLocalDecryptedPokemon(name);
    }

    @PostMapping("/search")
    public String searchEncryptedPokemonByFilter(@RequestBody Map<String, Object> criteria) throws Exception {
        return pokeApiService.searchEncryptedPokemonByFilter(criteria);
    }
    
    @PostMapping("/decrypted/search")
    public List<Map<String, Object>> searchDecryptedPokemonByFilter(@RequestBody Map<String, Object> criteria) throws Exception {
        return pokeApiService.searchDecryptedPokemonByFilter(criteria);
    }

}