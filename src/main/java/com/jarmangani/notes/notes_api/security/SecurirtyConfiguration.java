package com.jarmangani.notes.notes_api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurirtyConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                        .antMatchers("/").permitAll()
                        .anyRequest().authenticated())
                        .formLogin((form) -> form
                                        .loginPage("/login")
                                        .permitAll())
                        .logout((logout) -> logout.permitAll());

        return http.build();
    }

        @Bean
        public PasswordEncoder encoder() {
                return new BCryptPasswordEncoder();
        }
}
