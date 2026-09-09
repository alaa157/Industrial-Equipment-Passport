package com.industrial.auth.security;

import com.industrial.auth.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
private final UserRepository users;
public CustomUserDetailsService(UserRepository users){this.users=users;}
@Override
public UserDetails loadUserByUsername(String username)throws UsernameNotFoundException{
return users.findByUsernameIgnoreCaseOrEmailIgnoreCase(username,username)
.map(UserPrincipal::new)
.orElseThrow(()->new UsernameNotFoundException("Invalid credentials"));
}
}
