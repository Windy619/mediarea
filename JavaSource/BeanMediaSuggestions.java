import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.context.FacesContext;

import metier.media.*;
import dao.media.*;

/**
 * @author Florence
 *
 */
public class BeanMediaSuggestions {
	// DAO
	private static DaoMedia daoMedia;
	
	// Propri�t�s
	private List<Media> listeMediasSuggeres;
 	private List<Media>listeMediasSuggeresTotal;
 	private Set<Tag> tagMedia;
 	private List<Media> listeTousMedia;
 	private Iterator<Tag> iteratorMedia;
 	private HashMap<Media, Integer> mapOccurrenceTags;
 	private Tag tagMediaCourant;
 	private Set<Tag> setTagMediaCourant;
 	
 	// Bean
 	private BeanMedia beanMedia;
 	
	
	/**
	 * Constructeur du Bean
	 */
	public BeanMediaSuggestions() {
		// Chargement du m�dia visualis�
		beanMedia = (BeanMedia) FacesContext.getCurrentInstance().getCurrentInstance().getExternalContext().getSessionMap().get("beanMedia");
		
		// Instantiation des Dao
		daoMedia = new DaoMedia();
		
		// Cr�ation de la liste qui contiendra les m�dias sugg�r�s
		listeMediasSuggeres = new ArrayList<Media>();
		
		// Appel � l'algorithme g�n�rant les m�dias sugg�r�s
		algorithmeSuggestions();
	}
	

	/** 
	 * Algorithme des suggestions de m�dia
	 * @return
	 */
	public void algorithmeSuggestions() {
		// R�cup�ration de la liste de tags associ�s au m�dia
		tagMedia = beanMedia.getMediaVisualise().getTags();
		//System.out.println("tagMedia : " + tagMedia);
		
		// R�cup�ration de tous les m�dias
		listeTousMedia = daoMedia.getTous();
		
		// Cr�ation d'une HashMap contenant le nb d'occurrences de tags correspondants dans les m�dias
		mapOccurrenceTags = new HashMap<Media, Integer>();
		
		iteratorMedia = tagMedia.iterator();
		// Parcours des tags du m�dia visualis�
		while(iteratorMedia.hasNext()) { 
			tagMediaCourant = iteratorMedia.next();
			//System.out.println("Set tagMedia : " + tagMediaCourant);
			
			// Parcours de tous les m�dias
			for(Media elMedia : listeTousMedia) {
				if(! elMedia.equals(null)) { //if(! elMedia.equals(beanMedia.getMediaVisualise())) { TODO //tout sauf le m�dia actuellement visualis�
					setTagMediaCourant = daoMedia.getUn(elMedia.getIdMedia()).getTags();
					
					//Parcours des tags associ�s au m�dia
					for(Tag tagMediaCourantAutreMedia : setTagMediaCourant) {
						if(tagMediaCourant.toString().equals(tagMediaCourantAutreMedia.toString())) {
							if(mapOccurrenceTags.containsKey(elMedia)) {
								// Incr�mentation de l'occurrence de tags du m�dia courant
								mapOccurrenceTags.put(elMedia, mapOccurrenceTags.get(elMedia) + 1); //tag correspond suppl�mentaire
							}
							else {
								// Association de 1 au m�dia courant pour la HashMap (la cl� n'existe pas encore)
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
		
		// Cr�ation d'une liste de cl�s
		listeMediasSuggeres = new ArrayList<Media>(mapOccurrenceTags.keySet());
		//System.out.println("size listeMediasSuggeres : " + listeMediasSuggeres.size());
		
		listeMediasSuggeresTotal = listeMediasSuggeres;
		
		// Limitation � 20 m�dias sugg�r�s
		if(listeMediasSuggeres.size() < 20) {
			// Extraction des premiers �l�ments de la liste des m�dias sugg�r�s
			listeMediasSuggeres = listeMediasSuggeres.subList(0, listeMediasSuggeres.size());
		}
		else {
			// Extraction des 20 premiers m�dias sugg�r�s
			listeMediasSuggeres = listeMediasSuggeres.subList(0, 20);
		}
		//suggestion en tenant compte du titre et cat�gories XXX
	}
	
	

	// GETTER / SETTER
	
	public List<Media> getListeMediasSuggeres() {
		return listeMediasSuggeres;
	}
	
	public void setListeMediasSuggeres(List<Media> listeMediasSuggeres) {
		this.listeMediasSuggeres = listeMediasSuggeres;
	}
	
	public List<Media> getListeMediasSuggeresTotal() {
		return listeMediasSuggeresTotal;
	}
	
	public void setListeMediasSuggeresTotal(List<Media> listeMediasSuggeresTotal) {
		this.listeMediasSuggeresTotal = listeMediasSuggeresTotal;
	}	
}
