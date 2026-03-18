package com.banksimulator.exception;

public class EmailDejaUtiliseException extends BankSimulatorException {
    public EmailDejaUtiliseException(String email) {
        super("Email déjà utilisé", 409);
    }
}
