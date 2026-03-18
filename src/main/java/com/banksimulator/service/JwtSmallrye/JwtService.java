package com.banksimulator.service.JwtSmallrye;

import com.banksimulator.enums.Role;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.util.UUID;

@ApplicationScoped
public class JwtService {

    private static final String ISSUER = "banksimulator";
    private static final long EXPIRATION_HOURS = 24;

    public String genererToken(UUID userId, Role role) {
        return Jwt.issuer(ISSUER)
                .subject(userId.toString())
                .groups(role.name())
                .expiresIn(Duration.ofHours(EXPIRATION_HOURS))
                .sign();
    }
}
