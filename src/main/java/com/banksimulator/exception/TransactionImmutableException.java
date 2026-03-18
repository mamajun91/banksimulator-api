package com.banksimulator.exception;

public class TransactionImmutableException extends BankSimulatorException {
    public TransactionImmutableException() {
        super("Transaction non annulable — statut différent de PENDING", 422);
    }
}
