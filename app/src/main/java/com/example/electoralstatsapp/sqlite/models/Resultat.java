package com.example.electoralstatsapp.sqlite.models;

import java.util.Date;

public class Resultat {
    private long id;
    private int nbVoix;
    private float pourcentage;
    private Date dateSaisie;
    private long electionId;
    private long candidatId;
    private long bureauVoteId;

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public int getNbVoix() { return nbVoix; }
    public void setNbVoix(int nbVoix) { this.nbVoix = nbVoix; }
    public float getPourcentage() { return pourcentage; }
    public void setPourcentage(float pourcentage) { this.pourcentage = pourcentage; }
    public Date getDateSaisie() { return dateSaisie; }
    public void setDateSaisie(Date dateSaisie) { this.dateSaisie = dateSaisie; }
    public long getElectionId() { return electionId; }
    public void setElectionId(long electionId) { this.electionId = electionId; }
    public long getCandidatId() { return candidatId; }
    public void setCandidatId(long candidatId) { this.candidatId = candidatId; }
    public long getBureauVoteId() { return bureauVoteId; }
    public void setBureauVoteId(long bureauVoteId) { this.bureauVoteId = bureauVoteId; }
} 