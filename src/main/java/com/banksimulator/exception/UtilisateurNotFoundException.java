package com.banksimulator.exception;

public class UtilisateurNotFoundException extends RuntimeException {
    public UtilisateurNotFoundException(String id) {
        super("Utilisateur introuvable : " + id);
    }
}