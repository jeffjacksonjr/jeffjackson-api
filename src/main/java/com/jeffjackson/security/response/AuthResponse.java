package com.jeffjackson.security.response;

public record AuthResponse(String token, String expiration) {}