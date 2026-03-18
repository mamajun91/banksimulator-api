package com.banksimulator.dto.envoie;

import com.banksimulator.enums.TypeTransaction;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

public record TransactionRequestDTO(

        @NotNull TypeTransaction typeTransaction,
        @NotNull @DecimalMin("0.01") BigDecimal montant,
        @NotBlank String devise,
        String libelle,
        UUID idCompteCrediteur,
        String ibanExterne
) {}
