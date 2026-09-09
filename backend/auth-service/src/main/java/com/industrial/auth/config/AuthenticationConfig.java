package com.industrial.auth.config;

import com.industrial.auth.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthenticationConfig {
@Bean
AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)throws Exception{return configuration.getAuthenticationManager();}

@Bean
org.springframework.security.authentication.dao.DaoAuthenticationProvider authenticationProvider(CustomUserDetailsService service,PasswordEncoder encoder){
var provider=new org.springframework.security.authentication.dao.DaoAuthenticationProvider(service);
provider.setPasswordEncoder(encoder);
return provider;
}
}
