package com.banksimulator.exception;

public class CompteDejaCloturedException extends BankSimulatorException {
    public CompteDejaCloturedException() {
        super("Compte déja Cloturé", 409);
    }
}
