package com.banksimulator.repository;

import com.banksimulator.entities.Utilisateur;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UtilisateurRepository implements PanacheRepositoryBase<Utilisateur, UUID> {

    public Optional<Utilisateur> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }

    public boolean existsByEmail(String email) {
        return count("email", email) > 0;
    }
}