package com.banksimulator.service.impl;

import com.banksimulator.dto.envoie.TransactionRequestDTO;
import com.banksimulator.dto.reponse.TransactionResponseDTO;
import com.banksimulator.entities.CompteBancaire;
import com.banksimulator.entities.Transaction;
import com.banksimulator.enums.EntiteCible;
import com.banksimulator.enums.StatutCompte;
import com.banksimulator.enums.StatutTransaction;
import com.banksimulator.enums.TypeTransaction;
import com.banksimulator.exception.*;
import com.banksimulator.mapper.TransactionMapper;
import com.banksimulator.repository.CompteBancaireRepository;
import com.banksimulator.repository.TransactionRepository;
import com.banksimulator.service.interfaces.ITransactionService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class TransactionServiceImpl implements ITransactionService {

    @Inject CompteBancaireRepository compteRepo;
    @Inject TransactionRepository transactionRepo;
    @Inject TransactionMapper transactionMapper;
    @Inject AuditTrailServiceImpl auditTrailService;

    @Override
    @Transactional
    public TransactionResponseDTO effectuerDepot(TransactionRequestDTO dto, UUID idCompte) {
        CompteBancaire compte = compteRepo.findByIdOptional(idCompte)
                .orElseThrow(CompteNotFoundException::new);
        if (compte.getStatut() != StatutCompte.ACTIF)
            throw new CompteNonActifException();

        Transaction transaction = new Transaction(
                null, TypeTransaction.DEPOT, dto.montant(), genererReference());
        transaction.setCompteCrediteur(compte);
        transaction.setLibelle(dto.libelle());
        transaction.setIbanExterne(dto.ibanExterne());
        transaction.setDevise(dto.devise());
        transaction.setStatut(StatutTransaction.COMPLETED);
        transactionRepo.persist(transaction);

        compte.setSolde(compte.getSolde().add(dto.montant()));

        auditTrailService.tracer(compte.getTitulaire().getId(),
                "ACTION_EFFECTUER_DEPOT", EntiteCible.TRANSACTION, transaction.getId());
        return transactionMapper.toDTO(transaction);
    }

    @Override
    @Transactional
    public TransactionResponseDTO effectuerRetrait(TransactionRequestDTO dto, UUID idCompte) {
        CompteBancaire compte = compteRepo.findByIdOptional(idCompte)
                .orElseThrow(CompteNotFoundException::new);
        if (compte.getStatut() != StatutCompte.ACTIF)
            throw new CompteNonActifException();

        BigDecimal soldeEffectif = compte.getSolde()
                .add(compte.getDecouvertAutorise() != null ? compte.getDecouvertAutorise() : BigDecimal.ZERO);
        if (dto.montant().compareTo(soldeEffectif) > 0)
            throw new SoldeInsuffisantException();

        Transaction transaction = new Transaction(
                null, TypeTransaction.RETRAIT, dto.montant(), genererReference());
        transaction.setCompteDebiteur(compte);
        transaction.setLibelle(dto.libelle());
        transaction.setIbanExterne(dto.ibanExterne());
        transaction.setDevise(dto.devise());
        transaction.setStatut(StatutTransaction.COMPLETED);
        transactionRepo.persist(transaction);

        compte.setSolde(compte.getSolde().subtract(dto.montant()));

        auditTrailService.tracer(compte.getTitulaire().getId(),
                "ACTION_EFFECTUER_RETRAIT", EntiteCible.TRANSACTION, transaction.getId());
        return transactionMapper.toDTO(transaction);
    }

    @Override
    @Transactional
    public TransactionResponseDTO effectuerVirement(TransactionRequestDTO dto, UUID idCompteDebiteur) {
        CompteBancaire debiteur = compteRepo.findByIdOptional(idCompteDebiteur)
                .orElseThrow(CompteNotFoundException::new);
        if (debiteur.getStatut() != StatutCompte.ACTIF)
            throw new CompteNonActifException();

        CompteBancaire crediteur = compteRepo.findByIdOptional(dto.idCompteCrediteur())
                .orElseThrow(CompteNotFoundException::new);
        if (crediteur.getStatut() != StatutCompte.ACTIF)
            throw new CompteNonActifException();

        BigDecimal soldeEffectif = debiteur.getSolde()
                .add(debiteur.getDecouvertAutorise() != null ? debiteur.getDecouvertAutorise() : BigDecimal.ZERO);
        if (dto.montant().compareTo(soldeEffectif) > 0)
            throw new SoldeInsuffisantException();

        Transaction transaction = new Transaction(
                null, TypeTransaction.VIREMENT, dto.montant(), genererReference());
        transaction.setCompteDebiteur(debiteur);
        transaction.setCompteCrediteur(crediteur);
        transaction.setLibelle(dto.libelle());
        transaction.setDevise(dto.devise());
        transaction.setStatut(StatutTransaction.COMPLETED);
        transactionRepo.persist(transaction);

        debiteur.setSolde(debiteur.getSolde().subtract(dto.montant()));
        crediteur.setSolde(crediteur.getSolde().add(dto.montant()));

        auditTrailService.tracer(debiteur.getTitulaire().getId(),
                "ACTION_EFFECTUER_VIREMENT", EntiteCible.TRANSACTION, transaction.getId());
        return transactionMapper.toDTO(transaction);
    }

    @Override
    @Transactional
    public TransactionResponseDTO annulerTransaction(UUID idTransaction) {
        Transaction transaction = transactionRepo.findByIdOptional(idTransaction)
                .orElseThrow(TransactionNotFoundException::new);
        if (transaction.getStatut() != StatutTransaction.PENDING)
            throw new TransactionImmutableException();

        transaction.setStatut(StatutTransaction.CANCELLED);

        auditTrailService.tracer(transaction.getInitiateur().getId(),
                "ACTION_ANNULER_TRANSACTION", EntiteCible.TRANSACTION, idTransaction);
        return transactionMapper.toDTO(transaction);
    }

    @Override
    public TransactionResponseDTO findById(UUID id) {
        return transactionMapper.toDTO(transactionRepo.findByIdOptional(id)
                .orElseThrow(TransactionNotFoundException::new));
    }

    @Override
    public List<TransactionResponseDTO> findAll() {
        return transactionRepo.listAll().stream()
                .map(transactionMapper::toDTO)
                .toList();
    }

    @Override
    public List<TransactionResponseDTO> findByCompteDebiteurId(UUID idCompte) {
        return transactionRepo.findByCompteDebiteurId(idCompte).stream()
                .map(transactionMapper::toDTO)
                .toList();
    }

    @Override
    public List<TransactionResponseDTO> findByCompteCrediteurId(UUID idCompte) {
        return transactionRepo.findByCompteCrediteurId(idCompte).stream()
                .map(transactionMapper::toDTO)
                .toList();
    }

    @Override
    public List<TransactionResponseDTO> findByInitiateurId(UUID idUtilisateur) {
        return transactionRepo.findByInitiateurId(idUtilisateur).stream()
                .map(transactionMapper::toDTO)
                .toList();
    }

    private String genererReference() {
        String ref;
        do {
            ref = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (transactionRepo.existsByReference(ref));
        return ref;
    }
}

