package com.banksimulator.mapper;

import com.banksimulator.dto.envoie.TransactionRequestDTO;
import com.banksimulator.dto.reponse.TransactionResponseDTO;
import com.banksimulator.entities.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "jakarta")
public interface TransactionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reference", ignore = true)
    @Mapping(target = "statut", ignore = true)
    @Mapping(target = "compteDebiteur", ignore = true)
    @Mapping(target = "compteCrediteur", ignore = true)
    @Mapping(target = "initiateur", ignore = true)
    @Mapping(target = "dateExecution", ignore = true)
    @Mapping(target = "dateValeur", ignore = true)
    @Mapping(target = "messageErreur", ignore = true)
    @Mapping(target = "version", ignore = true)
    Transaction toEntity(TransactionRequestDTO dto);

    @Mapping(target = "compteDebiteurId", source = "compteDebiteur.id")
    @Mapping(target = "compteCrediteurId", source = "compteCrediteur.id")
    @Mapping(target = "initiateurId", source = "initiateur.id")
    TransactionResponseDTO toDTO(Transaction transaction);
}