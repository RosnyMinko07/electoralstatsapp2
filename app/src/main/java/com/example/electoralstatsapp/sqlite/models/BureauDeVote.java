package com.example.electoralstatsapp.sqlite.models;

public class BureauDeVote {
    private long id;
    private String numero;
    private int nbInscrits;
    private int nbVotants;
    private long centreDeVoteId;

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public int getNbInscrits() { return nbInscrits; }
    public void setNbInscrits(int nbInscrits) { this.nbInscrits = nbInscrits; }
    public int getNbVotants() { return nbVotants; }
    public void setNbVotants(int nbVotants) { this.nbVotants = nbVotants; }
    public long getCentreDeVoteId() { return centreDeVoteId; }
    public void setCentreDeVoteId(long centreDeVoteId) { this.centreDeVoteId = centreDeVoteId; }
} 