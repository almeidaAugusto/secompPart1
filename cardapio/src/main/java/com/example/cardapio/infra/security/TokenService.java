package com.example.cardapio.infra.security;

// Importações necessárias para manipulação de JWT, exceções e classes de domínio
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.cardapio.domain.user.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

// Declaração da classe como um serviço gerenciado pelo Spring
@Service
public class TokenService {

    // Método para gerar um token JWT baseado em um usuário
    public String generateToken(User user) {
        // Define a chave secreta usada para assinar o token
        var secret = "senha-secreta";

        try {
            // Cria um algoritmo de assinatura HMAC com a chave secreta
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // Cria o token JWT com informações específicas
            String token = JWT.create()
                    .withIssuer("cardapio") // Define o emissor do token
                    .withSubject(user.getEmail()) // Define o assunto do token como o email do usuário
                    .withExpiresAt(genExpirationDate()) // Define a data de expiração do token
                    .sign(algorithm); // Assina o token com o algoritmo

            return token; // Retorna o token gerado
        } catch (JWTCreationException exception) {
            // Lança uma exceção em caso de erro na criação do token
            throw new RuntimeException("Error while generating token",exception);
        }
    }

    // Método para validar um token JWT e retornar o assunto (email do usuário)
    public String validateToken(String token) {
        try {
            // Cria um algoritmo de assinatura HMAC com a mesma chave secreta
            Algorithm algorithm = Algorithm.HMAC256("senha-secreta");
            // Verifica o token e retorna o assunto (email do usuário)
            return JWT.require(algorithm)
                    .withIssuer("cardapio") // Verifica se o emissor é o esperado
                    .build()
                    .verify(token) // Verifica o token
                    .getSubject(); // Retorna o assunto do token
        } catch (JWTVerificationException exception) {
            // Retorna uma string vazia em caso de erro na verificação do token
            return "";
        }
    }

    // Método privado para gerar a data de expiração do token
    private Instant genExpirationDate() {
        // Define a data de expiração como 2 horas a partir do momento atual
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
