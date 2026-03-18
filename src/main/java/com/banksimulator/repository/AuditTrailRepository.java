package com.banksimulator.repository;

import com.banksimulator.entities.AuditTrail;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class AuditTrailRepository implements PanacheRepositoryBase<AuditTrail, UUID> {

    public List<AuditTrail> findByUtilisateurId(UUID utilisateurId) {
        return find("utilisateur.id", utilisateurId).list();
    }

    public List<AuditTrail> findByEntiteId(UUID entiteId) {
        return find("entiteId", entiteId).list();
    }
}
