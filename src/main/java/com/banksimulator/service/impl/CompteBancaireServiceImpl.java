package com.banksimulator.service.impl;

import com.banksimulator.dto.envoie.CompteBancaireRequestDTO;
import com.banksimulator.dto.reponse.CompteBancaireResponseDTO;
import com.banksimulator.entities.CompteBancaire;
import com.banksimulator.entities.Utilisateur;
import com.banksimulator.enums.EntiteCible;
import com.banksimulator.enums.StatutCompte;
import com.banksimulator.exception.*;
import com.banksimulator.mapper.CompteBancaireMapper;
import com.banksimulator.repository.CompteBancaireRepository;
import com.banksimulator.repository.UtilisateurRepository;
import com.banksimulator.service.interfaces.ICompteBancaireService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CompteBancaireServiceImpl implements ICompteBancaireService {

    @Inject CompteBancaireRepository compteRepo;
    @Inject UtilisateurRepository utilisateurRepo;
    @Inject CompteBancaireMapper compteMapper;
    @Inject AuditTrailServiceImpl auditTrailService;

    @Override
    @Transactional
    public CompteBancaireResponseDTO creer(CompteBancaireRequestDTO dto, UUID idTitulaire) {
        Utilisateur titulaire = utilisateurRepo.findByIdOptional(idTitulaire)
                .orElseThrow(UtilisateurNotFoundException::new);

        String iban = genererIbanUnique();
        String bic = "BANKSIMFR";

        CompteBancaire compte = compteMapper.toEntity(dto);
        compte.setTitulaire(titulaire);
        compte.setIban(iban);
        compte.setBic(bic);

        if (dto.idConseiller() != null) {
            Utilisateur conseiller = utilisateurRepo.findByIdOptional(dto.idConseiller())
                    .orElseThrow(UtilisateurNotFoundException::new);
            compte.setConseiller(conseiller);
        }

        compteRepo.persist(compte);
        auditTrailService.tracer(idTitulaire, "CREER_COMPTE", EntiteCible.COMPTE, compte.getId());
        return compteMapper.toDTO(compte);
    }

    @Override
    public CompteBancaireResponseDTO findById(UUID id) {
        return compteMapper.toDTO(compteRepo.findByIdOptional(id)
                .orElseThrow(CompteNotFoundException::new));
    }

    @Override
    public List<CompteBancaireResponseDTO> findAll() {
        return compteRepo.listAll().stream().map(compteMapper::toDTO).toList();
    }

    @Override
    public List<CompteBancaireResponseDTO> findByClient(UUID idClient) {
        return compteRepo.findByTitulaireId(idClient).stream().map(compteMapper::toDTO).toList();
    }

    @Override
    @Transactional
    public CompteBancaireResponseDTO bloquer(UUID id) {
        CompteBancaire compte = compteRepo.findByIdOptional(id)
                .orElseThrow(CompteNotFoundException::new);
        if (compte.getStatut() == StatutCompte.BLOQUE)
            throw new CompteNonActifException();
        compte.setStatut(StatutCompte.BLOQUE);
        auditTrailService.tracer(compte.getTitulaire().getId(), "BLOQUER_COMPTE", EntiteCible.COMPTE, id);
        return compteMapper.toDTO(compte);
    }

    @Override
    @Transactional
    public CompteBancaireResponseDTO debloquer(UUID id) {
        CompteBancaire compte = compteRepo.findByIdOptional(id)
                .orElseThrow(CompteNotFoundException::new);
        if (compte.getStatut() != StatutCompte.BLOQUE)
            throw new CompteNonActifException();
        compte.setStatut(StatutCompte.ACTIF);
        auditTrailService.tracer(compte.getTitulaire().getId(), "DEBLOQUER_COMPTE", EntiteCible.COMPTE, id);
        return compteMapper.toDTO(compte);
    }

    @Override
    @Transactional
    public CompteBancaireResponseDTO cloturer(UUID id) {
        CompteBancaire compte = compteRepo.findByIdOptional(id)
                .orElseThrow(CompteNotFoundException::new);
        if (compte.getSolde().compareTo(BigDecimal.ZERO) != 0)
            throw new SoldeInsuffisantException();
        compte.setStatut(StatutCompte.CLOTURE);
        compte.setDateCloture(LocalDate.now());
        auditTrailService.tracer(compte.getTitulaire().getId(), "CLOTURER_COMPTE", EntiteCible.COMPTE, id);
        return compteMapper.toDTO(compte);
    }

    @Override
    @Transactional
    public CompteBancaireResponseDTO modifierDecouvert(UUID id, BigDecimal nouveauDecouvert) {
        CompteBancaire compte = compteRepo.findByIdOptional(id)
                .orElseThrow(CompteNotFoundException::new);
        compte.setDecouvertAutorise(nouveauDecouvert);
        auditTrailService.tracer(compte.getTitulaire().getId(), "MODIFIER_DECOUVERT", EntiteCible.COMPTE, id);
        return compteMapper.toDTO(compte);
    }

    private String genererIbanUnique() {
        String iban;
        do {
            iban = "FR76" + String.format("%011d", (long)(Math.random() * 100000000000L));
        } while (compteRepo.existsByIban(iban));
        return iban;
    }
}
