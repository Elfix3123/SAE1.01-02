import java.util.ArrayList;

public class UtilitairePaireChaineEntier {
	public static int indiceDichoChaine(ArrayList<PaireChaineEntier> listePaires, String chaine) {
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

	public static int indiceDichoEntier(ArrayList<PaireChaineEntier> listePaires, int entier) {
		// { listePaires trié sur l'entier } -> { si entier est dans listePaires l'indice de sa première occurence,
		// sinon moins l'indice moins un qu'iwl aurait du avoir }
		if(listePaires.size() <= 0 || entier > listePaires.get(listePaires.size()-1).getEntier()){
			return -listePaires.size()-1;
		}
		else{
			int inf = 0;
			int sup = listePaires.size()-1;
			int m;

			while(inf < sup){
				m = (inf+sup)/2;

				if(entier <= listePaires.get(m).getEntier()){
					sup = m;
				}
				else{
					inf = m+1;
				}
			}
			
			if (entier == listePaires.get(inf).getEntier()) {
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

	public static void triFusion(ArrayList<String> mots, int inf, int sup) {
		// { mots[inf..sup] non vide } => { mots[inf..sup] trié }
		if (inf < sup) {
			int m = (inf + sup) / 2;
			triFusion(mots, inf, m); // appel récursif sur tranche gauche
			triFusion(mots, m + 1, sup); // appel récursif sur tranche droite
			fusionTabGTabD(mots, inf, m, sup); // appel du worker : tri de inf à sup
		}
	}

	public static void fusionTabGTabD(ArrayList<String> mots, int inf, int m, int sup) {
		// { inf <= sup, m = (inf+sup)/2, mots[inf..m] trié, mots[m+1..sup] trié }
		// => { mots[inf..sup] trié }
		ArrayList<String> vTemp = new ArrayList<>();
		int iGauche = inf;
		int iDroite = m+1;

		while(iGauche <= m && iDroite <= sup){
			if(mots.get(iGauche).compareTo(mots.get(iDroite)) < 0){
				vTemp.add(mots.get(iGauche));
				iGauche++;
			}
			else{
				vTemp.add(mots.get(iDroite));
				iDroite++;
			}
		}
		
		while(iGauche <= m){
			vTemp.add(mots.get(iGauche));
			iGauche++;
		}

		while(iDroite <= sup){
			vTemp.add(mots.get(iDroite));
			iDroite++;
		}

		int i = inf;

		while(i <= sup){
			mots.set(i, vTemp.get(i-inf));
			i++;
		}
	}
}