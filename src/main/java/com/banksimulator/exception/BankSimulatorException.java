package com.banksimulator.exception;

public abstract class BankSimulatorException extends RuntimeException {
    private final int status;

    protected BankSimulatorException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() { return status; }
}
