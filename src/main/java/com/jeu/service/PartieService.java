package com.jeu.service;

import com.jeu.entity.Joueur;
import com.jeu.entity.Partie;
import com.jeu.model.JeuSession;
import com.jeu.repository.PartieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PartieService {

    @Autowired
    private PartieRepository partieRepository;

    public Partie sauvegarder(JeuSession jeu, Joueur joueur) {
        Partie partie = jeu.getPartieId() != null
                ? partieRepository.findById(jeu.getPartieId()).orElse(new Partie())
                : new Partie();

        partie.setJoueur(joueur);
        partie.setNiveau(jeu.getNiveau());
        partie.setNombreSecret(jeu.getNombreSecret());
        partie.setEssaisUtilises(jeu.getEssaisUtilises());
        partie.setEssaisMax(jeu.getEssaisMax());
        partie.setRangeMin(jeu.getRangeMin());
        partie.setRangeMax(jeu.getRangeMax());
        partie.setEnCours(true);
        partie.setGagnee(false);
        partie.setScore(0);
        partie.setHistoriqueEssais(jeu.getHistoriqueAsString());
        return partieRepository.save(partie);
    }

    public Partie terminer(JeuSession jeu, Joueur joueur) {
        Partie partie = jeu.getPartieId() != null
                ? partieRepository.findById(jeu.getPartieId()).orElse(new Partie())
                : new Partie();

        partie.setJoueur(joueur);
        partie.setNiveau(jeu.getNiveau());
        partie.setNombreSecret(jeu.getNombreSecret());
        partie.setEssaisUtilises(jeu.getEssaisUtilises());
        partie.setEssaisMax(jeu.getEssaisMax());
        partie.setRangeMin(jeu.getRangeMin());
        partie.setRangeMax(jeu.getRangeMax());
        partie.setEnCours(false);
        partie.setGagnee(jeu.isGagnee());
        partie.setScore(jeu.calculerScore());
        partie.setHistoriqueEssais(jeu.getHistoriqueAsString());
        return partieRepository.save(partie);
    }

    public List<Partie> getPartiesEnCours(Joueur joueur) {
        return partieRepository.findByJoueurAndEnCours(joueur, true);
    }

    public List<Partie> getHistorique(Joueur joueur) {
        return partieRepository.findByJoueurOrderByDateSauvegardeDesc(joueur);
    }

    public Optional<Partie> findById(Long id, Joueur joueur) {
        return partieRepository.findByIdAndJoueur(id, joueur);
    }
}
