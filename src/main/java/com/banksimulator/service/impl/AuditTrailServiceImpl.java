package com.banksimulator.service.impl;

import com.banksimulator.dto.reponse.AuditTrailResponseDTO;
import com.banksimulator.entities.Action;
import com.banksimulator.entities.AuditTrail;
import com.banksimulator.entities.Utilisateur;
import com.banksimulator.enums.EntiteCible;
import com.banksimulator.exception.ActionNotFoundException;
import com.banksimulator.exception.UtilisateurNotFoundException;
import com.banksimulator.mapper.AuditTrailMapper;
import com.banksimulator.repository.ActionRepository;
import com.banksimulator.repository.AuditTrailRepository;
import com.banksimulator.repository.UtilisateurRepository;
import com.banksimulator.service.interfaces.IAuditTrailService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class AuditTrailServiceImpl implements IAuditTrailService {

    @Inject
    AuditTrailRepository auditTrailRepository;
    @Inject
    ActionRepository actionRepository;
    @Inject
    UtilisateurRepository utilisateurRepository;
    @Inject
    AuditTrailMapper auditTrailMapper;

    @Override
    @Transactional
    public void tracer(UUID utilisateurId, String actionCode, EntiteCible entiteCible, UUID entiteId) {
        Action action = actionRepository.findByCode(actionCode)
                .orElseThrow(ActionNotFoundException::new);

        Utilisateur utilisateur = utilisateurRepository.findByIdOptional(utilisateurId)
                .orElseThrow(UtilisateurNotFoundException::new);

        AuditTrail audit = new AuditTrail(utilisateur, action, entiteCible, entiteId);
        auditTrailRepository.persist(audit);
    }

    @Override
    public List<AuditTrailResponseDTO> findAll() {
        return auditTrailRepository
                .listAll()
                .stream()
                .map(auditTrailMapper::toDTO)
                .toList();
    }

    @Override
    public List<AuditTrailResponseDTO> findByUtilisateurId(UUID utilisateurId) {
        return auditTrailRepository
                .findByUtilisateurId(utilisateurId)
                .stream()
                .map(auditTrailMapper::toDTO)
                .toList();
    }

    @Override
    public List<AuditTrailResponseDTO> findByEntiteId(UUID entiteId) {
        return auditTrailRepository
                .findByEntiteId(entiteId)
                .stream()
                .map(auditTrailMapper::toDTO)
                .toList();
    }
}