package com.banksimulator.resource;

import com.banksimulator.dto.envoie.CompteBancaireRequestDTO;
import com.banksimulator.dto.envoie.UtilisateurRequestDTO;
import com.banksimulator.dto.reponse.UtilisateurResponseDTO;
import com.banksimulator.service.interfaces.ICompteBancaireService;
import com.banksimulator.service.interfaces.IUtilisateurService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Path("api/v1/clients/{idClient}/comptes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompteBancaireResource {

    @Inject
    ICompteBancaireService compteService;

    @POST
    @RolesAllowed({"ADMIN", "CONSEILLER"})
    public Response creer(
            @PathParam("idClient") UUID idClient,
            @Valid CompteBancaireRequestDTO dto) {
        return Response.status(Response.Status.CREATED)
                .entity(compteService.creer(dto, idClient))
                .build();
    }

    @GET
    @RolesAllowed({"ADMIN", "CONSEILLER", "AUDITEUR"})
    public Response findByClient(
            @PathParam("idClient") UUID idClient) {
        return Response.ok(compteService.findByClient(idClient)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "CONSEILLER", "AUDITEUR"})
    public Response findById(
            @PathParam("idClient") UUID idClient,
            @PathParam("id") UUID id) {
        return Response.ok(compteService.findById(id)).build();
    }

    @PATCH
    @Path("/{id}/bloquer")
    @RolesAllowed("ADMIN")
    public Response bloquer(
            @PathParam("idClient") UUID idClient,
            @PathParam("id") UUID id) {
        return Response.ok(compteService.bloquer(id)).build();
    }

    @PATCH
    @Path("/{id}/debloquer")
    @RolesAllowed("ADMIN")
    public Response debloquer(
            @PathParam("idClient") UUID idClient,
            @PathParam("id") UUID id) {
        return Response.ok(compteService.debloquer(id)).build();
    }

    @PATCH
    @Path("/{id}/cloturer")
    @RolesAllowed("ADMIN")
    public Response cloturer(
            @PathParam("idClient") UUID idClient,
            @PathParam("id") UUID id) {
        return Response.ok(compteService.cloturer(id)).build();
    }

    @PATCH
    @Path("/{id}/decouvert")
    @RolesAllowed({"ADMIN", "CONSEILLER"})
    public Response modifierDecouvert(
            @PathParam("idClient") UUID idClient,
            @PathParam("id") UUID id,
            BigDecimal montant) {
        return Response.ok(compteService.modifierDecouvert(id, montant)).build();
    }
}
