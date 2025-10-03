package com.example.cardapio.domain.DTO;

import com.example.cardapio.domain.user.UserRole;

public record RegisterDTO(String name, String email, String password, UserRole role) {

}
