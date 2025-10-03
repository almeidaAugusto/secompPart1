package com.example.cardapio.domain.DTO;

import com.example.cardapio.domain.food.Food;

public record FoodResponseDTO(Long id, String title, String image, Integer price) {
    public FoodResponseDTO(Food food){
        this(food.getId(), food.getTitle(), food.getImage(), food.getPrice());
    }
}
