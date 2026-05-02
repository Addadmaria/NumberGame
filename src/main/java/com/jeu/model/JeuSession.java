package com.jeu.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JeuSession implements Serializable {

    private static final long serialVersionUID = 1L;

    private int nombreSecret;
    private int essaisUtilises;
    private int essaisMax;
    private int rangeMin;
    private int rangeMax;
    private int niveau;
    private boolean fin;
    private boolean gagnee;
    private List<String> historique;
    private Long partieId;

    public JeuSession(int niveau) {
        this.historique = new ArrayList<>();
        this.fin = false;
        this.gagnee = false;
        this.essaisUtilises = 0;

        if (niveau == 1) {
            this.niveau = 1; rangeMin = 1; rangeMax = 50; essaisMax = 15;
        } else if (niveau == 3) {
            this.niveau = 3; rangeMin = 1; rangeMax = 200; essaisMax = 7;
        } else {
            this.niveau = 2; rangeMin = 1; rangeMax = 100; essaisMax = 10;
        }
        this.nombreSecret = new Random().nextInt(rangeMax - rangeMin + 1) + rangeMin;
    }

    public JeuSession(int niveau, int nombreSecret, int essaisUtilises, int essaisMax,
                      int rangeMin, int rangeMax, String historiqueStr, Long partieId) {
        this.niveau = niveau;
        this.nombreSecret = nombreSecret;
        this.essaisUtilises = essaisUtilises;
        this.essaisMax = essaisMax;
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
        this.fin = false;
        this.gagnee = false;
        this.partieId = partieId;
        this.historique = new ArrayList<>();
        if (historiqueStr != null && !historiqueStr.isEmpty()) {
            for (String h : historiqueStr.split(";")) {
                if (!h.isBlank()) historique.add(h);
            }
        }
    }

    public String jouer(int nb) {
        if (fin) return "La partie est déjà terminée.";
        essaisUtilises++;
        int restants = essaisMax - essaisUtilises;
        String msg;

        if (nb < nombreSecret) {
            msg = "🔼 " + nb + " est trop petit ! (" + restants + " essai(s) restant(s))";
        } else if (nb > nombreSecret) {
            msg = "🔽 " + nb + " est trop grand ! (" + restants + " essai(s) restant(s))";
        } else {
            fin = true; gagnee = true;
            msg = "🎉 Bravo ! " + nb + " est le bon nombre en " + essaisUtilises + " essai(s) !";
        }

        if (!fin && essaisUtilises >= essaisMax) {
            fin = true; gagnee = false;
            msg = "💀 Plus d'essais ! Le nombre secret était " + nombreSecret;
        }

        historique.add(msg);
        return msg;
    }

    public int calculerScore() {
        if (!gagnee) return 0;
        int base;
        if (niveau == 1) base = 100;
        else if (niveau == 3) base = 500;
        else base = 250;
        int bonus = (essaisMax - essaisUtilises) * 10;
        return Math.max(base + bonus, 0);
    }

    public String getNiveauLabel() {
        if (niveau == 1) return "Facile";
        if (niveau == 3) return "Difficile";
        return "Moyen";
    }

    public String getHistoriqueAsString() {
        return String.join(";", historique);
    }

    public int getEssaisRestants() {
        return essaisMax - essaisUtilises;
    }

    public int getPourcentageProgression() {
        if (essaisMax == 0) return 0;
        return (essaisUtilises * 100) / essaisMax;
    }

    public int getNombreSecret()          { return nombreSecret; }
    public int getEssaisUtilises()        { return essaisUtilises; }
    public void setEssaisUtilises(int e)  { this.essaisUtilises = e; }
    public int getEssaisMax()             { return essaisMax; }
    public int getRangeMin()              { return rangeMin; }
    public int getRangeMax()              { return rangeMax; }
    public int getNiveau()                { return niveau; }
    public boolean isFin()                { return fin; }
    public void setFin(boolean fin)       { this.fin = fin; }
    public boolean isGagnee()             { return gagnee; }
    public void setGagnee(boolean g)      { this.gagnee = g; }
    public List<String> getHistorique()   { return historique; }
    public Long getPartieId()             { return partieId; }
    public void setPartieId(Long id)      { this.partieId = id; }
}
