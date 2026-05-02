package com.jeu.repository;

import com.jeu.entity.Joueur;
import com.jeu.entity.Partie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartieRepository extends JpaRepository<Partie, Long> {
    List<Partie> findByJoueurAndEnCours(Joueur joueur, boolean enCours);
    List<Partie> findByJoueurOrderByDateSauvegardeDesc(Joueur joueur);
    Optional<Partie> findByIdAndJoueur(Long id, Joueur joueur);
}
