import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Classification {


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
		int j;
		String texteFichier = "";
		ArrayList<PaireChaineEntier> scoresDepecheEtudiee;
		String chaineMaxDepecheEtudiee;
		ArrayList<PaireChaineEntier> sommeReussite = new ArrayList<>();
		
		while (i < categories.size()) {
			sommeReussite.add(new PaireChaineEntier(categories.get(i).getNom(), 0));
			i++;
		}
		i = 0;

		while (i < depeches.size()) {
			scoresDepecheEtudiee = new ArrayList<>();
			j = 0;
			while (j < categories.size()) {
				scoresDepecheEtudiee.add(new PaireChaineEntier(categories.get(j).getNom(), categories.get(j).score(depeches.get(i))));
				j++;
			}
			chaineMaxDepecheEtudiee = UtilitairePaireChaineEntier.chaineMax(scoresDepecheEtudiee);
			texteFichier += depeches.get(i).getId() + ":" + chaineMaxDepecheEtudiee + "\n";

			if (chaineMaxDepecheEtudiee.compareTo(depeches.get(i).getCategorie()) == 0) {
				sommeReussite.set(UtilitairePaireChaineEntier.indicePourChaine(sommeReussite, chaineMaxDepecheEtudiee), 
				new PaireChaineEntier(chaineMaxDepecheEtudiee, sommeReussite.get(UtilitairePaireChaineEntier.indicePourChaine(sommeReussite, chaineMaxDepecheEtudiee)).getEntier() + 1) );
			}

			i++;
		}
		i = 0;

		while (i < sommeReussite.size()) {
			texteFichier += sommeReussite.get(i).getChaine() + ":" + sommeReussite.get(i).getEntier() + "%\n";
			i++;
		}

		
		System.out.println(texteFichier);
		try {
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

		// Parcours complet du vecteur des dépêches
		while (i < depeches.size()){
			if(depeches.get(i).getCategorie().compareTo(categorie) == 0){	// On vérifie si la dépêche courante est de la bonne catégorie
				j = 0;

				while (j < depeches.get(i).getMots().size()){	// On parcours chaque mot de la dépèche
					if (UtilitairePaireChaineEntier.indicePourChaine(resultat, depeches.get(i).getMots().get(j)) == -1) {	// Si le mot n'est pas déjà dans le vécteur on l'ajoute
						resultat.add(new PaireChaineEntier(depeches.get(i).getMots().get(j), 0));
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

		// Parcours complet du vecteur des dépêches
		while(i < depeches.size()){
			j = 0;

			while (j < depeches.get(i).getMots().size()){	// On parcours chaque mot de la dépêche
				indiceMotCourant = UtilitairePaireChaineEntier.indicePourChaine(dictionnaire, depeches.get(i).getMots().get(j));
				
				if (indiceMotCourant != -1) {
					if (depeches.get(i).getCategorie().compareTo(categorie) == 0) {
						increment = 1;
					}
					else {
						increment = -1;
					}

					dictionnaire.set(indiceMotCourant, new PaireChaineEntier(dictionnaire.get(indiceMotCourant).getChaine(), 
					dictionnaire.get(indiceMotCourant).getEntier() + increment));
				}

				j++;
			}

			i++;
		}
	}

	public static int poidsPourScore(int score) {
		return 0;
	}

	public static void generationLexique(ArrayList<Depeche> depeches, String categorie, String nomFichier) {

	}

	public static void main(String[] args) {
		//Chargement des dépêches en mémoire
		System.out.println("chargement des dépêches");
		ArrayList<Depeche> depeches = lectureDepeches("./ClassificationAutomatique/depeches.txt");
		
		// Création d'un vecteur de catégories
		ArrayList<Categorie> listeCategories = new ArrayList<>();

		// Ajout des catégories dans le vécteur
		Categorie culture = new Categorie("CULTURE");
		culture.initLexique("ClassificationAutomatique/lex_culture.txt");
		listeCategories.add(culture);

		Categorie economie = new Categorie("ECONOMIE");
		economie.initLexique("ClassificationAutomatique/lex_economie.txt");
		listeCategories.add(economie);

		Categorie environnementscience = new Categorie("ENVIRONNEMENT-SCIENCES");
		environnementscience.initLexique("ClassificationAutomatique/lex_environnementscience.txt");
		listeCategories.add(environnementscience);

		Categorie politique = new Categorie("POLITIQUE");
		politique.initLexique("ClassificationAutomatique/lex_politique.txt");
		listeCategories.add(politique);

		Categorie sports = new Categorie("SPORTS");
		sports.initLexique("ClassificationAutomatique/lex_sports.txt");
		listeCategories.add(sports);

		classementDepeches(depeches, listeCategories, "./ClassificationAutomatique/fichier_resultats.txt");

		ArrayList<PaireChaineEntier> dico = initDico(depeches, "CULTURE");
		calculScores(depeches, "CULTURE", dico);
		System.out.println(dico);
	}
}
