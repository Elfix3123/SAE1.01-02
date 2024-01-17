import java.util.ArrayList;

public class UtilitairePaireChaineEntier {
	public static int indiceDicho(ArrayList<PaireChaineEntier> listePaires, String chaine) {
		// { listePaires trié sur la chaine } -> { si chaine est dans listePaires l'indice de sa première occurence,
		// sinon moins l'indice moins un qu'il aurait du avoir }
		if(listePaires.size() <= 0 || chaine.compareTo(listePaires.get(listePaires.size()-1).getChaine()) > 0){
			return -listePaires.size()-1;
		}
		else{
			int inf = 0;
			int sup = listePaires.size()-1;
			int m;

			while(inf < sup){
				m = (inf+sup)/2;

				if(chaine.compareTo(listePaires.get(m).getChaine()) <= 0){
					sup = m;
				}
				else{
					inf = m+1;
				}
			}
			
			if (chaine.compareTo(listePaires.get(inf).getChaine()) == 0) {
				return inf;
			}
			else {
				return -inf-1;
			}
		}
	}

	public static int indicePourChaine(ArrayList<PaireChaineEntier> listePaires, String chaine) {
		// { } -> { retourne l’indice de chaine dans listePaires si chaine est présente et -1 sinon }
		int i = 0;

		// Parcours potentiellement partiel
		// Invariant de boucle : aucun element de listePaires[0..i-1] d'attribut chaine égal à l'argument chaine
		while (i < listePaires.size() && listePaires.get(i).getChaine().compareTo(chaine) != 0) {
			i++;
		}

		if (i == listePaires.size()) {
			return -1;
		}
		else {
			return i;
		}
	}

	public static int entierPourChaine(ArrayList<PaireChaineEntier> listePaires, String chaine) {
		// { } -> { retourne l’entier associé à la chaîne de caractères chaine dans listePaires si elle est présente et 0 sinon }
		int i = 0;

		// Parcours potentiellement partiel
		// Invariant de boucle : aucun element de listePaires[0..i-1] d'attribut chaine égal à l'argument chaine
		while (i < listePaires.size() && listePaires.get(i).getChaine().compareTo(chaine) != 0) {
			i++;
		}

		if (i == listePaires.size()) {
			return 0;
		}
		else {
			return listePaires.get(i).getEntier();
		}
	}

	public static String chaineMax(ArrayList<PaireChaineEntier> listePaires) {
		// { } -> { retourne la chaine associé au plus grand entier de listePaires }
		int i = 1;
		int entierMax = listePaires.get(0).getEntier();
		String strMax = listePaires.get(0).getChaine();

		// Parcours complet (recherche du maximum)
		// Invariant de boucle : entierMax est le maximum des attributs entier des PairesChaineEntier de listePaires[0..i-1]
		while (i < listePaires.size()) {
			if (listePaires.get(i).getEntier() > entierMax) {
				entierMax = listePaires.get(i).getEntier();
				strMax = listePaires.get(i).getChaine();
			}

			i++;
		}

		return strMax;
	}


	public static float moyenne(ArrayList<PaireChaineEntier> listePaires) {
		// { } -> { retourne la moyenne des entiers stockés dans listePaires }
		int i = 0;
		int somme = 0;

		// Parcours complet (calcul de moyenne de tous les éléments d'un vecteur)
		// Invariant : somme est la somme des entiers stockés dans listePaires[0..i-1]
		while (i < listePaires.size()-1) {
			somme+=listePaires.get(i).getEntier();
			i++;
		}

		return ((float)somme)/listePaires.size();
	}
}
