package com.banksimulator.resource;

import com.banksimulator.dto.envoie.TransactionRequestDTO;
import com.banksimulator.dto.reponse.TransactionResponseDTO;
import com.banksimulator.service.interfaces.ITransactionService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("api/v1/comptes/{idCompte}/transactions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransactionResource {

    @Inject
    ITransactionService transactionService;

    @Inject
    JsonWebToken jwt;

    @POST
    @Path("/depot")
    @RolesAllowed("CLIENT")
    public Response depot(@PathParam("idCompte") UUID idCompte,
                          @Valid TransactionRequestDTO dto) {
        return Response.status(Response.Status.CREATED)
                .entity(transactionService.effectuerDepot(dto, idCompte))
                .build();
    }

    @POST
    @Path("/retrait")
    @RolesAllowed("CLIENT")
    public Response retrait(@PathParam("idCompte") UUID idCompte,
                            @Valid TransactionRequestDTO dto) {
        return Response.status(Response.Status.CREATED)
                .entity(transactionService.effectuerRetrait(dto, idCompte))
                .build();
    }

    @POST
    @Path("/virement")
    @RolesAllowed("CLIENT")
    public Response virement(@PathParam("idCompte") UUID idCompte,
                             @Valid TransactionRequestDTO dto) {
        return Response.status(Response.Status.CREATED)
                .entity(transactionService.effectuerVirement(dto, idCompte))
                .build();
    }

    @GET
    @Path("/debiteur")
    @RolesAllowed({"ADMIN", "CONSEILLER", "AUDITEUR"})
    public Response findByCompteDebiteur(@PathParam("idCompte") UUID idCompte) {
        return Response.ok(transactionService.findByCompteDebiteurId(idCompte)).build();
    }

    @GET
    @Path("/crediteur")
    @RolesAllowed({"ADMIN", "CONSEILLER", "AUDITEUR"})
    public Response findByCompteCrediteur(@PathParam("idCompte") UUID idCompte) {
        return Response.ok(transactionService.findByCompteCrediteurId(idCompte)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "CONSEILLER", "AUDITEUR"})
    public Response findById(@PathParam("idCompte") UUID idCompte,
                             @PathParam("id") UUID id) {
        return Response.ok(transactionService.findById(id)).build();
    }

    /** Pour consulter l'historique*/
    @GET
    @Path("/historique")
    @RolesAllowed("CLIENT")
    public Response historique(@PathParam("idCompte") UUID idCompte) {
        String email = jwt.getName();
        return Response.ok(transactionService.findHistoriqueByCompte(idCompte, email)).build();
    }
}
