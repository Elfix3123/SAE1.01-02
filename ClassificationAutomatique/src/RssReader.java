import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;



public class RssReader {


    public static ArrayList<Depeche> lectureRss(String nomFichier) {
		//creation d'un tableau de dépêches
		ArrayList<Depeche> rss = new ArrayList<>();
		try {
			// lecture du fichier d'entrée
			String texte = new String(Files.readAllBytes(Paths.get(nomFichier)));

			

			
			int i = 0;
			int j, k;
			int depCourante = 0;
			String date = null;
			String categorie = null;
			String contenu = null;

			while (i < texte.length()){
				if (texte.subSequence(i, i+6) == "<item>") {
					depCourante += 1;
					j = i;
					while(texte.subSequence(j, j+7) != "<\\item>"){
						if (texte.subSequence(j, j+10) == "<category>") {
							k = j;
							while(texte.subSequence(k, k+11) != "<\\category>"){
								k++;
							}
							categorie = texte.substring(j, k);
						}
						if (texte.subSequence(j, j+13) == "<description>") {
							k = j;
							while(texte.subSequence(k, k+14) != "<\\description>"){
								k++;
							}
							contenu = texte.substring(j, k);		
						}
						if (texte.subSequence(j, j+9) == "<pubDate>") {
							k = j;
							while(texte.subSequence(k, k+11) != "<\\pubDate>"){
								k++;
							}
							date = texte.substring(j, k);
						}
						j++;
					}
					rss.add(new Depeche(String.valueOf(depCourante), date, categorie, contenu));
					i = j;
				}
				
				i++;

			}

		

		} catch (IOException e) {
			e.printStackTrace();
		}



		return rss;
	}
}
