package com.example.cardapio.infra.security;

// Importações necessárias para configuração de segurança
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Classe de configuração para personalizar a segurança da aplicação
@Configuration // Indica que esta classe contém definições de beans para o Spring
@EnableWebSecurity // Habilita a configuração personalizada de segurança web
public class SecurityConfigurations {

    // Injeta o filtro de segurança personalizado
    @Autowired
    SecurityFilter securityFilter;

    // Define a cadeia de filtros de segurança
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf(csrf -> csrf.disable()) // Desabilita a proteção contra CSRF
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Define a política de sessão como stateless
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll() // Permite acesso público ao endpoint de login
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll() // Permite acesso público ao endpoint de registro
                        .requestMatchers(HttpMethod.POST, "/food").permitAll() // Permite acesso público ao endpoint de criação de comida
                        .requestMatchers(HttpMethod.POST, "/food").hasRole("ADMIN") // Restringe o acesso ao endpoint de criação de comida para usuários com o papel ADMIN
                        .anyRequest().authenticated() // Exige autenticação para qualquer outra requisição
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) // Adiciona o filtro personalizado antes do filtro padrão de autenticação
                .build(); // Constrói a cadeia de filtros
    }

    // Define o gerenciador de autenticação
    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager(); // Retorna o gerenciador de autenticação configurado
    }

    // Define o codificador de senhas
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(); // Retorna uma instância do codificador BCrypt para criptografar senhas
    }
}
