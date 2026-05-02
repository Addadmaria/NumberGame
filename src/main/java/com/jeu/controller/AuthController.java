package com.jeu.controller;

import com.jeu.service.JoueurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private JoueurService joueurService;

    @GetMapping("/")
    public String index() {
        return "redirect:/accueil";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String logout,
                            Model model) {
        if (error != null)  model.addAttribute("error",  "Nom d'utilisateur ou mot de passe incorrect.");
        if (logout != null) model.addAttribute("logout", "Vous êtes déconnecté.");
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() { return "register"; }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String confirmPassword,
                           Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Les mots de passe ne correspondent pas.");
            return "register";
        }
        if (username.trim().length() < 3) {
            model.addAttribute("error", "Le nom d'utilisateur doit avoir au moins 3 caractères.");
            return "register";
        }
        if (password.length() < 4) {
            model.addAttribute("error", "Le mot de passe doit avoir au moins 4 caractères.");
            return "register";
        }
        try {
            joueurService.inscrire(username.trim(), password);
            return "redirect:/login?registered=true";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}
