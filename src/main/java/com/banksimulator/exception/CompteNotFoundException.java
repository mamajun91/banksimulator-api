package com.banksimulator.exception;

public class CompteNotFoundException extends BankSimulatorException {
    public CompteNotFoundException(String id) {
        super("Compte introuvable", 404);
    }
}
