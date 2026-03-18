package com.banksimulator.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<BankSimulatorException> {

    @Override
    public Response toResponse(BankSimulatorException exception) {
        return Response.status(exception.getStatus())
                .entity(new ErrorReponse(exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}