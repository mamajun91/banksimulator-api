package com.banksimulator.repository;

import com.banksimulator.entities.CompteBancaire;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CompteBancaireRepository implements PanacheRepositoryBase<CompteBancaire, UUID> {

    public Optional<CompteBancaire> findByIban(String iban) {
        return find("iban", iban).firstResultOptional();
    }

    public boolean existsByIban(String iban) {
        return count("iban", iban) > 0;
    }

    public List<CompteBancaire> findByTitulaireId(UUID titulaireId) {
        return find("titulaire.id", titulaireId).list();
    }
}
