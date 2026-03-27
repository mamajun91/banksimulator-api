package com.banksimulator.service.impl;

import com.banksimulator.dto.envoie.AuthRequestDTO;
import com.banksimulator.dto.envoie.PieceIdentiteRequestDTO;
import com.banksimulator.dto.envoie.UtilisateurRequestDTO;
import com.banksimulator.dto.reponse.AuthResponseDTO;
import com.banksimulator.dto.reponse.PieceIdentiteResponseDTO;
import com.banksimulator.dto.reponse.UtilisateurResponseDTO;
import com.banksimulator.entities.PieceIdentite;
import com.banksimulator.entities.Utilisateur;
import com.banksimulator.enums.EntiteCible;
import com.banksimulator.enums.StatutUtilisateur;
import com.banksimulator.exception.CompteBloqueException;
import com.banksimulator.exception.EmailDejaUtiliseException;
import com.banksimulator.exception.MotDePasseDifferentException;
import com.banksimulator.exception.UtilisateurNotFoundException;
import com.banksimulator.mapper.PieceIdentiteMapper;
import com.banksimulator.mapper.UtilisateurMapper;
import com.banksimulator.repository.PieceIdentiteRepository;
import com.banksimulator.repository.UtilisateurRepository;
import com.banksimulator.service.interfaces.IUtilisateurService;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UtilisateurServiceImpl implements IUtilisateurService {

    @Inject
    UtilisateurRepository utilisateurRepository;

    @Inject
    PieceIdentiteRepository pieceIdentiteRepository;

    @Inject
    AuditTrailServiceImpl auditTrailService;

    @Inject
    UtilisateurMapper utilisateurMapper;

    @Inject
    PieceIdentiteMapper pieceIdentiteMapper;

    @Inject io.smallrye.jwt.build.JwtClaimsBuilder jwtClaimsBuilder;

    @Override
    @Transactional
    public UtilisateurResponseDTO creer(UtilisateurRequestDTO dto) {
        if (utilisateurRepository.existsByEmail(dto.email())) {
            throw new EmailDejaUtiliseException();
        }
        Utilisateur utilisateur = utilisateurMapper.toEntity(dto);
        utilisateur.setMotDePasseHash(BcryptUtil.bcryptHash(dto.motDePasse()));
        utilisateurRepository.persist(utilisateur);

        auditTrailService.tracer(
                utilisateur.getId(),
                "CREER_UTILISATEUR",
                EntiteCible.UTILISATEUR,
                utilisateur.getId()
        );

        return utilisateurMapper.toDTO(utilisateur);
    }

    @Override
    public UtilisateurResponseDTO findById(UUID id) {
        Utilisateur utilisateur = utilisateurRepository.findByIdOptional(id)
                .orElseThrow(UtilisateurNotFoundException::new);
        return utilisateurMapper.toDTO(utilisateur);
    }

    @Override
    public List<UtilisateurResponseDTO> findAll() {
        return utilisateurRepository.listAll()
                .stream()
                .map(utilisateurMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public UtilisateurResponseDTO modifier(UUID id, UtilisateurRequestDTO dto) {
        Utilisateur utilisateur = utilisateurRepository.findByIdOptional(id)
                .orElseThrow(UtilisateurNotFoundException::new);
        if (!utilisateur.getEmail().equals(dto.email())
                && utilisateurRepository.existsByEmail(dto.email())) {
            throw new EmailDejaUtiliseException();
        }
        utilisateurMapper.updateEntity(dto, utilisateur);

        auditTrailService.tracer(
                utilisateur.getId(),
                "MODIFIER_UTILISATEUR",
                EntiteCible.UTILISATEUR,
                utilisateur.getId()
        );

        return utilisateurMapper.toDTO(utilisateur);
    }

    @Override
    @Transactional
    public void supprimer(UUID id) {
        Utilisateur utilisateur = utilisateurRepository.findByIdOptional(id)
                .orElseThrow(UtilisateurNotFoundException::new);
        utilisateurRepository.delete(utilisateur);

        auditTrailService.tracer(
                utilisateur.getId(),
                "SUPPRIMER_UTILISATEUR",
                EntiteCible.UTILISATEUR,
                utilisateur.getId()
        );
    }

    @Override
    @Transactional
    public UtilisateurResponseDTO bloquer(UUID id) {
        Utilisateur utilisateur = utilisateurRepository.findByIdOptional(id)
                .orElseThrow(UtilisateurNotFoundException::new);
        if (utilisateur.getStatut() == StatutUtilisateur.BLOQUE) {
            throw new EmailDejaUtiliseException();
        }
        utilisateur.setStatut(StatutUtilisateur.BLOQUE);

        auditTrailService.tracer(
                utilisateur.getId(),
                "BLOQUER_UTILISATEUR",
                EntiteCible.UTILISATEUR,
                utilisateur.getId()
        );

        return utilisateurMapper.toDTO(utilisateur);
    }

    @Override
    @Transactional
    public UtilisateurResponseDTO debloquer(UUID id) {
        Utilisateur utilisateur = utilisateurRepository.findByIdOptional(id)
                .orElseThrow(UtilisateurNotFoundException::new);
        if (utilisateur.getStatut() != StatutUtilisateur.BLOQUE) {
            throw new EmailDejaUtiliseException();
        }
        utilisateur.setStatut(StatutUtilisateur.ACTIF);
        utilisateur.setNbEchecsAuth(0);

        auditTrailService.tracer(
                utilisateur.getId(),
                "DEBLOQUER_UTILISATEUR",
                EntiteCible.UTILISATEUR,
                utilisateur.getId()
        );

        return utilisateurMapper.toDTO(utilisateur);
    }

    @Override
    @Transactional
    public PieceIdentiteResponseDTO ajouterPieceIdentite(UUID idUtilisateur, PieceIdentiteRequestDTO dto) {
        Utilisateur utilisateur = utilisateurRepository.findByIdOptional(idUtilisateur)
                .orElseThrow(UtilisateurNotFoundException::new);
        PieceIdentite pieceIdentite = pieceIdentiteMapper.toEntity(dto);
        pieceIdentite.setUtilisateur(utilisateur);
        pieceIdentiteRepository.persist(pieceIdentite);

        auditTrailService.tracer(
                utilisateur.getId(),
                "AJOUTER_IDENTITE",
                EntiteCible.UTILISATEUR,
                utilisateur.getId()
        );

        return pieceIdentiteMapper.toDTO(pieceIdentite);
    }

    @Override
    @Transactional
    public AuthResponseDTO authentifier(AuthRequestDTO dto) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(dto.email())
                .orElseThrow(UtilisateurNotFoundException::new);

        if (!BcryptUtil.matches(dto.motDePasse(), utilisateur.getMotDePasseHash())) {
            utilisateur.setNbEchecsAuth(utilisateur.getNbEchecsAuth() + 1);

            if (utilisateur.getNbEchecsAuth() >= 3) {
                utilisateur.setStatut(StatutUtilisateur.BLOQUE);
                auditTrailService.tracer(utilisateur.getId(),
                        "ACTION_AUTH_ECHOUEE", EntiteCible.UTILISATEUR, utilisateur.getId());
                throw new CompteBloqueException();
            }
            throw new MotDePasseDifferentException();
        }

        utilisateur.setNbEchecsAuth(0);

        long expiresIn = 3600L;
        String token = Jwt.issuer("banksimulator")
                .upn(utilisateur.getEmail())
                .groups(utilisateur.getRole().name())
                .expiresIn(expiresIn)
                .sign();

        auditTrailService.tracer(utilisateur.getId(),
                "ACTION_AUTH_REUSSIE", EntiteCible.UTILISATEUR, utilisateur.getId());

        return new AuthResponseDTO(token, "Bearer", expiresIn,
                utilisateur.getId(), utilisateur.getRole());
    }

}
