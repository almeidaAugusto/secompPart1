package com.example.cardapio.controller;

// Importa os DTOs necessários para autenticação e registro
import com.example.cardapio.domain.DTO.AuthenticationDTO;
import com.example.cardapio.domain.DTO.LoginResponseDTO;
import com.example.cardapio.domain.DTO.RegisterDTO;
// Importa a classe de domínio User
import com.example.cardapio.domain.user.User;
// Importa o repositório de usuários para acessar o banco de dados
import com.example.cardapio.repositories.UserRepository;
// Importa o serviço de token para geração e validação de tokens JWT
import com.example.cardapio.infra.security.TokenService;
// Importa classes do Spring para injeção de dependências e manipulação de requisições HTTP
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// Importa a validação de dados
import jakarta.validation.*;

// Declara a classe como um controlador REST
@RestController
@RequestMapping("auth") // Define o endpoint base como "auth"
public class AuthenticationController {
    // Injeta o gerenciador de autenticação do Spring
    @Autowired
    private AuthenticationManager authenticationManager;
    // Injeta o repositório de usuários para interagir com o banco de dados
    @Autowired
    private UserRepository userRepository;
    // Injeta o serviço de token para geração de tokens JWT
    @Autowired
    private TokenService tokenService;
    // Injeta o codificador de senhas configurado no SecurityConfigurations
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Método para autenticar um usuário e gerar um token JWT
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data){
        // Exibe a senha no console (não recomendado em produção, apenas para depuração)
        System.out.println(data.password());
        // Cria um objeto de autenticação com o email e senha fornecidos
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        // Autentica o usuário usando o gerenciador de autenticação
        var auth = this.authenticationManager.authenticate(usernamePassword);

        // Gera um token JWT para o usuário autenticado
        var token = tokenService.generateToken((User) auth.getPrincipal());

        // Retorna o token JWT no corpo da resposta
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    // Método para registrar um novo usuário
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data){
        // Verifica se o email já está cadastrado no banco de dados
        if(this.userRepository.findByEmail(data.email()) != null) return ResponseEntity.badRequest().build();

        // Criptografa a senha fornecida pelo usuário
        String encryptedPassword = passwordEncoder.encode(data.password());
        // Exibe a senha criptografada no console (não recomendado em produção, apenas para depuração)
        System.out.println(encryptedPassword);
        // Cria um novo objeto User com os dados fornecidos
        User newUser = new User(data.name(), data.email(), encryptedPassword, data.role());
        // Salva o novo usuário no banco de dados
        this.userRepository.save(newUser);

        // Retorna uma resposta de sucesso
        return ResponseEntity.ok().build();
    }
}
