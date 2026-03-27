package com.banksimulator.service.interfaces;


import com.banksimulator.dto.reponse.AuditTrailResponseDTO;
import com.banksimulator.enums.EntiteCible;

import java.util.List;
import java.util.UUID;

public interface IAuditTrailService {

    void tracer(UUID utilisateurId, String actionCode, EntiteCible entiteCible, UUID entiteId);
    List<AuditTrailResponseDTO> findAll();
    List<AuditTrailResponseDTO> findByUtilisateurId(UUID utilisateurId);
    List<AuditTrailResponseDTO> findByEntiteId(UUID entiteId);
}