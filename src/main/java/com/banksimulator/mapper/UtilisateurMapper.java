package com.banksimulator.mapper;

import com.banksimulator.dto.envoie.UtilisateurRequestDTO;
import com.banksimulator.dto.reponse.UtilisateurResponseDTO;
import com.banksimulator.entities.Utilisateur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "jakarta")
public interface UtilisateurMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "motDePasseHash", ignore = true)
    @Mapping(target = "statut", ignore = true)
    @Mapping(target = "nbEchecsAuth", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateModification", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "piecesIdentite", ignore = true)
    @Mapping(target = "comptes", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "audits", ignore = true)
    Utilisateur toEntity(UtilisateurRequestDTO dto);

    UtilisateurResponseDTO toDTO(Utilisateur utilisateur);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "motDePasseHash", ignore = true)
    @Mapping(target = "statut", ignore = true)
    @Mapping(target = "nbEchecsAuth", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateModification", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "piecesIdentite", ignore = true)
    @Mapping(target = "comptes", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "audits", ignore = true)
    void updateEntity(UtilisateurRequestDTO dto, @MappingTarget Utilisateur utilisateur);
}
