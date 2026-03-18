package com.banksimulator.exception;

public class CompteNotFoundException extends BankSimulatorException {
    public CompteNotFoundException() {
        super("Compte introuvable", 404);
    }
}
