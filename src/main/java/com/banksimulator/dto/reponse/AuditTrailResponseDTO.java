package com.banksimulator.dto.reponse;

import com.banksimulator.enums.EntiteCible;

import java.time.LocalDateTime;
import java.util.UUID;

public record AuditTrailResponseDTO(
        UUID id,
        UUID utilisateurId,
        String actionCode,
        EntiteCible entiteCible,
        UUID entiteId,
        LocalDateTime timestamp
) {}
