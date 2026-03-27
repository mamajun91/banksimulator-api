package com.banksimulator.mapper;

import com.banksimulator.dto.reponse.AuditTrailResponseDTO;
import com.banksimulator.entities.AuditTrail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "jakarta")
public interface AuditTrailMapper {

    @Mapping(target = "utilisateurId", source = "utilisateur.id")
    @Mapping(target = "actionCode", source = "action.code")
    AuditTrailResponseDTO toDTO(AuditTrail auditTrail);
}
