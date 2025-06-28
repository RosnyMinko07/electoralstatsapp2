package com.example.electoralstatsapp.sqlite.models;

// Ce n'est pas une table de la DB, mais un objet pour faciliter l'affichage
public class ResultatView {

    private String electionType;
    private String candidatFullName;
    private String bureauNumero;
    private int nbVoix;

    // Getters and Setters
    public String getElectionType() { return electionType; }
    public void setElectionType(String electionType) { this.electionType = electionType; }

    public String getCandidatFullName() { return candidatFullName; }
    public void setCandidatFullName(String candidatFullName) { this.candidatFullName = candidatFullName; }

    public String getBureauNumero() { return bureauNumero; }
    public void setBureauNumero(String bureauNumero) { this.bureauNumero = bureauNumero; }

    public int getNbVoix() { return nbVoix; }
    public void setNbVoix(int nbVoix) { this.nbVoix = nbVoix; }
} 