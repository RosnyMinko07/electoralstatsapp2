package com.example.electoralstatsapp.sqlite.models;

public class Circonscription {
    private long id;
    private String nom;
    private String region;
    private int nbInscrits;

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public int getNbInscrits() { return nbInscrits; }
    public void setNbInscrits(int nbInscrits) { this.nbInscrits = nbInscrits; }

    @Override
    public String toString() {
        return nom;
    }
} 