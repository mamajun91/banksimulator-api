package com.banksimulator.dto.reponse;

import com.banksimulator.enums.TypePieceIdentite;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record PieceIdentiteResponseDTO(

        UUID id,
        UUID utilisateurId,
        TypePieceIdentite typePiece,
        String numero,
        LocalDate dateExpiration,
        LocalDateTime dateCreation
) {}
