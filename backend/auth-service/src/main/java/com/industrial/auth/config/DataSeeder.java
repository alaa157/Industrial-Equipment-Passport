package com.industrial.auth.config;

import com.industrial.auth.entity.Role;
import com.industrial.auth.entity.User;
import com.industrial.auth.repository.UserRepository;
import java.util.Set;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("development")
public class DataSeeder {
@Bean
CommandLineRunner seedAdmin(UserRepository users,PasswordEncoder encoder){
return args->{
if(!users.existsByUsernameIgnoreCase("admin")){
users.save(new User(
"admin",
"admin@industrial.local",
encoder.encode("Admin123!ChangeMe"),
Set.of(Role.ADMIN,Role.MANAGER)
));
}
};
}
}
