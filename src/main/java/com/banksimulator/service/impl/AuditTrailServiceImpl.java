package com.banksimulator.service.impl;

import com.banksimulator.entities.Action;
import com.banksimulator.entities.AuditTrail;
import com.banksimulator.enums.EntiteCible;
import com.banksimulator.repository.ActionRepository;
import com.banksimulator.repository.AuditTrailRepository;
import com.banksimulator.service.interfaces.IAuditTrailService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.UUID;

@ApplicationScoped
public class AuditTrailServiceImpl implements IAuditTrailService {

    @Inject
    AuditTrailRepository auditTrailRepository;

    @Inject
    ActionRepository actionRepository;

    @Override
    @Transactional
    public void tracer(UUID utilisateurId, String actionCode, EntiteCible entiteCible, UUID entiteId) {
        Action action = actionRepository.findByCode(actionCode)
                .orElseThrow(() -> new NotFoundException("Action introuvable : " + actionCode));

        AuditTrail audit = new AuditTrail();
        audit.setEntiteCible(entiteCible);
        audit.setEntiteId(entiteId);
        audit.setAction(action);
        auditTrailRepository.persist(audit);
    }
}