package com.banksimulator.service.interfaces;


import com.banksimulator.enums.EntiteCible;
import java.util.UUID;

public interface IAuditTrailService {

    void tracer(UUID utilisateurId, String actionCode, EntiteCible entiteCible, UUID entiteId);
}