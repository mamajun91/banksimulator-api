package com.banksimulator.exception;

public class CompteBloqueException extends RuntimeException {
    public CompteBloqueException() {
        super("Compte bloqué — accès refusé");
    }
}