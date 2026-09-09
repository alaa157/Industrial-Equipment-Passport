package com.industrial.auth.security;

import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.JWKSet;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;

@Configuration
public class JwtConfig {
@Bean
SecretKey jwtSecretKey(@Value("${jwt.secret}") String secret){
if(secret==null||secret.length()<32) throw new IllegalStateException("JWT_SECRET must contain at least 32 characters");
return new javax.crypto.spec.SecretKeySpec(secret.getBytes(java.nio.charset.StandardCharsets.UTF_8),"HmacSHA256");
}

@Bean
JwtEncoder jwtEncoder(SecretKey key){
OctetSequenceKey jwk=new OctetSequenceKey.Builder(key.getEncoded()).keyID("iep-jwt").algorithm(com.nimbusds.jose.JWSAlgorithm.HS256).build();
return new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(jwk)));
}

@Bean
JwtDecoder jwtDecoder(SecretKey key){
return NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
}
}
