package com.jeu.controller;

import com.jeu.entity.Joueur;
import com.jeu.entity.Partie;
import com.jeu.model.JeuSession;
import com.jeu.service.JoueurService;
import com.jeu.service.PartieService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/jeu")
public class JeuController {

    @Autowired private JoueurService joueurService;
    @Autowired private PartieService partieService;

    @GetMapping("/nouveau/{niveau}")
    public String nouvellepartie(@PathVariable int niveau,
                                  Authentication auth,
                                  HttpSession session,
                                  Model model) {
        if (niveau < 1 || niveau > 3) niveau = 1;
        JeuSession jeu = new JeuSession(niveau);
        session.setAttribute("jeu", jeu);
        model.addAttribute("jeu", jeu);
        model.addAttribute("joueur", joueurService.findByUsername(auth.getName()));
        return "jeu";
    }

    @GetMapping("/reprendre/{id}")
    public String reprendrePartie(@PathVariable Long id,
                                   Authentication auth,
                                   HttpSession session,
                                   Model model) {
        Joueur joueur = joueurService.findByUsername(auth.getName());
        Optional<Partie> opt = partieService.findById(id, joueur);
        if (opt.isPresent()) {
            Partie p = opt.get();
            JeuSession jeu = new JeuSession(
                p.getNiveau(), p.getNombreSecret(), p.getEssaisUtilises(),
                p.getEssaisMax(), p.getRangeMin(), p.getRangeMax(),
                p.getHistoriqueEssais(), p.getId()
            );
            session.setAttribute("jeu", jeu);
            model.addAttribute("jeu", jeu);
            model.addAttribute("joueur", joueur);
            model.addAttribute("message", "Partie reprise ! Continuez à jouer 🎮");
        } else {
            return "redirect:/accueil";
        }
        return "jeu";
    }

    @PostMapping("/jouer")
    public String jouer(@RequestParam int nombre,
                         Authentication auth,
                         HttpSession session,
                         Model model) {
        JeuSession jeu = (JeuSession) session.getAttribute("jeu");
        if (jeu == null) return "redirect:/accueil";

        String message = jeu.jouer(nombre);
        session.setAttribute("jeu", jeu);

        if (jeu.isFin()) {
            Joueur joueur = joueurService.findByUsername(auth.getName());
            Partie partie = partieService.terminer(jeu, joueur);
            joueurService.updateStats(joueur, jeu.calculerScore(), jeu.isGagnee());
            jeu.setPartieId(partie.getId());
            session.setAttribute("jeu", jeu);
        }

        model.addAttribute("jeu", jeu);
        model.addAttribute("message", message);
        model.addAttribute("joueur", joueurService.findByUsername(auth.getName()));
        return "jeu";
    }

    @PostMapping("/sauvegarder")
    public String sauvegarder(Authentication auth,
                               HttpSession session,
                               Model model) {
        JeuSession jeu = (JeuSession) session.getAttribute("jeu");
        if (jeu == null || jeu.isFin()) return "redirect:/accueil";

        Joueur joueur = joueurService.findByUsername(auth.getName());
        Partie partie = partieService.sauvegarder(jeu, joueur);
        jeu.setPartieId(partie.getId());
        session.setAttribute("jeu", jeu);

        model.addAttribute("jeu", jeu);
        model.addAttribute("joueur", joueur);
        model.addAttribute("message", "✅ Partie sauvegardée avec succès !");
        return "jeu";
    }

    @GetMapping("/historique")
    public String historique(Authentication auth, Model model) {
        Joueur joueur = joueurService.findByUsername(auth.getName());
        model.addAttribute("parties", partieService.getHistorique(joueur));
        model.addAttribute("joueur",  joueur);
        return "historique";
    }
}
