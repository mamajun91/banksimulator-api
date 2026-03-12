package com.banksimulator.entities;

import com.banksimulator.enums.StatutCompte;
import com.banksimulator.enums.TypeCompte;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "compte_bancaire")
public class CompteBancaire {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "titulaire_id", nullable = false)
    private Utilisateur titulaire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conseiller_id")
    private Utilisateur conseiller;

    @Column(nullable = false, unique = true, length = 34)
    private String iban;

    @Column(nullable = false, length = 11)
    private String bic;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_compte", nullable = false)
    private TypeCompte typeCompte;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal solde = BigDecimal.ZERO;

    @Column(nullable = false, length = 3)
    private String devise = "EUR";

    @Column(name = "decouvert_autorise", nullable = false, precision = 15, scale = 2)
    private BigDecimal decouvertAutorise = BigDecimal.ZERO;

    @Column(name = "taux_interet", precision = 5, scale = 4)
    private BigDecimal tauxInteret = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutCompte statut = StatutCompte.ACTIF;

    @Column(name = "date_ouverture", nullable = false)
    private LocalDate dateOuverture;

    @Column(name = "date_cloture")
    private LocalDate dateCloture;

    @Column(name = "date_modification", nullable = false)
    private LocalDateTime dateModification;

    @Version
    private Long version;

    @OneToMany(mappedBy = "compteDebiteur")
    private List<Transaction> transactionsDebitees;

    @OneToMany(mappedBy = "compteCrediteur")
    private List<Transaction> transactionsCreditees;

    @PrePersist
    public void prePersist() {
        dateOuverture = LocalDate.now();
        dateModification = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        dateModification = LocalDateTime.now();
    }

    //Constructeurs
    public CompteBancaire() {}

    public CompteBancaire(Utilisateur titulaire, String iban, String bic, TypeCompte typeCompte) {
        this.titulaire = titulaire;
        this.iban = iban;
        this.bic = bic;
        this.typeCompte = typeCompte;
    }


    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Utilisateur getTitulaire() { return titulaire; }
    public void setTitulaire(Utilisateur titulaire) { this.titulaire = titulaire; }
    public Utilisateur getConseiller() { return conseiller; }
    public void setConseiller(Utilisateur conseiller) { this.conseiller = conseiller; }
    public String getIban() { return iban; }
    public void setIban(String iban) { this.iban = iban; }
    public String getBic() { return bic; }
    public void setBic(String bic) { this.bic = bic; }
    public TypeCompte getTypeCompte() { return typeCompte; }
    public void setTypeCompte(TypeCompte typeCompte) { this.typeCompte = typeCompte; }
    public BigDecimal getSolde() { return solde; }
    public void setSolde(BigDecimal solde) { this.solde = solde; }
    public String getDevise() { return devise; }
    public void setDevise(String devise) { this.devise = devise; }
    public BigDecimal getDecouvertAutorise() { return decouvertAutorise; }
    public void setDecouvertAutorise(BigDecimal decouvertAutorise) { this.decouvertAutorise = decouvertAutorise; }
    public BigDecimal getTauxInteret() { return tauxInteret; }
    public void setTauxInteret(BigDecimal tauxInteret) { this.tauxInteret = tauxInteret; }
    public StatutCompte getStatut() { return statut; }
    public void setStatut(StatutCompte statut) { this.statut = statut; }
    public LocalDate getDateOuverture() { return dateOuverture; }
    public LocalDate getDateCloture() { return dateCloture; }
    public void setDateCloture(LocalDate dateCloture) { this.dateCloture = dateCloture; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }
    public List<Transaction> getTransactionsDebitees() { return transactionsDebitees; }
    public List<Transaction> getTransactionsCreditees() { return transactionsCreditees; }
}
