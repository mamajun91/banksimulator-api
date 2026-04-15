package com.banksimulator.service;

import com.banksimulator.dto.envoie.AuthRequestDTO;
import com.banksimulator.dto.envoie.UtilisateurRequestDTO;
import com.banksimulator.dto.reponse.AuthResponseDTO;
import com.banksimulator.dto.reponse.UtilisateurResponseDTO;
import com.banksimulator.entities.Utilisateur;
import com.banksimulator.enums.Role;
import com.banksimulator.enums.StatutUtilisateur;
import com.banksimulator.exception.CompteBloqueException;
import com.banksimulator.exception.EmailDejaUtiliseException;
import com.banksimulator.exception.MotDePasseDifferentException;
import com.banksimulator.exception.UtilisateurNotFoundException;
import com.banksimulator.mapper.PieceIdentiteMapper;
import com.banksimulator.mapper.UtilisateurMapper;
import com.banksimulator.repository.PieceIdentiteRepository;
import com.banksimulator.repository.UtilisateurRepository;
import com.banksimulator.service.impl.AuditTrailServiceImpl;
import com.banksimulator.service.impl.UtilisateurServiceImpl;
import io.quarkus.elytron.security.common.BcryptUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UtilisateurServiceImplTest {

    @Mock
    UtilisateurRepository utilisateurRepository;
    @Mock
    UtilisateurMapper utilisateurMapper;
    @Mock
    AuditTrailServiceImpl auditTrailService;
    @Mock
    PieceIdentiteRepository pieceIdentiteRepository;
    @Mock
    PieceIdentiteMapper pieceIdentiteMapper;

    @InjectMocks
    UtilisateurServiceImpl service;

    /** creer  */
    @Test
    void creer_emailDejaUtilise_leveEmailDejaUtiliseException() {
        /** Arrange  */
        UtilisateurRequestDTO dto = buildDto("test@email.com");
        when(utilisateurRepository.existsByEmail("test@email.com")).thenReturn(true);

        /** Act & Assert  */
        assertThrows(EmailDejaUtiliseException.class, () -> service.creer(dto));
        verify(utilisateurRepository, never()).persist(any(Utilisateur.class));
    }

    @Test
    void creer_emailDisponible_retourneUtilisateurResponseDTO() {
        // Arrange
        UtilisateurRequestDTO dto = buildDto("nouveau@email.com");
        Utilisateur utilisateur = new Utilisateur();
        UtilisateurResponseDTO responseDTO = mock(UtilisateurResponseDTO.class);

        when(utilisateurRepository.existsByEmail("nouveau@email.com")).thenReturn(false);
        when(utilisateurMapper.toEntity(dto)).thenReturn(utilisateur);
        when(utilisateurMapper.toDTO(utilisateur)).thenReturn(responseDTO);

        /** Act  */
        UtilisateurResponseDTO result = service.creer(dto);

        /** Assert  */
        assertNotNull(result);
        verify(utilisateurRepository).persist(utilisateur);
    }

    /** authentifier */
    @Test
    void authentifier_utilisateurInexistant_leveUtilisateurNotFoundException() {
        /** Arrange  */
        AuthRequestDTO dto = new AuthRequestDTO("inconnu@email.com", "motdepasse");
        when(utilisateurRepository.findByEmail("inconnu@email.com"))
                .thenReturn(Optional.empty());

        /** Act & Assert  */
        assertThrows(UtilisateurNotFoundException.class, () -> service.authentifier(dto));
    }

    @Test
    void authentifier_mauvaisMotDePasse_leveMdpDifferentException() {
        /** Arrange  */
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setMotDePasseHash(BcryptUtil.bcryptHash("bonMotDePasse"));
        utilisateur.setNbEchecsAuth(0);

        AuthRequestDTO dto = new AuthRequestDTO("test@email.com", "mauvaisMotDePasse");
        when(utilisateurRepository.findByEmail("test@email.com"))
                .thenReturn(Optional.of(utilisateur));

        /** Act & Assert  */
        assertThrows(MotDePasseDifferentException.class, () -> service.authentifier(dto));
        assertEquals(1, utilisateur.getNbEchecsAuth());
    }

    @Test
    void authentifier_troisEchecs_leveCompteBloqueException() {
        /**  Arrange  */
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setMotDePasseHash(BcryptUtil.bcryptHash("bonMotDePasse"));
        utilisateur.setNbEchecsAuth(2);

        AuthRequestDTO dto = new AuthRequestDTO("test@email.com", "mauvaisMotDePasse");
        when(utilisateurRepository.findByEmail("test@email.com"))
                .thenReturn(Optional.of(utilisateur));

        /** Act & Assert */
        assertThrows(CompteBloqueException.class, () -> service.authentifier(dto));
        assertEquals(StatutUtilisateur.BLOQUE, utilisateur.getStatut());
    }

    @Test
    void authentifier_credentialsValides_retourneAuthResponseDTO() {
        /** Arrange */
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setMotDePasseHash(BcryptUtil.bcryptHash("bonMotDePasse"));
        utilisateur.setNbEchecsAuth(1);
        utilisateur.setRole(Role.CLIENT);
        utilisateur.setEmail("test@email.com");

        AuthRequestDTO dto = new AuthRequestDTO("test@email.com", "bonMotDePasse");
        when(utilisateurRepository.findByEmail("test@email.com"))
                .thenReturn(Optional.of(utilisateur));

        /** Act  */
        AuthResponseDTO result = service.authentifier(dto);

        /** Assert */
        assertNotNull(result.accessToken());
        assertEquals("Bearer", result.tokenType());
        assertEquals(0, utilisateur.getNbEchecsAuth());
    }

    /**  helpers  */
    private UtilisateurRequestDTO buildDto(String email) {
        return new UtilisateurRequestDTO(
                "Doe", "John", email, "0600000000",
                LocalDate.of(1990, 1, 1),
                "1 rue Test", "Paris", "75000", "FR",
                "motDePasse123", Role.CLIENT
        );
    }
}