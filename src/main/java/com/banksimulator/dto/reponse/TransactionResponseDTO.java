package com.banksimulator.dto.reponse;

import com.banksimulator.enums.StatutTransaction;
import com.banksimulator.enums.TypeTransaction;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponseDTO(

        UUID id,
        UUID compteDebiteurId,
        UUID compteCrediteurId,
        UUID initiateurId,
        String reference,
        TypeTransaction typeTransaction,
        BigDecimal montant,
        String devise,
        String libelle,
        StatutTransaction statut,
        String ibanExterne,
        LocalDateTime dateExecution,
        LocalDate dateValeur,
        String messageErreur
) {}