package com.banksimulator.dto.envoie;

import com.banksimulator.enums.TypeCompte;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public record CompteBancaireRequestDTO(

        @NotNull TypeCompte typeCompte,
        BigDecimal decouvertAutorise,
        BigDecimal tauxInteret,
        UUID idConseiller
) {}
