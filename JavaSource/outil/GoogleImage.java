package outil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class GoogleImage {
	
	// Chemin vers l'API de google pour la recherche d'image
	private final static String API = "http://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=";
	
	/** 
	 * Lecture d'une page web
	 * @param page
	 * @return
	 */
	public static String lirePage(String page) {

		StringBuffer buffer = new StringBuffer();;
		
		try {

			// Accès à la page
			URL url = new URL(page);
			InputStreamReader ipsr = new InputStreamReader(url.openStream());
			BufferedReader br = new BufferedReader(ipsr);
			String line = null;
			
			// Création de la chaine
			while ((line = br.readLine()) != null) {
				buffer.append(line).append('\n');
			}
			
			br.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return buffer.toString();
	}
	
	public static ArrayList<String> rechercheGoogle(String recherche) {
		
		// On supprime les espaces
		recherche = recherche.replace(" ", "_");
		
		// Récupération de la page HTML
		String page = lirePage(API + recherche);
		
		// Split de la page
		String[] tableau = page.split("\"");
		System.out.println("Nombre d'éléments : " + tableau.length);
		
		// On rempli un tableau de resultat avec les string qui nous interessent (celles qui contiennent '.jpg')
		ArrayList<String> lstRes = new ArrayList<String>();
		for (String string : tableau) {
			if (string.contains(".jpg")) {
				lstRes.add(string);
			}
		}
		
		// Retour du resultat
		return lstRes;
	}
}
