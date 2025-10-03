package com.example.cardapio.domain.food;

import com.example.cardapio.domain.DTO.FoodRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "foods")
@Entity(name = "foods")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String image;

    private Integer price;

    public Food(FoodRequestDTO food) {
        this.image = food.image();
        this.title = food.title();
        this.price = food.price();
    }
}
