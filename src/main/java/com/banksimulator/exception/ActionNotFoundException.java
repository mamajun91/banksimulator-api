package com.banksimulator.exception;

public class ActionNotFoundException extends BankSimulatorException {
    public ActionNotFoundException() {
        super("Action introuvable", 404);
    }
}
