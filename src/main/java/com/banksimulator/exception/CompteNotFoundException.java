package com.banksimulator.exception;

public class CompteNotFoundException extends RuntimeException {
    public CompteNotFoundException(String id) {
        super("Compte introuvable : " + id);
    }
}
