package com.banksimulator.exception;

public class UtilisateurNotFoundException extends BankSimulatorException {
    public UtilisateurNotFoundException(String id) {
        super("Utilisateur introuvable", 404);
    }
}