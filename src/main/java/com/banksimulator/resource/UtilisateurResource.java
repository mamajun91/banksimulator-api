package com.banksimulator.resource;


import com.banksimulator.dto.envoie.PieceIdentiteRequestDTO;
import com.banksimulator.dto.envoie.UtilisateurRequestDTO;
import com.banksimulator.service.interfaces.IUtilisateurService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("api/v1/utilisateurs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UtilisateurResource {

    @Inject
    IUtilisateurService utilisateurService;

@POST
@RolesAllowed("ADMIN")
    public Response creer(UtilisateurRequestDTO dto){
    return null;
    }

@GET
@Path("/{id}")
@RolesAllowed({"ADMIN", "CONSEILLER"})
    public  Response findById(@PathParam("id") UUID id){
    return null;
    }

@GET
@RolesAllowed({"ADMIN", "CONSEILLER", "AUDITEUR"})
    public Response findAll(){
    return null;
    }

@PUT
@Path("/{id}")
@RolesAllowed({"ADMIN", "CONSEILLER", "AUDITEUR"})
    public Response modifier(@PathParam("id") UUID id, UtilisateurRequestDTO dto){
    return null;
    }

@DELETE
@Path("/{id}")
@RolesAllowed("ADMIN")
    public Response supprimer(@PathParam("id") UUID id){
    return null;
    }

@PATCH
@Path("/{id}/bloquer")
@RolesAllowed("ADMIN")
    public Response bloquer(@PathParam("id") UUID id){
    return  null;
    }

@PATCH
@Path("/{id}/debloquer")
@RolesAllowed("ADMIN")
    public Response debloquer(@PathParam("id") UUID id){
    return null;
    }

@POST
@Path("/{id}/pieces-identite")
@RolesAllowed({"ADMIN", "CONSEILLER"})
    public Response ajouterPieceIdentite(@PathParam("id") UUID id, PieceIdentiteRequestDTO pieceDto){
    return  null;
    }

}
