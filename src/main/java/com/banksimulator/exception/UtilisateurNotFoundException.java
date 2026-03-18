package com.banksimulator.exception;

public class UtilisateurNotFoundException extends BankSimulatorException {
    public UtilisateurNotFoundException() {
        super("Utilisateur introuvable", 404);
    }
}