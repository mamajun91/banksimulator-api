package com.banksimulator.service.interfaces;

import com.banksimulator.dto.envoie.CompteBancaireRequestDTO;
import com.banksimulator.dto.reponse.CompteBancaireResponseDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ICompteBancaireService {

    CompteBancaireResponseDTO creer(CompteBancaireRequestDTO dto, UUID idTitulaire);
    CompteBancaireResponseDTO findById(UUID id);
    List<CompteBancaireResponseDTO> findAll();
    List<CompteBancaireResponseDTO> findByClient(UUID idClient);
    CompteBancaireResponseDTO bloquer(UUID id);
    CompteBancaireResponseDTO debloquer(UUID id);
    CompteBancaireResponseDTO cloturer(UUID id);
    CompteBancaireResponseDTO modifierDecouvert(UUID id, BigDecimal nouveauDecouvert);
}
