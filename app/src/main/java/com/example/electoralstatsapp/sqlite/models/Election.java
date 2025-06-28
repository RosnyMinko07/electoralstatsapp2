package com.example.electoralstatsapp.sqlite.models;

import java.util.Date;

public class Election {
    private long id;
    private String type;
    private Date dateScrutin;
    private String statut;
    private int nbTours;

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Date getDateScrutin() { return dateScrutin; }
    public void setDateScrutin(Date dateScrutin) { this.dateScrutin = dateScrutin; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public int getNbTours() { return nbTours; }
    public void setNbTours(int nbTours) { this.nbTours = nbTours; }

    @Override
    public String toString() {
        return type;
    }
} 