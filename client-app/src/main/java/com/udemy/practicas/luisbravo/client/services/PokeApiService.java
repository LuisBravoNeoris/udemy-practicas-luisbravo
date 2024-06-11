package com.udemy.practicas.luisbravo.client.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.udemy.practicas.luisbravo.client.models.Pokemon;
import com.udemy.practicas.luisbravo.client.repositories.PokemonRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PokeApiService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private PokemonRepository pokemonRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final EncryptionService encrypt;

    @Autowired
    public PokeApiService(EncryptionService encrypt) {
        this.encrypt = encrypt;
    }

    public Map<String, Object> getPokemon(String name, boolean isDecrypted) throws Exception {

        Optional<Pokemon> localPokemon = getLocalNullablePokemon(name);

        if (localPokemon.isPresent()) {
            return getLocalPokemon(name, isDecrypted);
        }

        String url = String.format("https://pokeapi.co/api/v2/pokemon/%s", name);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<Map<String, Object>>() {
                });
        Map<String, Object> pokemonData = response.getBody();

        if (pokemonData != null) {
            Map<String, Object> filteredData = new LinkedHashMap<>();

            filteredData.put("id", pokemonData.get("id"));
            filteredData.put("is_default", pokemonData.get("is_default"));
            filteredData.put("location_area_encounters", pokemonData.get("location_area_encounters"));
            filteredData.put("moves", pokemonData.get("moves"));
            filteredData.put("name", pokemonData.get("name"));
            filteredData.put("order", pokemonData.get("order"));
            filteredData.put("past_abilities", pokemonData.get("past_abilities"));
            filteredData.put("past_types", pokemonData.get("past_types"));
            filteredData.put("species", pokemonData.get("species"));
            filteredData.put("sprites", pokemonData.get("sprites"));
            filteredData.put("stats", pokemonData.get("stats"));
            filteredData.put("types", pokemonData.get("types"));
            filteredData.put("weight", pokemonData.get("weight"));

            try {
                String data = objectMapper.writeValueAsString(filteredData);
                data = encrypt.encrypt(data);

                Pokemon pokemon = new Pokemon();
                pokemon.setId(Long.valueOf(pokemonData.get("id").toString()));
                pokemon.setName(name);
                pokemon.setData(data);
                pokemonRepository.save(pokemon);

                return getLocalPokemon(name, isDecrypted);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error al convertir el map a JSON", e);
            }
        }
        return null;
    }

    public Map<String, Object> getLocalPokemon(String name, boolean isDecrypted) throws Exception {

        Pokemon pokemon = pokemonRepository.findByName(name);
        String jsonData = pokemon.getData();

        if (isDecrypted) {
            jsonData = encrypt.decrypt(jsonData);
        } else {
            return Map.of("Pokemon encriptado " + name, jsonData);
        }

        try {
            return objectMapper.readValue(jsonData, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al convertir el map a JSON", e);
        }
    }

    private List<Map<String, Object>> filterAndDecryptPokemon(Map<String, Object> criteria) throws Exception {
        
        searchPokemonIfNotPresent((String) criteria.get("name"));
        List<Pokemon> allPokemon = pokemonRepository.findAll();
        List<Map<String, Object>> filteredPokemonList = new ArrayList<>();

        for (Pokemon pokemon : allPokemon) {
            String jsonData = encrypt.decrypt(pokemon.getData());

            try {
                Map<String, Object> pokemonData = objectMapper.readValue(jsonData,
                        new TypeReference<Map<String, Object>>() {});

                boolean matches = criteria.entrySet().stream()
                        .anyMatch(entry -> entry.getValue().equals(pokemonData.get(entry.getKey())));

                if (matches) {
                    filteredPokemonList.add(pokemonData);
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error al convertir el JSON a Map", e);
            }
        }

        return filteredPokemonList;
    }

    public List<Map<String, Object>> searchDecryptedPokemonByFilter(Map<String, Object> criteria) throws Exception {
        searchPokemonIfNotPresent((String) criteria.get("name"));
        return filterAndDecryptPokemon(criteria);
    }

    public String searchEncryptedPokemonByFilter(Map<String, Object> criteria) throws Exception {
        List<Map<String, Object>> filteredPokemonList = filterAndDecryptPokemon(criteria);
        return encryptList(filteredPokemonList);
    }

    public String encryptList(List<Map<String, Object>> dataList) throws Exception {
        String jsonString = objectMapper.writeValueAsString(dataList);
        return encrypt.encrypt(jsonString);
    }

    public Map<String, Object> getLocalEncryptedPokemon(String name) throws Exception {
        return getPokemon(name, false);
    }

    public Map<String, Object> getLocalDecryptedPokemon(String name) throws Exception {
        return getPokemon(name, true);
    }

    public Optional<Pokemon> getLocalNullablePokemon(String name) {
        return Optional.ofNullable(this.pokemonRepository.findByName(name));
    }

    private void searchPokemonIfNotPresent(String name){
        Optional<Pokemon> localPokemon = getLocalNullablePokemon(name);

        if (!localPokemon.isPresent()) {
            try {
                getPokemon(name, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}