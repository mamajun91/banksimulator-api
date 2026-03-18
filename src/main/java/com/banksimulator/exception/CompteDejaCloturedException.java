package com.banksimulator.exception;

public class CompteDejaCloturedException extends BankSimulatorException {
    public CompteDejaCloturedException(String message) {
        super("Compte déja Cloturé", 409);
    }
}
