package com.banksimulator.dto.reponse;

import com.banksimulator.enums.Role;

import java.util.UUID;

public record AuthResponseDTO(
        String accessToken,
        String tokenType,
        Long expiresIn,
        UUID utilisateurId,
        Role role
) {}
