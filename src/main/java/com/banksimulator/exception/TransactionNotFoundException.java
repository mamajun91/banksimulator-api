package com.banksimulator.exception;

public class TransactionNotFoundException extends BankSimulatorException {
    public TransactionNotFoundException() {
        super("Transaction introuvable", 404);
    }
}
