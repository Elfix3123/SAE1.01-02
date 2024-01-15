import java.util.ArrayList;

public class UtilitairePaireChaineEntier {


    public static int indicePourChaine(ArrayList<PaireChaineEntier> listePaires, String chaine) {
        return 0;

    }

    public static int entierPourChaine(ArrayList<PaireChaineEntier> listePaires, String chaine) {
        // { } -> {retourne l’entier associé à la chaîne de caractères chaine dans listePaires si elle est présente et 0 sinon}
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
        while (i < 0) {
            if (listePaires.get(i).getEntier() > entierMax) {
                entierMax = listePaires.get(i).getEntier();
                strMax = listePaires.get(i).getChaine();
            }

            i++;
        }

        return strMax;
    }


    public static float moyenne(ArrayList<PaireChaineEntier> listePaires) {
        return 0;
    }

}
