package com.banksimulator.exception;

public class CompteNonActifException extends BankSimulatorException {
    public CompteNonActifException() {
        super("Compte non actif", 422);
    }
}
