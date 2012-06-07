import java.io.File;
import java.util.ArrayList;

import javax.faces.context.FacesContext;

import metier.media.Media;
import metier.utilisateur.Utilisateur;
import dao.media.DaoMedia;


/**
 * Bean de la page principale Home
 * @author Benjamin
 *
 */
public class BeanHome {

	// DAOs
	private DaoMedia daoMedia;
	
	// List
	private ArrayList<Media> nouvellesVideos;
	private ArrayList<Media> nouveauxSons;
	private ArrayList<Media> topNotationVideos;
	private ArrayList<Media> topNotationSons;
	private ArrayList<Media> recommendationVideos;
	private ArrayList<Media> recommendationSons;	
	
	// Utilisateur connecte actuellement
	private Utilisateur utilisateurConnecte;	
	
	//Bean
	BeanConnexion beanConnexion;   	
	
	public BeanHome() {
		// DAOs
		daoMedia = new DaoMedia();
		
		// Chargement des listes
		
		nouvellesVideos = new ArrayList(daoMedia.nouvellesVideos());
		nouveauxSons = new ArrayList(daoMedia.nouveauxSons());
		topNotationVideos = new ArrayList(daoMedia.topNotationVideo());
		topNotationSons = new ArrayList(daoMedia.topNotationAudio());
		recommendationVideos = new ArrayList();
		recommendationSons = new ArrayList();
		
		// Chargement de l'utilisateur connecte
		beanConnexion = (BeanConnexion) FacesContext.getCurrentInstance().getCurrentInstance().getExternalContext().getSessionMap().get("beanConnexion");
		
		if (beanConnexion != null) {
			// ON charge l'utilisateur connecte
			utilisateurConnecte = beanConnexion.getUser();		
			if (utilisateurConnecte != null) {
				// Si l'utilisateur n'est pas null, on charge des recommendations
				recommendationVideos = new ArrayList(daoMedia.recommendationVideos(utilisateurConnecte));
				recommendationSons = new ArrayList(daoMedia.recommendationVideos(utilisateurConnecte));				
			}
		}			
	}	
	
	// GETTER SETTER
	
	public ArrayList<Media> getNouvellesVideos() {
		return nouvellesVideos;
	}

	public void setNouvellesVideos(ArrayList<Media> nouvellesVideos) {
		this.nouvellesVideos = nouvellesVideos;
	}

	public ArrayList<Media> getNouveauxSons() {
		return nouveauxSons;
	}

	public void setNouveauxSons(ArrayList<Media> nouveauxSons) {
		this.nouveauxSons = nouveauxSons;
	}

	public ArrayList<Media> getTopNotationVideos() {
		return topNotationVideos;
	}

	public void setTopNotationVideos(ArrayList<Media> topVideos) {
		this.topNotationVideos = topVideos;
	}

	public ArrayList<Media> getTopNotationSons() {
		return topNotationSons;
	}

	public void setTopNotationSons(ArrayList<Media> topSons) {
		this.topNotationSons = topSons;
	}

	public Utilisateur getUtilisateurConnecte() {
		utilisateurConnecte = beanConnexion.getUser();
		return utilisateurConnecte;
	}

	public void setUtilisateurConnecte(Utilisateur utilisateurConnecte) {
		this.utilisateurConnecte = utilisateurConnecte;
	}

	public ArrayList<Media> getRecommendationVideos() {
		return recommendationVideos;
	}

	public void setRecommendationVideos(ArrayList<Media> recommendationVideos) {
		this.recommendationVideos = recommendationVideos;
	}

	public ArrayList<Media> getRecommendationSons() {
		return recommendationSons;
	}

	public void setRecommendationSons(ArrayList<Media> recommendationSons) {
		this.recommendationSons = recommendationSons;
	}	
	
	
}
