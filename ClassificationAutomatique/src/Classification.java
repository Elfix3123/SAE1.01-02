import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Classification {
	// Fonctions pour la méthode par lexique
	private static ArrayList<Depeche> lectureDepeches(String nomFichier) {
		//creation d'un tableau de dépêches
		ArrayList<Depeche> depeches = new ArrayList<>();
		try {
			// lecture du fichier d'entrée
			FileInputStream file = new FileInputStream(nomFichier);
			Scanner scanner = new Scanner(file);

			while (scanner.hasNextLine()) {
				String ligne = scanner.nextLine();
				String id = ligne.substring(3);
				ligne = scanner.nextLine();
				String date = ligne.substring(3);
				ligne = scanner.nextLine();
				String categorie = ligne.substring(3);
				ligne = scanner.nextLine();
				String lignes = ligne.substring(3);
				while (scanner.hasNextLine() && !ligne.equals("")) {
					ligne = scanner.nextLine();
					if (!ligne.equals("")) {
						lignes = lignes + '\n' + ligne;
					}
				}
				Depeche uneDepeche = new Depeche(id, date, categorie, lignes);
				depeches.add(uneDepeche);
			}
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return depeches;
	}


	public static void classementDepeches(ArrayList<Depeche> depeches, ArrayList<Categorie> categories, String nomFichier) {
		// { } -> { pour chacune des dépêches de depeches, calcule le score pour chaque catégorie de categories et
		// écrit dans le fichier de nom nomFichier, le nom de la catégorie ayant le plus grand score ainsi que les
		// pourcentages correspondants }
		int i = 0;
		int compteur = 0;
		int j;
		String texteFichier = "";
		ArrayList<PaireChaineEntier> scoresDepecheEtudiee;
		String chaineMaxDepecheEtudiee;
		ArrayList<PaireChaineEntier> sommeReussite = new ArrayList<>();
		int totalReussite = 0;
		PaireResultatCompteur<Integer> resultat;
		
		// Création d'un vecteur sommeReussite, qui associe chaque Categorie de categories à 0
		while (i < categories.size()) {
			sommeReussite.add(new PaireChaineEntier(categories.get(i).getNom(), 0));
			i++;
		}
		i = 0;

		// Parcours de toutes les Depeche de depeches
		while (i < depeches.size()) {
			scoresDepecheEtudiee = new ArrayList<>();
			j = 0;

			while (j < categories.size()) {		// Calcul du score de la Depeche dans chaque catégorie
				resultat = categories.get(j).score(depeches.get(i));
				scoresDepecheEtudiee.add(new PaireChaineEntier(categories.get(j).getNom(), resultat.getRes()));
				compteur += resultat.getCompteur();
				j++;
			}

			chaineMaxDepecheEtudiee = UtilitairePaireChaineEntier.chaineMax(scoresDepecheEtudiee);	// Choix de la catégorie au score le plus haut
			texteFichier += depeches.get(i).getId() + ":" + chaineMaxDepecheEtudiee + "\n";

			if (chaineMaxDepecheEtudiee.compareTo(depeches.get(i).getCategorie()) == 0) {	// Vérification du résultat et mise à jour de sommeReussite
				sommeReussite.set(UtilitairePaireChaineEntier.indicePourChaine(sommeReussite, chaineMaxDepecheEtudiee), 
				new PaireChaineEntier(chaineMaxDepecheEtudiee, sommeReussite.get(UtilitairePaireChaineEntier.indicePourChaine(sommeReussite, chaineMaxDepecheEtudiee)).getEntier() + 1) );
			}

			i++;
		}
		i = 0;

		while (i < sommeReussite.size()) {	// Ajout des pourcentages de réussite et de la moyenne à la fin de texteFichier
			texteFichier += sommeReussite.get(i).getChaine() + ":" + sommeReussite.get(i).getEntier() + "%\n";
			totalReussite += sommeReussite.get(i).getEntier();
			i++;
		}

		texteFichier += "MOYENNE:" + (((float)totalReussite)/sommeReussite.size()) + "%\n";
		System.out.println("Compteur de score : " + compteur);

		try {	// Ecriture du fichier
			FileWriter file = new FileWriter(nomFichier);
			file.write(texteFichier);
			file.close();
			System.out.println("le résultat été retranscrit avec succès dans " + nomFichier);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<PaireChaineEntier> initDico(ArrayList<Depeche> depeches, String categorie) {
		// retourne une ArrayList<PaireChaineEntier> contenant tous les mots présents dans au moins une dépêche de la catégorie categorie.
		// scores initialisés à 0 dans un premier temps
		ArrayList<PaireChaineEntier> resultat = new ArrayList<>();

		int i = 0;
		int j;
		int indiceChaine;

		// Parcours complet du vecteur des dépêches
		while (i < depeches.size()){
			if(depeches.get(i).getCategorie().compareTo(categorie) == 0){	// On vérifie si la dépêche courante est de la bonne catégorie
				j = 0;

				while (j < depeches.get(i).getMots().size()){	// On parcours chaque mot de la dépèche
					/* 
					if (UtilitairePaireChaineEntier.indicePourChaine(resultat, depeches.get(i).getMots().get(j)) == -1) {	// Version non dichotomique
						resultat.add(new PaireChaineEntier(depeches.get(i).getMots().get(j), 0));
					}
					*/
					indiceChaine = UtilitairePaireChaineEntier.indiceDichoChaine(resultat, depeches.get(i).getMots().get(j));

					if (indiceChaine < 0) {	// Si le mot n'est pas déjà dans le vécteur on l'ajoute
						resultat.add(-indiceChaine-1, new PaireChaineEntier(depeches.get(i).getMots().get(j), 0));
					}
					j++;
				}
			}

			i++;
		}
		return resultat;
	}

	public static void calculScores(ArrayList<Depeche> depeches, String categorie, ArrayList<PaireChaineEntier> dictionnaire) {
		// met à jour les scores des mots présents dans dictionnaire. Lorsqu'un mot présent dans dictionnaire apparaît dans une dépêche de depeches, son score est :
		// - décrémenté si la dépêche n'est pas dans la catégorie categorie et
		// - incrémenté si la dépêche est dans la catégorie categorie
		int i = 0;
		int j;
		int increment;
		int indiceMotCourant;
		int compteur = 0;

		// Parcours complet du vecteur des dépêches
		while(i < depeches.size()){
			j = 0;

			while (j < depeches.get(i).getMots().size()){	// On parcours chaque mot de la dépêche
				// indiceMotCourant = UtilitairePaireChaineEntier.indicePourChaine(dictionnaire, depeches.get(i).getMots().get(j));	// Version non dichotomique
				indiceMotCourant = UtilitairePaireChaineEntier.indiceDichoChaine(dictionnaire, depeches.get(i).getMots().get(j));

				if (indiceMotCourant >= 0) {	// On vérifie si le mot courant est dans le dictionnaire
					if (depeches.get(i).getCategorie().compareTo(categorie) == 0) {
						increment = 1;
					} else {
						increment = -1;
					}

					dictionnaire.set(indiceMotCourant, new PaireChaineEntier(dictionnaire.get(indiceMotCourant).getChaine(), // On le met à jour avec le score approprié
					dictionnaire.get(indiceMotCourant).getEntier() + increment));
					compteur++;
				}

				j++;
			}

			i++;
		}
		System.out.println("Compteur de calculScore : " + compteur);
	}

	public static int poidsPourScore(int score) {
		// { } -> { retourne le poids associé à score }
		if (score > 2) {
			return 3;
		}
		else if (score > -4) {
			return 2;
		}
		else {
			return 1;
		}
	}

	public static void generationLexique(ArrayList<Depeche> depeches, String categorie, String nomFichier) {
		// { } -> { crée pour la catégorie categorie le fichier lexique de nom nomFichier à partir du vecteur de
		// dépêches depeches }
		ArrayList<PaireChaineEntier> dico = initDico(depeches, categorie);
		calculScores(depeches, categorie, dico);
		String texteFichier = "";
		int i = 0;
		
		while (i < dico.size()) {	// On ajoute dans texteFichier les paires des mots de dico et leur poids associé
			texteFichier += dico.get(i).getChaine() + ":" + poidsPourScore(dico.get(i).getEntier()) + "\n";
			i++;
		}

		try {	// Ecriture du fichier
			FileWriter file = new FileWriter(nomFichier);
			file.write(texteFichier);
			file.close();
			System.out.println("le résultat été retranscrit avec succès dans " + nomFichier);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Fonctions pour la méthode des K plus proches voisins
	public static ArrayList<String> triMotsDepeche(Depeche depeche) {
		// { } -> { vecteur des mots de depeche triés et sans les mots vides }
		ArrayList<String> motsVides = new ArrayList<>();
		try {
			FileInputStream file = new FileInputStream("./ClassificationAutomatique/mots_vides.txt");
			Scanner scanner = new Scanner(file);

			while (scanner.hasNextLine()) {
				motsVides.add(scanner.nextLine());
			}
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ArrayList<String> texte = new ArrayList<>(depeche.getMots());
		UtilitairePaireChaineEntier.triFusion(texte, 0, texte.size()-1);

		int i = 0, j = 0;

		// Invariant : aucun mot de motsVides[0..j-1] dans texte[0..i-1]
		while (i < texte.size() & j < motsVides.size()) {
			if (texte.get(i).compareTo(motsVides.get(j)) == 0) {		// On vérifie si le mot est un mot vide
				texte.remove(i);
			}
			else if (texte.get(i).compareTo(motsVides.get(j)) < 0) {
				i++;
			}
			else {
				j++;
			}
		}

		return texte;
	}

	public static int compareTextes(ArrayList<String> texte1, ArrayList<String> texte2) {
		// { texte1 et texte2 triés } -> { nombre de mots en commun entre texte1 et texte2 }
		int i = 0, j = 0;
		int total = 0;

		// Invariant : total est le nombre de mots communs à texte1[0..i] et texte2[0..j]
		while (i < texte1.size() & j < texte2.size()) {
			if (texte1.get(i).compareTo(texte2.get(j)) == 0) {
				total++;
				i++;
			}
			else if (texte1.get(i).compareTo(texte2.get(j)) < 0) {
				i++;
			}
			else {
				j++;
			}
		}

		return total;
	}

	public static ArrayList<PaireChaineEntier> kPlusProchesVoisinsDepeche (ArrayList<PaireChaineVecteur<String>> depechesAprentissageTrie, Depeche depeche, int k) {
		// { depecheAprentissageTrie contient des vecteurs triés } -> { vecteur des couples majoritaires des catégories et de leurs scores 
		//	respectifs des dépêches de depecheAprentissage plus proches de depeche }
		int scoreCourant;
		int indiceCourant;
		ArrayList<PaireChaineEntier> kPlusProchesVoisins = new ArrayList<>();
		int i = 0;

		while (i < depechesAprentissageTrie.size()) {		// On compare chaque dépêche de depechesSource à celles de depechesAprentissage
			scoreCourant = compareTextes(depechesAprentissageTrie.get(i).getVecteur(), triMotsDepeche(depeche));
			indiceCourant = UtilitairePaireChaineEntier.indiceDichoEntier(kPlusProchesVoisins, scoreCourant);
	
			if (indiceCourant <= k) {
				kPlusProchesVoisins.add(indiceCourant, new PaireChaineEntier(depechesAprentissageTrie.get(i).getChaine(), scoreCourant));
			}

			if (kPlusProchesVoisins.size() > k) {
				kPlusProchesVoisins.remove(kPlusProchesVoisins.size()-1);
			}

			i++;
		}

		return kPlusProchesVoisins;
	}

	public static void classementDepechesKNN (ArrayList<Depeche> depechesAprentissage, ArrayList<Depeche> depechesSource, int k, String nomFichier) {
		// { } -> { pour chacune des dépêches de depeches, recherche la catégorie majoritaire des k dépêches de depechesAprentissage 
		// qui partagent le plus de mots avec les dépêches de depechesSource, et écrit dans le fichier de nom nomFichier
		// le nom de cette catégorie ainsi que les pourcentages correspondants }
		int i = 0;
		String texteFichier = "";
		ArrayList<PaireChaineEntier> sommeReussite = new ArrayList<>();
		ArrayList<PaireChaineVecteur<String>> depechesAprentissageTrie = new ArrayList<>();
		String categorieCourante;

		int totalReussite = 0;
		// On trie les mots de toutes les dépêches de depechesAprentissage
		while (i < depechesAprentissage.size()) {
			depechesAprentissageTrie.add(new PaireChaineVecteur<>(depechesAprentissage.get(i).getCategorie(), triMotsDepeche(depechesAprentissage.get(i))));
			i++;
		}

		// Parcours de toutes les Depeche de depechesSource
		i = 0;
		while (i < depechesSource.size()) {
			if (UtilitairePaireChaineEntier.indicePourChaine(sommeReussite, depechesSource.get(i).getCategorie()) == -1) {		// Ajout de la catégorie courante à sommeReussite si elle n'y est pas deja
				sommeReussite.add(new PaireChaineEntier(depechesSource.get(i).getCategorie(), 0));
			}

			categorieCourante = kPlusProchesVoisinsDepeche(depechesAprentissageTrie, depechesSource.get(i), k).get(0).getChaine();
			texteFichier += depechesSource.get(i).getId() + ":" + categorieCourante + "\n";

			if (categorieCourante.compareTo(depechesSource.get(i).getCategorie()) == 0) {	// Vérification du résultat et mise à jour de sommeReussite
				sommeReussite.set(UtilitairePaireChaineEntier.indicePourChaine(sommeReussite, categorieCourante), 
				new PaireChaineEntier(categorieCourante, sommeReussite.get(UtilitairePaireChaineEntier.indicePourChaine(sommeReussite, categorieCourante)).getEntier() + 1) );
			}

			System.out.println(categorieCourante + "   " + depechesSource.get(i).getCategorie());
			
			i++;
		}
		i = 0;

		while (i < sommeReussite.size()) {	// Ajout des pourcentages de réussite et de la moyenne à la fin de texteFichier
			texteFichier += sommeReussite.get(i).getChaine() + ":" + sommeReussite.get(i).getEntier() + "%\n";
			totalReussite += sommeReussite.get(i).getEntier();
			i++;
		}

		texteFichier += "MOYENNE:" + (((float)totalReussite)/sommeReussite.size()) + "%\n";

		try {	// Ecriture du fichier
			FileWriter file = new FileWriter(nomFichier);
			file.write(texteFichier);
			file.close();
			System.out.println("le résultat été retranscrit avec succès dans " + nomFichier);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		//Chargement des dépêches en mémoire
		System.out.println("chargement des dépêches");
		ArrayList<Depeche> depeches = lectureDepeches("./ClassificationAutomatique/depeches.txt");
		
		// Création d'un vecteur de catégories
		ArrayList<Categorie> listeCategories = new ArrayList<>();

		// Ajout des catégories dans le vécteur
		Categorie culture = new Categorie("CULTURE");
		listeCategories.add(culture);

		Categorie economie = new Categorie("ECONOMIE");
		listeCategories.add(economie);

		Categorie environnementscience = new Categorie("ENVIRONNEMENT-SCIENCES");
		listeCategories.add(environnementscience);

		Categorie politique = new Categorie("POLITIQUE");
		listeCategories.add(politique);

		Categorie sports = new Categorie("SPORTS");
		listeCategories.add(sports);

		// Initialisation des catégories
		int i = 0;
		
		while (i < listeCategories.size()) {
			generationLexique(depeches, listeCategories.get(i).getNom(), "./ClassificationAutomatique/lex_" + listeCategories.get(i).getNom() + ".txt");	// Création d'un lexique pour chaque catégorie
			listeCategories.get(i).initLexique("./ClassificationAutomatique/lex_" + listeCategories.get(i).getNom() + ".txt");	// Initialisation de chaque catégorie avec le lexique créé
			i++;
		}

		// Calcul des résultats avec chaque dépêche de test
		depeches = lectureDepeches("./ClassificationAutomatique/test.txt");
		classementDepeches(depeches, listeCategories, "./ClassificationAutomatique/fichier_resultats.txt");
	
		long endTime = System.currentTimeMillis();
		System.out.println("Calculs réalisés en : " + (endTime-startTime) + "ms");
	}
	/*
	public static void main(String[] args) {
		
		long startTime = System.currentTimeMillis();

		System.out.println("chargement des dépêches");
		ArrayList<Depeche> depechesAprentissage = lectureDepeches("./ClassificationAutomatique/depeches.txt");
		ArrayList<Depeche> depechesSource = lectureDepeches("./ClassificationAutomatique/test.txt");
		classementDepechesKNN(depechesAprentissage, depechesSource, 10, "./ClassificationAutomatique/fichier_resultats_knn.txt");

		long endTime = System.currentTimeMillis();
		System.out.println("Calculs réalisés en : " + (endTime-startTime) + "ms");
	}
	*/
}
