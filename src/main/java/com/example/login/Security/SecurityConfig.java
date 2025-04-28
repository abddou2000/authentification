package com.example.login.Security;

import com.example.login.Models.Role;
import com.example.login.Repositories.EmployeSimpleRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final EmployeSimpleRepository employeSimpleRepository;

    public SecurityConfig(EmployeSimpleRepository employeSimpleRepository) {
        this.employeSimpleRepository = employeSimpleRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/login", "/api/register").permitAll()  // Permet l'accès sans authentification
                        .anyRequest().authenticated()  // Nécessite une authentification pour les autres URLs
                )
                .csrf(AbstractHttpConfigurer::disable); // Désactive la protection CSRF pour les API

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> employeSimpleRepository.findByEmailPro(email)
                .or(() -> employeSimpleRepository.findByEmailPerso(email))
                .map(employeSimple -> {
                    // Récupérez le rôle de l'employé
                    Role role = employeSimple.getRole();
                    String roleName = role != null ? role.getNomRole() : "USER"; // Par défaut "USER" si aucun rôle trouvé

                    return User.builder()
                            .username(employeSimple.getEmailPro())
                            .password(employeSimple.getMotDePasse()) // Mot de passe en texte clair avec {noop}
                            .roles(roleName) // Affectez le rôle de l'utilisateur
                            .build();
                })
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }



    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        // Ne pas utiliser de encodeur de mot de passe, puisqu'on utilise {noop} pour les mots de passe en texte clair
        authenticationManagerBuilder.userDetailsService(userDetailsService());
        return authenticationManagerBuilder.build();
    }
}
