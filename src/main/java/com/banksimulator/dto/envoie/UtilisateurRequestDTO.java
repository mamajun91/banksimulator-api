package com.banksimulator.dto.envoie;

import com.banksimulator.enums.Role;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record UtilisateurRequestDTO(

        @NotBlank String nom,
        @NotBlank String prenom,
        @NotBlank @Email String email,
        @NotBlank String telephone,
        @NotNull LocalDate dateNaissance,
        @NotBlank String adresseRue,
        @NotBlank String adresseVille,
        @NotBlank String codePostal,
        @NotBlank String pays,
        @NotBlank @Size(min = 8) String motDePasse,
        @NotNull Role role
) {}