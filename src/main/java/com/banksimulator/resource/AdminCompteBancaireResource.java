package com.banksimulator.resource;

import com.banksimulator.service.interfaces.ICompteBancaireService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("api/v1/comptes")
@Produces(MediaType.APPLICATION_JSON)
public class AdminCompteBancaireResource {

    @Inject
    ICompteBancaireService compteService;

    @GET
    @RolesAllowed("ADMIN")
    public Response findAll() {
        return Response.ok(compteService.findAll()).build();
    }
}