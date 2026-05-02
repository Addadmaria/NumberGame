package com.jeu.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "parties")
@Data
@NoArgsConstructor
public class Partie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "joueur_id", nullable = false)
    private Joueur joueur;

    private int niveau;
    private int nombreSecret;
    private int essaisUtilises;
    private int essaisMax;
    private int rangeMin;
    private int rangeMax;
    private boolean enCours;
    private boolean gagnee;
    private int score;

    @Column(length = 1000)
    private String historiqueEssais = "";

    private LocalDateTime dateSauvegarde;
    private LocalDateTime dateCreation;

    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
        dateSauvegarde = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        dateSauvegarde = LocalDateTime.now();
    }

    public String getNiveauLabel() {
        if (niveau == 1) return "Facile";
        if (niveau == 2) return "Moyen";
        if (niveau == 3) return "Difficile";
        return "Inconnu";
    }
}
