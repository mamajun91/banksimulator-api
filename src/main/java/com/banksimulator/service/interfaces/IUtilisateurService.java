package com.banksimulator.service.interfaces;

import com.banksimulator.dto.envoie.PieceIdentiteRequestDTO;
import com.banksimulator.dto.envoie.UtilisateurRequestDTO;
import com.banksimulator.dto.reponse.PieceIdentiteResponseDTO;
import com.banksimulator.dto.reponse.UtilisateurResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IUtilisateurService{

    UtilisateurResponseDTO creer(UtilisateurRequestDTO dto);
    UtilisateurResponseDTO findById(UUID id);
    List<UtilisateurResponseDTO> findAll();
    UtilisateurResponseDTO modifier(UUID id, UtilisateurRequestDTO dto);
    void supprimer(UUID id);
    UtilisateurResponseDTO bloquer(UUID id);
    UtilisateurResponseDTO debloquer(UUID id);
    PieceIdentiteResponseDTO ajouterPieceIdentite(UUID id, PieceIdentiteRequestDTO pieceDto);
}