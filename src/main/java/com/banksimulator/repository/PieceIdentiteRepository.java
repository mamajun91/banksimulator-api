package com.banksimulator.repository;

import com.banksimulator.entities.PieceIdentite;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PieceIdentiteRepository implements PanacheRepositoryBase<PieceIdentite, UUID> {

    public List<PieceIdentite> findByUtilisateurId(UUID utilisateurId) {
        return find("utilisateur.id", utilisateurId).list();
    }
}