package com.banksimulator.dto.envoie;

import com.banksimulator.enums.TypePieceIdentite;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record PieceIdentiteRequestDTO(

        @NotNull TypePieceIdentite typePiece,
        @NotBlank String numero,
        @NotNull @Future LocalDate dateExpiration
) {}