public class PaireChaineEntier {
	private String chaine;
	private int entier;

	public String getChaine() {
		return this.chaine;
	}

	public int getEntier() {
		return this.entier;
	}

	PaireChaineEntier(String chaine, int entier) {
		this.chaine = chaine;
		this.entier = entier;
	}

	@Override
	public String toString() {
		// pour l'affichage de l'instance courante de la classe
		return "(" + this.chaine + ", " + this.entier + ")";
	}
}
