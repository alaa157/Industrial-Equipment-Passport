package com.industrial.auth.dto;

import java.util.List;
import java.util.UUID;

public record AuthResponse(
String accessToken,
String refreshToken,
long expiresInSeconds,
UUID userId,
String username,
List<String> roles
){}
