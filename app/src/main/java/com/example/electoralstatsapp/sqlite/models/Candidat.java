package com.example.electoralstatsapp.sqlite.models;

public class Candidat {
    private long id;
    private String nom;
    private String prenom;
    private String parti;
    private String biographie;
    private String photo;
    private String saisiPar;

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getParti() { return parti; }
    public void setParti(String parti) { this.parti = parti; }
    public String getBiographie() { return biographie; }
    public void setBiographie(String biographie) { this.biographie = biographie; }
    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }
    public String getSaisiPar() { return saisiPar; }
    public void setSaisiPar(String saisiPar) { this.saisiPar = saisiPar; }
} 