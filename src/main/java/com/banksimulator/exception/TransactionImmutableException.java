package com.banksimulator.exception;

public class TransactionImmutableException extends RuntimeException {
    public TransactionImmutableException() {
        super("Transaction non annulable — statut différent de PENDING");
    }
}
