package com.example.electricitymanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http){

        http.authorizeHttpRequests(
                auth ->
                        auth
//                                .requestMatchers("/api/v1/users/register","/api/v1/login").permitAll()
//                                .requestMatchers("/api/v1/tickets/**").hasRole("USER")
//                                .requestMatchers(HttpMethod.GET,"/api/v1/matches/**").hasAnyRole("USER","ADMIN")
//                                .requestMatchers("/api/v1/admin/register",
//                                        "/api/v1/standiums",
//                                        "/api/v1/matches/**").hasRole("ADMIN")
                                .anyRequest().permitAll()
//                                .anyRequest().authenticated()

        );

        http.csrf(csrf -> csrf.disable());
        http.httpBasic(Customizer.withDefaults());

//        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
