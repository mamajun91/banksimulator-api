package com.banksimulator.resource;

import com.banksimulator.dto.envoie.AuthRequestDTO;
import com.banksimulator.service.interfaces.IUtilisateurService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("api/v1/authentification")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    IUtilisateurService utilisateurService;

    @POST
    public Response authentifier(@Valid AuthRequestDTO dto) {
        return Response.ok(utilisateurService.authentifier(dto)).build();
    }

    @POST
    @Path("/verifier-email")
    public Response verifyEmail(String email) {
        boolean existe = utilisateurService.verifyEmail(email);
        return Response.ok(existe).build();
    }
}
