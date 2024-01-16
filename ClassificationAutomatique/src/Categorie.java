import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Categorie {

	private String nom; // le nom de la catégorie p.ex : sport, politique,...
	private ArrayList<PaireChaineEntier> lexique; //le lexique de la catégorie

	// constructeur
	public Categorie(String nom) {
		this.nom = nom;
		this.lexique = new ArrayList<>();
	}


	public String getNom() {
		return nom;
	}


	public  ArrayList<PaireChaineEntier> getLexique() {
		return lexique;
	}


	// initialisation du lexique de la catégorie à partir du contenu d'un fichier texte
	public void initLexique(String nomFichier) {
		try {
			// lecture du fichier d'entrée
			FileInputStream file = new FileInputStream(nomFichier);
			Scanner scanner = new Scanner(file);

			while (scanner.hasNextLine()) {
				String ligne = scanner.nextLine();
				lexique.add(new PaireChaineEntier(ligne.substring(0, ligne.length()-2),
					Integer.parseInt(ligne.substring(ligne.length()-1))));
			}
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	//calcul du score d'une dépêche pour la catégorie
	public PaireResultatCompteur<Integer> score(Depeche d) {
		// { } ==> 
		// {entier correspondant au score de la dépeche donnée pour cette catégorie}
		int compteur = 0;
		int res = 0;

		for (int i = 0; i < d.getMots().size(); i++){
			res += UtilitairePaireChaineEntier.entierPourChaine(this.getLexique(), d.getMots().get(i));
			compteur ++;
		}

		return new PaireResultatCompteur<Integer>(res, compteur);
	}
}
