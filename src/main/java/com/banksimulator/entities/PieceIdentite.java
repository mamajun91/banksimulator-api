package com.banksimulator.entities;

import com.banksimulator.enums.TypePieceIdentite;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "piece_identite")
public class PieceIdentite {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_piece", nullable = false)
    private TypePieceIdentite typePiece;

    @Column(nullable = false, unique = true, length = 50)
    private String numero;

    @Column(name = "date_expiration", nullable = false)
    private LocalDate dateExpiration;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @PrePersist
    public void prePersist() {
        dateCreation = LocalDateTime.now();
    }

    //Constructeurs
    public PieceIdentite() {}

    public PieceIdentite(Utilisateur utilisateur, TypePieceIdentite typePiece, String numero, LocalDate dateExpiration) {
        this.utilisateur = utilisateur;
        this.typePiece = typePiece;
        this.numero = numero;
        this.dateExpiration = dateExpiration;
    }


    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }
    public TypePieceIdentite getTypePiece() { return typePiece; }
    public void setTypePiece(TypePieceIdentite typePiece) { this.typePiece = typePiece; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public LocalDate getDateExpiration() { return dateExpiration; }
    public void setDateExpiration(LocalDate dateExpiration) { this.dateExpiration = dateExpiration; }
    public LocalDateTime getDateCreation() { return dateCreation; }
}
