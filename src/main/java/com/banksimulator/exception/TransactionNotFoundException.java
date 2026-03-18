package com.banksimulator.exception;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(String id) {
        super("Transaction introuvable : " + id);
    }
}
