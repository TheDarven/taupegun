package fr.thedarven.main;

public class TeamCreation {

	private String nom;
	private byte couleur;
	private String prefix;
	
	public TeamCreation() {
		nom = null;
		couleur = 0;
		prefix = null;
	}
	
	public String getNom() {
		return nom;
	}
	
	public byte getCouleur() {
		return couleur;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	
	
	public void setNom(String pNom) {
		nom = pNom;
	}
	
	public void setCouleur(byte pCouleur) {
		couleur = pCouleur;
	}
	
	public void setPrefix(String pPrefix) {
		prefix = pPrefix;
	}
}
