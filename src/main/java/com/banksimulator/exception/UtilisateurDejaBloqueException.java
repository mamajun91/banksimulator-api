package com.banksimulator.exception;

public class UtilisateurDejaBloqueException extends BankSimulatorException {
    public UtilisateurDejaBloqueException(String message) {
        super("Utilisateur déjà bloqué", 409);
    }
}
