package com.banksimulator.dto.reponse;

import com.banksimulator.enums.Role;
import com.banksimulator.enums.StatutUtilisateur;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record UtilisateurResponseDTO(

        UUID id,
        String nom,
        String prenom,
        String email,
        String telephone,
        LocalDate dateNaissance,
        String adresseRue,
        String adresseVille,
        String codePostal,
        String pays,
        Role role,
        StatutUtilisateur statut,
        int nbEchecsAuth,
        LocalDateTime dateCreation,
        LocalDateTime dateModification
) {}
