package com.banksimulator.exception;

public class EmailDejaUtiliseException extends BankSimulatorException {
    public EmailDejaUtiliseException() {
        super("Email déjà utilisé", 409);
    }
}
