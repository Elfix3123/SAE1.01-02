import java.util.ArrayList;

public class PaireChaineVecteur<e> {
	private String chaine;
	private ArrayList<e> vecteur;

	public String getChaine() {
		return this.chaine;
	}

	public ArrayList<e> getVecteur() {
		return this.vecteur;
	}

	PaireChaineVecteur(String chaine, ArrayList<e> vecteur) {
		this.chaine = chaine;
		this.vecteur = vecteur;
	}

	@Override
	public String toString() {
		// pour l'affichage de l'instance courante de la classe
		return "(" + this.chaine + ", " + this.vecteur + ")";
	}
}
