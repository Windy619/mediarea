import java.util.ArrayList;
import java.util.List;

import metier.media.*;
import dao.media.*;

/**
 * @author Florence
 *
 */

public class BeanMedia {
	String titre = null; //Initialization
	
	private DaoMedia daoMedia;
	
	private Media mediaVisualise;
	
	//constructeur
	public BeanMedia() {
		
		daoMedia = new DaoMedia();
		mediaVisualise = daoMedia.getUn(2);
	}
	
	//getter et setter
	

	//méthodes
	public String afficherTitre() {
		
		//titre = mediaVisualise.getTitreMedia();
		return mediaVisualise.getTitreMedia();
		//return "afficherTitre";
	}
	
	public String nbCommentairesMedia() {
		
		//utiliser la fonction commentairesMedia(int idMedia) dans DaoMedia
		
		return "nbCommentairesMedia";
	}
}
