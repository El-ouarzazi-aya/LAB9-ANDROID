package com.example.projetws.beans;

public class Etudiant {
    private int id;
    private String nom, prenom, ville, sexe;

    public Etudiant() {}

    public Etudiant(int id, String nom, String prenom, String ville, String sexe) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.ville = ville;
        this.sexe = sexe;
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getVille() { return ville; }
    public String getSexe() { return sexe; }

    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setVille(String ville) { this.ville = ville; }
    public void setSexe(String sexe) { this.sexe = sexe; }

    @Override
    public String toString() {
        String icon = sexe != null && sexe.equals("femme") ? "" : "";
        return icon + "  " + nom.toUpperCase() + " " + prenom + "  |   " + ville;
    }
}