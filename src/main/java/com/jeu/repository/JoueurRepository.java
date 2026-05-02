package com.jeu.repository;

import com.jeu.entity.Joueur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JoueurRepository extends JpaRepository<Joueur, Long> {
    Optional<Joueur> findByUsername(String username);
    boolean existsByUsername(String username);
}
