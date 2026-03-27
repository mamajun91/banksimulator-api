package com.banksimulator.service.interfaces;

import com.banksimulator.dto.envoie.TransactionRequestDTO;
import com.banksimulator.dto.reponse.TransactionResponseDTO;
import com.banksimulator.entities.Transaction;

import java.util.List;
import java.util.UUID;

public interface ITransactionService {
    TransactionResponseDTO effectuerDepot(TransactionRequestDTO dto, UUID idCompte);
    TransactionResponseDTO effectuerRetrait(TransactionRequestDTO dto, UUID idCompte);
    TransactionResponseDTO effectuerVirement(TransactionRequestDTO dto, UUID idCompteDebiteur);
    TransactionResponseDTO annulerTransaction(UUID idTransaction);
    TransactionResponseDTO findById(UUID id);
    List<TransactionResponseDTO> findAll();
    List<TransactionResponseDTO> findByCompteDebiteurId(UUID idCompte);
    List<TransactionResponseDTO> findByCompteCrediteurId(UUID idCompte);
    List<TransactionResponseDTO> findByInitiateurId(UUID idUtilisateur);
}
