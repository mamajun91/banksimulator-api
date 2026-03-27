package com.banksimulator.exception;

public class AccesInterditException extends BankSimulatorException {
    public AccesInterditException() {
        super("Accès interdit", 403);
    }
}
