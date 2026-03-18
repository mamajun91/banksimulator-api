package com.banksimulator.exception;

public class SoldeInsuffisantException extends RuntimeException {
    public SoldeInsuffisantException() {
        super("Solde insuffisant");
    }
}
