package com.example.electoralstatsapp.sqlite.models;

public class CentreDeVote {
    private long id;
    private String nom;
    private String adresse;
    private double latitude;
    private double longitude;
    private long circonscriptionId;

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public long getCirconscriptionId() { return circonscriptionId; }
    public void setCirconscriptionId(long circonscriptionId) { this.circonscriptionId = circonscriptionId; }

    @Override
    public String toString() {
        return nom;
    }
} 