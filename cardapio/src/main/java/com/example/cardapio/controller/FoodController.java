package com.example.cardapio.controller;

import com.example.cardapio.domain.DTO.FoodRequestDTO;
import com.example.cardapio.domain.DTO.FoodResponseDTO;
import com.example.cardapio.domain.food.Food;
import com.example.cardapio.repositories.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("food")
public class FoodController {

    @Autowired
    private FoodRepository foodRepository;

    @GetMapping
    public List<FoodResponseDTO> getAll(){

        return foodRepository.findAll().stream().map(FoodResponseDTO::new).toList();
    }

    @PostMapping
    public FoodResponseDTO create(@RequestBody FoodRequestDTO data){
        Food foodData = new Food(data);
        return new FoodResponseDTO(foodRepository.save(foodData));
    }


}
