package com.banksimulator.entities;

import com.banksimulator.enums.Role;
import com.banksimulator.enums.StatutUtilisateur;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "utilisateur")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(length = 20)
    private String telephone;

    @Column(name = "date_naissance", nullable = false)
    private LocalDate dateNaissance;

    @Column(name = "adresse_rue", nullable = false, length = 255)
    private String adresseRue;

    @Column(name = "adresse_ville", nullable = false, length = 100)
    private String adresseVille;

    @Column(name = "code_postal", nullable = false, length = 10)
    private String codePostal;

    @Column(nullable = false, length = 2)
    private String pays = "FR";

    @Column(name = "mot_de_passe_hash", nullable = false, length = 255)
    private String motDePasseHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutUtilisateur statut = StatutUtilisateur.ACTIF;

    @Column(name = "nb_echecs_auth", nullable = false)
    private int nbEchecsAuth = 0;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_modification", nullable = false)
    private LocalDateTime dateModification;

    @Version
    private Long version;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private List<PieceIdentite> piecesIdentite;

    @OneToMany(mappedBy = "titulaire")
    private List<CompteBancaire> comptes;

    @OneToMany(mappedBy = "initiateur")
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "utilisateur")
    private List<AuditTrail> audits;

    @PrePersist
    public void prePersist() {
        dateCreation = LocalDateTime.now();
        dateModification = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        dateModification = LocalDateTime.now();
    }

    //Constructeurs
    public Utilisateur() {}

    public Utilisateur(String nom, String prenom, String email, String motDePasseHash, Role role) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasseHash = motDePasseHash;
        this.role = role;
    }


    // Getters & Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }
    public String getAdresseRue() { return adresseRue; }
    public void setAdresseRue(String adresseRue) { this.adresseRue = adresseRue; }
    public String getAdresseVille() { return adresseVille; }
    public void setAdresseVille(String adresseVille) { this.adresseVille = adresseVille; }
    public String getCodePostal() { return codePostal; }
    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }
    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }
    public String getMotDePasseHash() { return motDePasseHash; }
    public void setMotDePasseHash(String motDePasseHash) { this.motDePasseHash = motDePasseHash; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public StatutUtilisateur getStatut() { return statut; }
    public void setStatut(StatutUtilisateur statut) { this.statut = statut; }
    public int getNbEchecsAuth() { return nbEchecsAuth; }
    public void setNbEchecsAuth(int nbEchecsAuth) { this.nbEchecsAuth = nbEchecsAuth; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }
    public List<PieceIdentite> getPiecesIdentite() { return piecesIdentite; }
    public void setPiecesIdentite(List<PieceIdentite> piecesIdentite) { this.piecesIdentite = piecesIdentite; }
    public List<CompteBancaire> getComptes() { return comptes; }
    public void setComptes(List<CompteBancaire> comptes) { this.comptes = comptes; }
    public List<Transaction> getTransactions() { return transactions; }
    public void setTransactions(List<Transaction> transactions) { this.transactions = transactions; }
    public List<AuditTrail> getAudits() { return audits; }
    public void setAudits(List<AuditTrail> audits) { this.audits = audits; }
}