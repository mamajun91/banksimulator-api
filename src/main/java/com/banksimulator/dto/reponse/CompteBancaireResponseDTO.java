package com.banksimulator.dto.reponse;

import com.banksimulator.enums.StatutCompte;
import com.banksimulator.enums.TypeCompte;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record CompteBancaireResponseDTO(

        UUID id,
        UUID titulaireId,
        UUID conseillerId,
        String iban,
        String bic,
        TypeCompte typeCompte,
        BigDecimal solde,
        String devise,
        BigDecimal decouvertAutorise,
        BigDecimal tauxInteret,
        StatutCompte statut,
        LocalDate dateOuverture,
        LocalDate dateCloture,
        LocalDateTime dateModification
) {}