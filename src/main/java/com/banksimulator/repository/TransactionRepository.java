package com.banksimulator.repository;

import com.banksimulator.entities.Transaction;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class TransactionRepository implements PanacheRepositoryBase<Transaction, UUID> {

    public List<Transaction> findByCompteDebiteurId(UUID compteId) {
        return find("compteDebiteur.id", compteId).list();
    }

    public List<Transaction> findByCompteCrediteurId(UUID compteId) {
        return find("compteCrediteur.id", compteId).list();
    }

    public List<Transaction> findByInitiateurId(UUID utilisateurId) {
        return find("initiateur.id", utilisateurId).list();
    }

    public boolean existsByReference(String reference) {
        return count("reference", reference) > 0;
    }
}
