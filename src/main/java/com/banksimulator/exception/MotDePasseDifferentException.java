package com.banksimulator.exception;

public class MotDePasseDifferentException extends BankSimulatorException {
    public MotDePasseDifferentException() {
        super("Le mot de passe saisi ne correspond pas", 401);
    }
}
