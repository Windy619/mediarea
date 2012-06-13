import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import metier.media.*;
import dao.media.*;

import metier.utilisateur.*;
import dao.utilisateur.*;

/**
 * @author Florence
 *
 */
public class BeanMediaSuggestions {
	// DAO
	private static DaoMedia daoMedia;
	
	// Propriétés
	private List<Media> listeMediasSuggeres;
 	private Set<Tag> tagMedia;
 	private List<Media> listeTousMedia;
 	private Iterator<Tag> iteratorMedia;
 	private HashMap<Media, Integer> mapOccurrenceTags;
 	private Tag tagMediaCourant;
 	private Set<Tag> setTagMediaCourant;
	
	/**
	 * Constructeur du Bean
	 */
	public BeanMediaSuggestions() {	
		daoMedia = new DaoMedia();
		
		listeMediasSuggeres = new ArrayList<Media>();
		
		algorithmeSuggestions();
	}
	

	/** 
	 * Algorithme des suggestions de média
	 * @return
	 */
	public void algorithmeSuggestions() {
		//Récupération de la liste de tags associés au média
		tagMedia = daoMedia.getUn(2).getTags();
		//System.out.println("tagMedia : " + tagMedia);
		
		//Récupération de tous les médias
		listeTousMedia = daoMedia.getTous();
		
		//Création d'une HashMap contenant le nb d'occurrences de tags correspondants dans les médias
		mapOccurrenceTags = new HashMap<Media, Integer>();
		
		iteratorMedia = tagMedia.iterator();
		//Parcours des tags du média visualisé
		while(iteratorMedia.hasNext()) { 
			tagMediaCourant = iteratorMedia.next();
			//System.out.println("Set tagMedia : " + tagMediaCourant);
			
			//Parcours de tous les médias
			for(Media elMedia : listeTousMedia) {
				if(! elMedia.equals(null)) { //if(! elMedia.equals(daoMedia.getUn(2))) { TODO //tout sauf le média actuellement visualisé
					setTagMediaCourant = daoMedia.getUn(elMedia.getIdMedia()).getTags();
					
					//Parcours des tags associés au média
					for(Tag tagMediaCourantAutreMedia : setTagMediaCourant) {
						if(tagMediaCourant.toString().equals(tagMediaCourantAutreMedia.toString())) {
							if(mapOccurrenceTags.containsKey(elMedia)) {
								//Incrémentation de l'occurrence de tags du média courant
								mapOccurrenceTags.put(elMedia, mapOccurrenceTags.get(elMedia) + 1); //tag correspond supplémentaire
							}
							else {
								//Association de 1 au média courant pour la HashMap (la clé n'existe pas encore)
								mapOccurrenceTags.put(elMedia, 1);
							}
						}
					}
				}
			}
		}
		
		/*//Affichage de la HashMap
		Set cles = map.keySet();
		Iterator it = cles.iterator();
		while (it.hasNext()){
		   Object cle = it.next();
		   Object valeur = map.get(cle);
		   System.out.println(cle + " => " + valeur);
		}*/
		
		//Création d'une liste de clés
		listeMediasSuggeres = new ArrayList<Media>(mapOccurrenceTags.keySet());
		//System.out.println("size listeMediasSuggeres : " + listeMediasSuggeres.size());
		
		//Limitation à 20 médias suggérés
		if(listeMediasSuggeres.size() < 20) {
			//Extraction des premiers éléments de la liste des médias suggérés
			listeMediasSuggeres = listeMediasSuggeres.subList(0, listeMediasSuggeres.size());
		}
		else {
			//Extraiter des 20 premiers médias suggérés
			listeMediasSuggeres = listeMediasSuggeres.subList(0, 20);
		}
		//suggestion en tenant compte du titre et catégories XXX
	}
	
	

	// GETTER / SETTER
	
	public List<Media> getListeMediasSuggeres() {
		return listeMediasSuggeres;
	}
	
	public void setListeMediasSuggeres(List<Media> listeMediasSuggeres) {
		this.listeMediasSuggeres = listeMediasSuggeres;
	}
}
