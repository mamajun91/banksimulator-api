package com.banksimulator.exception;

public class SoldeInsuffisantException extends BankSimulatorException {
    public SoldeInsuffisantException() {
        super("Solde insuffisant", 422);
    }
}
