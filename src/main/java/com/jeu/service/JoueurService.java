package com.jeu.service;

import com.jeu.entity.Joueur;
import com.jeu.repository.JoueurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JoueurService implements UserDetailsService {

    @Autowired
    private JoueurRepository joueurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Joueur joueur = joueurRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Joueur non trouvé : " + username));

        return User.withUsername(joueur.getUsername())
                .password(joueur.getPassword())
                .roles(joueur.getRole().replace("ROLE_", ""))
                .build();
    }

    public Joueur inscrire(String username, String password) {
        if (joueurRepository.existsByUsername(username)) {
            throw new RuntimeException("Ce nom d'utilisateur est déjà pris !");
        }
        Joueur joueur = new Joueur(username, passwordEncoder.encode(password));
        return joueurRepository.save(joueur);
    }

    public Joueur findByUsername(String username) {
        return joueurRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Joueur non trouvé"));
    }

    public void updateStats(Joueur joueur, int score, boolean gagne) {
        joueur.setPartiesJouees(joueur.getPartiesJouees() + 1);
        if (gagne) {
            joueur.setPartiesGagnees(joueur.getPartiesGagnees() + 1);
            joueur.setScoreTotal(joueur.getScoreTotal() + score);
        }
        joueurRepository.save(joueur);
    }

    public List<Joueur> getClassement() {
        return joueurRepository.findAll()
                .stream()
                .sorted((a, b) -> Integer.compare(b.getScoreTotal(), a.getScoreTotal()))
                .limit(10)
                .toList();
    }
}
