package com.jeu.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "joueurs")
@Data
@NoArgsConstructor
public class Joueur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role = "ROLE_USER";

    private int scoreTotal = 0;
    private int partiesGagnees = 0;
    private int partiesJouees = 0;

    @OneToMany(mappedBy = "joueur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Partie> parties;

    public Joueur(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = "ROLE_USER";
    }
}
