package com.banksimulator.entities;

import com.banksimulator.enums.StatutTransaction;
import com.banksimulator.enums.TypeTransaction;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compte_debiteur_id")
    private CompteBancaire compteDebiteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compte_crediteur_id")
    private CompteBancaire compteCrediteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiateur_id", nullable = false)
    private Utilisateur initiateur;

    @Column(nullable = false, unique = true, length = 35)
    private String reference;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_transaction", nullable = false)
    private TypeTransaction typeTransaction;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal montant;

    @Column(nullable = false, length = 3)
    private String devise = "EUR";

    @Column(length = 140)
    private String libelle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutTransaction statut = StatutTransaction.PENDING;

    @Column(name = "iban_externe", length = 34)
    private String ibanExterne;

    @Column(name = "date_execution", nullable = false)
    private LocalDateTime dateExecution;

    @Column(name = "date_valeur")
    private LocalDate dateValeur;

    @Column(name = "message_erreur", length = 255)
    private String messageErreur;

    @Version
    private Long version;

    @PrePersist
    public void prePersist() {
        dateExecution = LocalDateTime.now();
    }

    //Constructeurs
    public Transaction() {}

    public Transaction(Utilisateur initiateur, TypeTransaction typeTransaction, BigDecimal montant, String reference) {
        this.initiateur = initiateur;
        this.typeTransaction = typeTransaction;
        this.montant = montant;
        this.reference = reference;
    }


    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public CompteBancaire getCompteDebiteur() { return compteDebiteur; }
    public void setCompteDebiteur(CompteBancaire compteDebiteur) { this.compteDebiteur = compteDebiteur; }
    public CompteBancaire getCompteCrediteur() { return compteCrediteur; }
    public void setCompteCrediteur(CompteBancaire compteCrediteur) { this.compteCrediteur = compteCrediteur; }
    public Utilisateur getInitiateur() { return initiateur; }
    public void setInitiateur(Utilisateur initiateur) { this.initiateur = initiateur; }
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    public TypeTransaction getTypeTransaction() { return typeTransaction; }
    public void setTypeTransaction(TypeTransaction typeTransaction) { this.typeTransaction = typeTransaction; }
    public BigDecimal getMontant() { return montant; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }
    public String getDevise() { return devise; }
    public void setDevise(String devise) { this.devise = devise; }
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
    public StatutTransaction getStatut() { return statut; }
    public void setStatut(StatutTransaction statut) { this.statut = statut; }
    public String getIbanExterne() { return ibanExterne; }
    public void setIbanExterne(String ibanExterne) { this.ibanExterne = ibanExterne; }
    public LocalDateTime getDateExecution() { return dateExecution; }
    public LocalDate getDateValeur() { return dateValeur; }
    public void setDateValeur(LocalDate dateValeur) { this.dateValeur = dateValeur; }
    public String getMessageErreur() { return messageErreur; }
    public void setMessageErreur(String messageErreur) { this.messageErreur = messageErreur; }
    public Long getVersion() { return version; }
}
