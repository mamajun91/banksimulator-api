package com.banksimulator.resource;


import com.banksimulator.dto.envoie.PieceIdentiteRequestDTO;
import com.banksimulator.dto.envoie.UtilisateurRequestDTO;
import com.banksimulator.dto.reponse.PieceIdentiteResponseDTO;
import com.banksimulator.dto.reponse.UtilisateurResponseDTO;
import com.banksimulator.service.interfaces.IUtilisateurService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("api/v1/utilisateurs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UtilisateurResource {

    @Inject
    IUtilisateurService utilisateurService;

@POST
@RolesAllowed("ADMIN")
    public Response creer(@Valid UtilisateurRequestDTO dto){
    UtilisateurResponseDTO result = utilisateurService.creer(dto);
    return Response.status(Response.Status.CREATED)
            .entity(result)
            .build();
    }

@GET
@Path("/{id}")
@RolesAllowed({"ADMIN", "CONSEILLER"})
    public  Response findById(@PathParam("id") UUID id){
    UtilisateurResponseDTO res = utilisateurService.findById(id);
    return Response.ok(res).build();
    }

@GET
@RolesAllowed({"ADMIN", "CONSEILLER", "AUDITEUR"})
    public Response findAll(){
    List<UtilisateurResponseDTO> rep = utilisateurService.findAll();
    return Response.ok(rep).build();
    }

@PUT
@Path("/{id}")
@RolesAllowed({"ADMIN", "CONSEILLER", "AUDITEUR"})
    public Response modifier(@PathParam("id") UUID id, @Valid UtilisateurRequestDTO dto){
    UtilisateurResponseDTO uti = utilisateurService.modifier(id,dto);
    return Response.ok(uti).build();
    }

@DELETE
@Path("/{id}")
@RolesAllowed("ADMIN")
    public Response supprimer(@PathParam("id") UUID id){
    utilisateurService.supprimer(id);
    return Response.noContent().build();
    }

@PATCH
@Path("/{id}/bloquer")
@RolesAllowed("ADMIN")
    public Response bloquer(@PathParam("id") UUID id){
    UtilisateurResponseDTO utilisateur = utilisateurService.bloquer(id);
    return Response.ok(utilisateur).build();
    }

@PATCH
@Path("/{id}/debloquer")
@RolesAllowed("ADMIN")
    public Response debloquer(@PathParam("id") UUID id){
    UtilisateurResponseDTO deblUti = utilisateurService.debloquer(id);
    return Response.ok(deblUti).build();
    }

@POST
@Path("/{id}/pieces-identite")
@RolesAllowed({"ADMIN", "CONSEILLER"})
    public Response ajouterPieceIdentite(@PathParam("id") UUID id, @Valid PieceIdentiteRequestDTO pieceDto){
    PieceIdentiteResponseDTO reslt = utilisateurService.ajouterPieceIdentite(id,pieceDto);
    return Response.status(Response.Status.CREATED)
            .entity(reslt)
            .build();
    }

}
