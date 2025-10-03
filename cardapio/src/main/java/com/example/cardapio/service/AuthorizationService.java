package com.example.cardapio.service;

// Importa o repositório de usuários para acessar os dados do banco
import com.example.cardapio.repositories.UserRepository;
// Importa as anotações e classes necessárias para o serviço de autenticação
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Declara a classe como um serviço gerenciado pelo Spring
@Service
public class AuthorizationService implements UserDetailsService {

    // Injeta o repositório de usuários para uso na classe
    @Autowired
    UserRepository userRepository;

    // Sobrescreve o método para carregar um usuário pelo nome de usuário (email neste caso)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca o usuário pelo email no repositório e retorna os detalhes do usuário
        return userRepository.findByEmail(username);
    }
}
