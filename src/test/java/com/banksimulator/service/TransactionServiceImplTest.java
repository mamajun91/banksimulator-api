package com.banksimulator.service;

import com.banksimulator.dto.envoie.TransactionRequestDTO;
import com.banksimulator.dto.reponse.TransactionResponseDTO;
import com.banksimulator.entities.CompteBancaire;
import com.banksimulator.entities.Transaction;
import com.banksimulator.entities.Utilisateur;
import com.banksimulator.enums.StatutCompte;
import com.banksimulator.enums.StatutTransaction;
import com.banksimulator.exception.*;
import com.banksimulator.mapper.TransactionMapper;
import com.banksimulator.repository.CompteBancaireRepository;
import com.banksimulator.repository.TransactionRepository;
import com.banksimulator.service.impl.AuditTrailServiceImpl;
import com.banksimulator.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    CompteBancaireRepository compteRepo;
    @Mock
    TransactionRepository transactionRepo;
    @Mock
    TransactionMapper transactionMapper;
    @Mock
    AuditTrailServiceImpl auditTrailService;

    @InjectMocks
    TransactionServiceImpl service;

    /** effectuerDepot */
    @Test
    void effectuerDepot_compteInexistant_leveCompteNotFoundException() {
        /** Arrange */
        UUID idCompte = UUID.randomUUID();
        when(compteRepo.findByIdOptional(idCompte)).thenReturn(Optional.empty());

        /** Act & Assert */
        assertThrows(CompteNotFoundException.class,
                () -> service.effectuerDepot(buildDto(BigDecimal.valueOf(100)), idCompte));
    }

    @Test
    void effectuerDepot_compteNonActif_leveCompteNonActifException() {
        /** Arrange */
        UUID idCompte = UUID.randomUUID();
        CompteBancaire compte = buildCompte(StatutCompte.BLOQUE, BigDecimal.valueOf(500));
        when(compteRepo.findByIdOptional(idCompte)).thenReturn(Optional.of(compte));

        /** Act & Assert */
        assertThrows(CompteNonActifException.class,
                () -> service.effectuerDepot(buildDto(BigDecimal.valueOf(100)), idCompte));
    }

    @Test
    void effectuerDepot_compteActif_soldeCredite() {
        /** Arrange */
        UUID idCompte = UUID.randomUUID();
        CompteBancaire compte = buildCompte(StatutCompte.ACTIF, BigDecimal.valueOf(500));
        when(compteRepo.findByIdOptional(idCompte)).thenReturn(Optional.of(compte));
        when(transactionRepo.existsByReference(anyString())).thenReturn(false);
        when(transactionMapper.toDTO(any())).thenReturn(mock(TransactionResponseDTO.class));

        /** Act */
        service.effectuerDepot(buildDto(BigDecimal.valueOf(100)), idCompte);

        /** Assert */
        assertEquals(BigDecimal.valueOf(600), compte.getSolde());
    }

    /** effectuerRetrait */
    @Test
    void effectuerRetrait_soldeInsuffisant_leveSoldeInsuffisantException() {
        /** Arrange */
        UUID idCompte = UUID.randomUUID();
        CompteBancaire compte = buildCompte(StatutCompte.ACTIF, BigDecimal.valueOf(50));
        when(compteRepo.findByIdOptional(idCompte)).thenReturn(Optional.of(compte));

        /** Act & Assert */
        assertThrows(SoldeInsuffisantException.class,
                () -> service.effectuerRetrait(buildDto(BigDecimal.valueOf(100)), idCompte));
    }

    @Test
    void effectuerRetrait_soldesSuffisant_soldeDebite() {
        /** Arrange */
        UUID idCompte = UUID.randomUUID();
        CompteBancaire compte = buildCompte(StatutCompte.ACTIF, BigDecimal.valueOf(500));
        when(compteRepo.findByIdOptional(idCompte)).thenReturn(Optional.of(compte));
        when(transactionRepo.existsByReference(anyString())).thenReturn(false);
        when(transactionMapper.toDTO(any())).thenReturn(mock(TransactionResponseDTO.class));

        /** Act */
        service.effectuerRetrait(buildDto(BigDecimal.valueOf(100)), idCompte);

        /** Assert */
        assertEquals(BigDecimal.valueOf(400), compte.getSolde());
    }

    /** effectuerVirement */
    @Test
    void effectuerVirement_debiteurInexistant_leveCompteNotFoundException() {
        /** Arrange */
        UUID idDebiteur = UUID.randomUUID();
        when(compteRepo.findByIdOptional(idDebiteur)).thenReturn(Optional.empty());

        /** Act & Assert */
        assertThrows(CompteNotFoundException.class,
                () -> service.effectuerVirement(buildDtoVirement(BigDecimal.valueOf(100)), idDebiteur));
    }

    @Test
    void effectuerVirement_soldeInsuffisant_leveSoldeInsuffisantException() {
        /** Arrange */
        UUID idDebiteur = UUID.randomUUID();
        UUID idCrediteur = UUID.randomUUID();
        CompteBancaire debiteur = buildCompte(StatutCompte.ACTIF, BigDecimal.valueOf(50));
        CompteBancaire crediteur = buildCompte(StatutCompte.ACTIF, BigDecimal.valueOf(0));

        when(compteRepo.findByIdOptional(idDebiteur)).thenReturn(Optional.of(debiteur));
        when(compteRepo.findByIdOptional(idCrediteur)).thenReturn(Optional.of(crediteur));

        /** Act & Assert */
        assertThrows(SoldeInsuffisantException.class,
                () -> service.effectuerVirement(buildDtoVirement(BigDecimal.valueOf(100), idCrediteur), idDebiteur));
    }

    @Test
    void effectuerVirement_nominal_soldesDebitesEtCredites() {
        /** Arrange */
        UUID idDebiteur = UUID.randomUUID();
        UUID idCrediteur = UUID.randomUUID();
        CompteBancaire debiteur = buildCompte(StatutCompte.ACTIF, BigDecimal.valueOf(500));
        CompteBancaire crediteur = buildCompte(StatutCompte.ACTIF, BigDecimal.valueOf(100));

        when(compteRepo.findByIdOptional(idDebiteur)).thenReturn(Optional.of(debiteur));
        when(compteRepo.findByIdOptional(idCrediteur)).thenReturn(Optional.of(crediteur));
        when(transactionRepo.existsByReference(anyString())).thenReturn(false);
        when(transactionMapper.toDTO(any())).thenReturn(mock(TransactionResponseDTO.class));

        /** Act */
        service.effectuerVirement(buildDtoVirement(BigDecimal.valueOf(100), idCrediteur), idDebiteur);

        /** Assert */
        assertEquals(BigDecimal.valueOf(400), debiteur.getSolde());
        assertEquals(BigDecimal.valueOf(200), crediteur.getSolde());
    }

    /** annulerTransaction */
    @Test
    void annulerTransaction_transactionInexistante_leveTransactionNotFoundException() {
        /** Arrange */
        UUID id = UUID.randomUUID();
        when(transactionRepo.findByIdOptional(id)).thenReturn(Optional.empty());

        /** Act & Assert */
        assertThrows(TransactionNotFoundException.class, () -> service.annulerTransaction(id));
    }

    @Test
    void annulerTransaction_statutNonPending_leveTransactionImmutableException() {
        /** Arrange */
        UUID id = UUID.randomUUID();
        Transaction transaction = new Transaction();
        transaction.setStatut(StatutTransaction.COMPLETED);
        when(transactionRepo.findByIdOptional(id)).thenReturn(Optional.of(transaction));

        /** Act & Assert */
        assertThrows(TransactionImmutableException.class, () -> service.annulerTransaction(id));
    }

    @Test
    void annulerTransaction_statutPending_transactionAnnulee() {
        /** Arrange */
        UUID id = UUID.randomUUID();
        Transaction transaction = new Transaction();
        transaction.setStatut(StatutTransaction.PENDING);
        Utilisateur initiateur = new Utilisateur();
        initiateur.setId(UUID.randomUUID());
        transaction.setInitiateur(initiateur);
        when(transactionRepo.findByIdOptional(id)).thenReturn(Optional.of(transaction));
        when(transactionMapper.toDTO(transaction)).thenReturn(mock(TransactionResponseDTO.class));

        /** Act */
        service.annulerTransaction(id);

        /** Assert */
        assertEquals(StatutTransaction.CANCELLED, transaction.getStatut());
    }

    /** helpers */
    private CompteBancaire buildCompte(StatutCompte statut, BigDecimal solde) {
        CompteBancaire compte = new CompteBancaire();
        Utilisateur titulaire = new Utilisateur();
        titulaire.setId(UUID.randomUUID());
        titulaire.setEmail("test@email.com");
        compte.setTitulaire(titulaire);
        compte.setStatut(statut);
        compte.setSolde(solde);
        return compte;
    }

    private TransactionRequestDTO buildDto(BigDecimal montant) {
        return new TransactionRequestDTO(null, montant, "EUR", null, null, null);
    }

    private TransactionRequestDTO buildDtoVirement(BigDecimal montant) {
        return new TransactionRequestDTO(null, montant, "EUR", null, UUID.randomUUID(), null);
    }

    private TransactionRequestDTO buildDtoVirement(BigDecimal montant, UUID idCrediteur) {
        return new TransactionRequestDTO(null, montant, "EUR", null, idCrediteur, null);
    }
}
