package com.banksimulator.exception;

public class CompteBloqueException extends BankSimulatorException {
    public CompteBloqueException() {
        super("Compte bloqué — accès refusé", 432);
    }
}