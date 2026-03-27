package com.banksimulator.resource;

import com.banksimulator.service.interfaces.IAuditTrailService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("api/v1/audit-trail")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed({"ADMIN", "AUDITEUR"})
public class AuditTrailResource {

    @Inject
    IAuditTrailService auditTrailService;

    @GET
    public Response findAll() {
        return Response.ok(auditTrailService.findAll()).build();
    }

    @GET
    @Path("/utilisateur/{idUtilisateur}")
    public Response findByUtilisateur(@PathParam("idUtilisateur") UUID idUtilisateur) {
        return Response.ok(auditTrailService.findByUtilisateurId(idUtilisateur)).build();
    }

    @GET
    @Path("/entite/{idEntite}")
    public Response findByEntite(@PathParam("idEntite") UUID idEntite) {
        return Response.ok(auditTrailService.findByEntiteId(idEntite)).build();
    }
}