package com.ecommerceapp.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain springSecurityConfiguration(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests()
                .antMatchers(HttpMethod.POST, "/customer/create").permitAll()
                .antMatchers(HttpMethod.PUT, "/product/update/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/product/create").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/order/get/all").hasRole("ADMIN")
                .anyRequest()
                .authenticated().and().csrf().disable().formLogin().and().httpBasic();

        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }

}
