package com.banksimulator.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<RuntimeException> {

    @Override
    public Response toResponse(RuntimeException exception) {
        if (exception instanceof UtilisateurNotFoundException ||
                exception instanceof CompteNotFoundException ||
                exception instanceof TransactionNotFoundException) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorReponse(exception.getMessage()))
                    .build();
        }
        if (exception instanceof EmailDejaUtiliseException) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorReponse(exception.getMessage()))
                    .build();
        }
        if (exception instanceof CompteNonActifException ||
                exception instanceof SoldeInsuffisantException ||
                exception instanceof TransactionImmutableException) {
            return Response.status(422)
                    .entity(new ErrorReponse(exception.getMessage()))
                    .build();
        }
        if (exception instanceof CompteBloqueException) {
            return Response.status(423)
                    .entity(new ErrorReponse(exception.getMessage()))
                    .build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorReponse("Erreur interne du serveur"))
                .build();
    }
}