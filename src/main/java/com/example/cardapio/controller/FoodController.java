package com.example.cardapio.controller;

import com.example.cardapio.domain.food.Food;
import com.example.cardapio.dto.FoodRequestDTO;
import com.example.cardapio.dto.FoodResponseDTO;
import com.example.cardapio.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/food")

public class FoodController {
    @Autowired
    private FoodRepository repository;
    @GetMapping
    public List<FoodResponseDTO> GetAll(){
        List<FoodResponseDTO> foodList = repository.findAll().stream().map(FoodResponseDTO::new).toList();
        return foodList;
    }
    @PostMapping
    public void saveFood(@RequestBody FoodRequestDTO data){
        Food foodData = new Food(data);
        repository.save(foodData);
        return;
    }
    @GetMapping("/{id}") // Define um endpoint GET que aceita um parâmetro de caminho "id"
    public FoodResponseDTO getById(@PathVariable Long id) { // O parâmetro "id" é extraído da URL e passado para o método
        Food food = repository.findById(id) // Busca um objeto Food no repositório pelo ID fornecido
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Food não encontrado")); // Lança uma exceção 404 se o Food não for encontrado
        return new FoodResponseDTO(food); // Retorna os dados do Food encontrado como um DTO de resposta
    }
    @PatchMapping("/{id}") // Define um endpoint PATCH que aceita um parâmetro de caminho "id"
    public FoodResponseDTO patch(@PathVariable Long id, @RequestBody FoodRequestDTO dto) {
        // Busca um objeto Food no repositório pelo ID fornecido
        // Lança uma exceção 404 se o Food não for encontrado
        Food food = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Food não encontrado"));
        // Atualiza os campos do objeto Food com os valores fornecidos no DTO, se não forem nulos
        if (dto.title() != null) food.setTitle(dto.title());
        if (dto.image() != null) food.setImage(dto.image());
        if (dto.price() != null) food.setPrice(dto.price());
        // Salva o objeto Food atualizado no repositório
        Food saveFood = repository.save(food);
        // Retorna os dados do objeto Food atualizado como um DTO de resposta
        return new FoodResponseDTO(saveFood);
    }
    @DeleteMapping("/{id}") // DELETE /food/{id}
            //responseentity<Void> indica que a resposta não terá corpo
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        // Busca o registro; se não existir, retorna 404
        Food food = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Food não encontrado"));
        // Remove do banco
        repository.delete(food);
        // Sucesso sem corpo
        return ResponseEntity.noContent().build(); // 204
    }



}
