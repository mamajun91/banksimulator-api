package com.banksimulator.mapper;

import com.banksimulator.dto.envoie.PieceIdentiteRequestDTO;
import com.banksimulator.dto.reponse.PieceIdentiteResponseDTO;
import com.banksimulator.entities.PieceIdentite;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "jakarta")
public interface PieceIdentiteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "utilisateur", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    PieceIdentite toEntity(PieceIdentiteRequestDTO dto);

    @Mapping(target = "utilisateurId", source = "utilisateur.id")
    PieceIdentiteResponseDTO toDTO(PieceIdentite pieceIdentite);
}
