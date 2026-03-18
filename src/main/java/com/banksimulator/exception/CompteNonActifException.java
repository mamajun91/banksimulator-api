package com.banksimulator.exception;

public class CompteNonActifException extends BankSimulatorException {
    public CompteNonActifException(String id) {
        super("Compte non actif", 422);
    }
}
