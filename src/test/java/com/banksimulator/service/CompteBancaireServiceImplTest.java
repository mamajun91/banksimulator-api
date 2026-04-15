package com.banksimulator.service;

import com.banksimulator.dto.envoie.CompteBancaireRequestDTO;
import com.banksimulator.dto.reponse.CompteBancaireResponseDTO;
import com.banksimulator.entities.CompteBancaire;
import com.banksimulator.entities.Utilisateur;
import com.banksimulator.enums.StatutCompte;
import com.banksimulator.enums.TypeCompte;
import com.banksimulator.exception.CompteNonActifException;
import com.banksimulator.exception.CompteNotFoundException;
import com.banksimulator.exception.SoldeInsuffisantException;
import com.banksimulator.exception.UtilisateurNotFoundException;
import com.banksimulator.mapper.CompteBancaireMapper;
import com.banksimulator.repository.CompteBancaireRepository;
import com.banksimulator.repository.UtilisateurRepository;
import com.banksimulator.service.impl.AuditTrailServiceImpl;
import com.banksimulator.service.impl.CompteBancaireServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompteBancaireServiceImplTest {

    @Mock
    CompteBancaireRepository compteRepo;
    @Mock
    UtilisateurRepository utilisateurRepo;
    @Mock
    CompteBancaireMapper compteMapper;
    @Mock
    AuditTrailServiceImpl auditTrailService;

    @InjectMocks
    CompteBancaireServiceImpl service;

    /** creer */
    @Test
    void creer_titulaireInexistant_leveUtilisateurNotFoundException() {
        /** Arrange */
        UUID idTitulaire = UUID.randomUUID();
        CompteBancaireRequestDTO dto = buildDto(null);
        when(utilisateurRepo.findByIdOptional(idTitulaire)).thenReturn(Optional.empty());

        /** Act & Assert */
        assertThrows(UtilisateurNotFoundException.class, () -> service.creer(dto, idTitulaire));
        verify(compteRepo, never()).persist(any(CompteBancaire.class));
    }

    @Test
    void creer_titulaireExistant_retourneCompteBancaireResponseDTO() {
        /** Arrange */
        UUID idTitulaire = UUID.randomUUID();
        Utilisateur titulaire = new Utilisateur();
        CompteBancaireRequestDTO dto = buildDto(null);
        CompteBancaire compte = new CompteBancaire();
        CompteBancaireResponseDTO responseDTO = mock(CompteBancaireResponseDTO.class);

        when(utilisateurRepo.findByIdOptional(idTitulaire)).thenReturn(Optional.of(titulaire));
        when(compteRepo.existsByIban(anyString())).thenReturn(false);
        when(compteMapper.toEntity(dto)).thenReturn(compte);
        when(compteMapper.toDTO(compte)).thenReturn(responseDTO);

        /** Act */
        CompteBancaireResponseDTO result = service.creer(dto, idTitulaire);

        /** Assert */
        assertNotNull(result);
        verify(compteRepo).persist(compte);
    }

    /** bloquer */
    @Test
    void bloquer_compteInexistant_leveCompteNotFoundException() {
        /** Arrange */
        UUID id = UUID.randomUUID();
        when(compteRepo.findByIdOptional(id)).thenReturn(Optional.empty());

        /** Act & Assert */
        assertThrows(CompteNotFoundException.class, () -> service.bloquer(id));
    }

    @Test
    void bloquer_compteDejaBloque_leveCompteNonActifException() {
        /** Arrange */
        UUID id = UUID.randomUUID();
        CompteBancaire compte = buildCompte(StatutCompte.BLOQUE);
        when(compteRepo.findByIdOptional(id)).thenReturn(Optional.of(compte));

        /** Act & Assert */
        assertThrows(CompteNonActifException.class, () -> service.bloquer(id));
    }

    @Test
    void bloquer_compteActif_retourneCompteBloqueDTO() {
        /** Arrange */
        UUID id = UUID.randomUUID();
        CompteBancaire compte = buildCompte(StatutCompte.ACTIF);
        when(compteRepo.findByIdOptional(id)).thenReturn(Optional.of(compte));
        when(compteMapper.toDTO(compte)).thenReturn(mock(CompteBancaireResponseDTO.class));

        /** Act */
        service.bloquer(id);

        /** Assert */
        assertEquals(StatutCompte.BLOQUE, compte.getStatut());
    }

    /** cloturer */
    @Test
    void cloturer_soldeNonNul_leveSoldeInsuffisantException() {
        /** Arrange */
        UUID id = UUID.randomUUID();
        CompteBancaire compte = buildCompte(StatutCompte.ACTIF);
        compte.setSolde(BigDecimal.valueOf(100));
        when(compteRepo.findByIdOptional(id)).thenReturn(Optional.of(compte));

        /** Act & Assert */
        assertThrows(SoldeInsuffisantException.class, () -> service.cloturer(id));
    }

    @Test
    void cloturer_soldeNul_retourneCompteCltureDTO() {
        /** Arrange */
        UUID id = UUID.randomUUID();
        CompteBancaire compte = buildCompte(StatutCompte.ACTIF);
        compte.setSolde(BigDecimal.ZERO);
        when(compteRepo.findByIdOptional(id)).thenReturn(Optional.of(compte));
        when(compteMapper.toDTO(compte)).thenReturn(mock(CompteBancaireResponseDTO.class));

        /** Act */
        service.cloturer(id);

        /** Assert */
        assertEquals(StatutCompte.CLOTURE, compte.getStatut());
        assertNotNull(compte.getDateCloture());
    }

    /** helpers */
    private CompteBancaireRequestDTO buildDto(UUID idConseiller) {
        return new CompteBancaireRequestDTO(TypeCompte.COURANT, null, null, idConseiller);
    }

    private CompteBancaire buildCompte(StatutCompte statut) {
        CompteBancaire compte = new CompteBancaire();
        Utilisateur titulaire = new Utilisateur();
        titulaire.setId(UUID.randomUUID());
        compte.setTitulaire(titulaire);
        compte.setStatut(statut);
        compte.setSolde(BigDecimal.ZERO);
        return compte;
    }
}