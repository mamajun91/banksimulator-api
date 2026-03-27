package com.banksimulator.mapper;

import com.banksimulator.dto.envoie.CompteBancaireRequestDTO;
import com.banksimulator.dto.reponse.CompteBancaireResponseDTO;
import com.banksimulator.entities.CompteBancaire;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "jakarta")
public interface CompteBancaireMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "titulaire", ignore = true)
    @Mapping(target = "conseiller", ignore = true)
    @Mapping(target = "iban", ignore = true)
    @Mapping(target = "bic", ignore = true)
    @Mapping(target = "solde", ignore = true)
    @Mapping(target = "devise", ignore = true)
    @Mapping(target = "statut", ignore = true)
    @Mapping(target = "dateOuverture", ignore = true)
    @Mapping(target = "dateCloture", ignore = true)
    @Mapping(target = "dateModification", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "transactionsDebitees", ignore = true)
    @Mapping(target = "transactionsCreditees", ignore = true)
    CompteBancaire toEntity(CompteBancaireRequestDTO dto);

    @Mapping(target = "titulaireId", source = "titulaire.id")
    @Mapping(target = "conseillerId", source = "conseiller.id")
    CompteBancaireResponseDTO toDTO(CompteBancaire compte);
}