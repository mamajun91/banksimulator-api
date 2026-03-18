package com.banksimulator.exception;

public class TransactionNotFoundException extends BankSimulatorException {
    public TransactionNotFoundException(String id) {
        super("Transaction introuvable", 404);
    }
}
