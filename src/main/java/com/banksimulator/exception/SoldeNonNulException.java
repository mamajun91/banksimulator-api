package com.banksimulator.exception;

public class SoldeNonNulException extends BankSimulatorException {
    public SoldeNonNulException() {
        super("Le solde doit être égal à 0", 422);
    }
}
