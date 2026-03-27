package com.banksimulator.resource;

import com.banksimulator.service.interfaces.ITransactionService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("api/v1/transactions")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionAdminResource {

    @Inject
    ITransactionService transactionService;

    @PATCH
    @Path("/{idTransaction}/annuler")
    @RolesAllowed({"CLIENT", "ADMIN"})
    public Response annuler(@PathParam("idTransaction") UUID idTransaction) {
        return Response.ok(transactionService.annulerTransaction(idTransaction)).build();
    }

    @GET
    @RolesAllowed("ADMIN")
    public Response findAll() {
        return Response.ok(transactionService.findAll()).build();
    }

    @GET
    @Path("/initiateur/{idInitiateur}")
    @RolesAllowed({"ADMIN", "CONSEILLER", "AUDITEUR"})
    public Response findByInitiateur(@PathParam("idInitiateur") UUID idInitiateur) {
        return Response.ok(transactionService.findByInitiateurId(idInitiateur)).build();
    }
}
