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
    }


    public static ArrayList<PaireChaineEntier> initDico(ArrayList<Depeche> depeches, String categorie) {
        ArrayList<PaireChaineEntier> resultat = new ArrayList<>();
        return resultat;

    }

    public static void calculScores(ArrayList<Depeche> depeches, String categorie, ArrayList<PaireChaineEntier> dictionnaire) {
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
        Categorie culture = new Categorie("culture");
        culture.initLexique("ClassificationAutomatique/lex_culture.txt");
        listeCategories.add(culture);

        Categorie economie = new Categorie("economie");
        economie.initLexique("ClassificationAutomatique/lex_economie.txt");
        listeCategories.add(economie);

        Categorie environnementscience = new Categorie("environnementscience");
        environnementscience.initLexique("ClassificationAutomatique/lex_environnementscience.txt");
        listeCategories.add(environnementscience);

        Categorie politique = new Categorie("politique");
        politique.initLexique("ClassificationAutomatique/lex_politique.txt");
        listeCategories.add(politique);

        Categorie sports = new Categorie("sports");
        sports.initLexique("ClassificationAutomatique/lex_sports.txt");
        listeCategories.add(sports);

        Depeche depecheEtudiee = depeches.get(0);
        depecheEtudiee.afficher();

        ArrayList<PaireChaineEntier> scoresDepecheEtudiee = new ArrayList<>();
        scoresDepecheEtudiee.add(new PaireChaineEntier("culture", culture.score(depecheEtudiee)));
        scoresDepecheEtudiee.add(new PaireChaineEntier("economie", economie.score(depecheEtudiee)));
        scoresDepecheEtudiee.add(new PaireChaineEntier("environnementscience", environnementscience.score(depecheEtudiee)));
        scoresDepecheEtudiee.add(new PaireChaineEntier("politique", politique.score(depecheEtudiee)));
        scoresDepecheEtudiee.add(new PaireChaineEntier("sports", sports.score(depecheEtudiee)));
    }
}