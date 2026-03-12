package com.banksimulator.entities;

import com.banksimulator.enums.EntiteCible;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_trail")
public class AuditTrail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "action_id", nullable = false)
    private Action action;

    @Enumerated(EnumType.STRING)
    @Column(name = "entite_cible", nullable = false)
    private EntiteCible entiteCible;

    @Column(name = "entite_id")
    private UUID entiteId;

    @Column(name = "ancien_etat", columnDefinition = "jsonb")
    private String ancienEtat;

    @Column(name = "nouvel_etat", columnDefinition = "jsonb")
    private String nouvelEtat;

    @Column(length = 200)
    private String endpoint;

    @Column(name = "adresse_ip", length = 45)
    private String adresseIp;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @PrePersist
    public void prePersist() {
        timestamp = LocalDateTime.now();
    }

    //Constructeurs
    public AuditTrail() {}

    public AuditTrail(Utilisateur utilisateur, Action action, EntiteCible entiteCible, UUID entiteId) {
        this.utilisateur = utilisateur;
        this.action = action;
        this.entiteCible = entiteCible;
        this.entiteId = entiteId;
    }



    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }
    public Action getAction() { return action; }
    public void setAction(Action action) { this.action = action; }
    public EntiteCible getEntiteCible() { return entiteCible; }
    public void setEntiteCible(EntiteCible entiteCible) { this.entiteCible = entiteCible; }
    public UUID getEntiteId() { return entiteId; }
    public void setEntiteId(UUID entiteId) { this.entiteId = entiteId; }
    public String getAncienEtat() { return ancienEtat; }
    public void setAncienEtat(String ancienEtat) { this.ancienEtat = ancienEtat; }
    public String getNouvelEtat() { return nouvelEtat; }
    public void setNouvelEtat(String nouvelEtat) { this.nouvelEtat = nouvelEtat; }
    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    public String getAdresseIp() { return adresseIp; }
    public void setAdresseIp(String adresseIp) { this.adresseIp = adresseIp; }
    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
