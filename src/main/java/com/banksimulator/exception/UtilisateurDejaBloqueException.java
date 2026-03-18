package com.banksimulator.exception;

public class UtilisateurDejaBloqueException extends BankSimulatorException {
    public UtilisateurDejaBloqueException() {
        super("Utilisateur déjà bloqué", 409);
    }
}
