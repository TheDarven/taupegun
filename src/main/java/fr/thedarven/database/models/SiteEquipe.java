package fr.thedarven.database.models;

public class SiteEquipe {

    private int id;
    private String nom;
    private int id_partie;
    private String couleur;
    private int mort;

    public SiteEquipe(String nom, int id_partie, String couleur, int mort) {
        this.id = -1;
        this.nom = nom;
        this.id_partie = id_partie;
        this.couleur = couleur;
        this.mort = mort;
    }

    public SiteEquipe(int id, String nom, int id_partie, String couleur, int mort) {
        this.id = id;
        this.nom = nom;
        this.id_partie = id_partie;
        this.couleur = couleur;
        this.mort = mort;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getId_partie() {
        return id_partie;
    }

    public void setId_partie(int id_partie) {
        this.id_partie = id_partie;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public int getMort() {
        return mort;
    }

    public void setMort(int mort) {
        this.mort = mort;
    }
}
