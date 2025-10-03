package com.example.cardapio.infra.security;

// Importa o repositório de usuários para acessar os dados do banco
import com.example.cardapio.repositories.UserRepository;
// Importa classes para manipulação de filtros e requisições HTTP
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
// Importa classes do Spring para autenticação e segurança
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Declara a classe como um componente gerenciado pelo Spring
@Component
public class SecurityFilter extends OncePerRequestFilter {

    // Injeta o serviço de token para validação de tokens JWT
    @Autowired
    TokenService tokenService;
    // Injeta o repositório de usuários para buscar informações no banco
    @Autowired
    UserRepository userRepository;

    // Método que intercepta todas as requisições para aplicar o filtro de segurança
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Recupera o token da requisição
        var token = this.recoverToken(request);
        // Verifica se o token não é nulo ou vazio
        if (token != null && !token.isEmpty()) {
            // Valida o token e obtém o login do usuário
            var login = tokenService.validateToken(token);
            // Busca os detalhes do usuário no banco pelo login
            UserDetails user = userRepository.findByEmail(login);

            // Verifica se o usuário existe antes de autenticar
            if (user != null) {
                System.out.println("Usuário nulo"); // Log para depuração
                // Cria um objeto de autenticação com as credenciais do usuário
                var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                // Define o contexto de segurança com o usuário autenticado
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        // Chama o próximo filtro na cadeia de filtros
        filterChain.doFilter(request, response);
    }

    // Método privado para recuperar o token do cabeçalho da requisição
    private String recoverToken(HttpServletRequest request) {
        // Obtém o cabeçalho "Authorization" da requisição
        var authHeader = request.getHeader("Authorization");
        // Verifica se o cabeçalho é nulo ou não começa com "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ""; // Retorna uma string vazia se o cabeçalho for inválido
        }
        // Remove o prefixo "Bearer " e retorna o token
        return authHeader.replace("Bearer ", "");
    }
}
