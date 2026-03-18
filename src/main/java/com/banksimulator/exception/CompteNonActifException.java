package com.banksimulator.exception;

public class CompteNonActifException extends RuntimeException {
    public CompteNonActifException(String id) {
        super("Compte non actif : " + id);
    }
}
