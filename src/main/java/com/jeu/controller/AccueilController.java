package com.jeu.controller;

import com.jeu.entity.Joueur;
import com.jeu.entity.Partie;
import com.jeu.service.JoueurService;
import com.jeu.service.PartieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AccueilController {

    @Autowired
    private JoueurService joueurService;

    @Autowired
    private PartieService partieService;

    @GetMapping("/accueil")
    public String accueil(Authentication auth, Model model) {
        Joueur joueur = joueurService.findByUsername(auth.getName());
        List<Partie> partiesEnCours = partieService.getPartiesEnCours(joueur);
        List<Joueur> classement    = joueurService.getClassement();

        model.addAttribute("joueur",         joueur);
        model.addAttribute("partiesEnCours", partiesEnCours);
        model.addAttribute("classement",     classement);
        return "accueil";
    }
}
