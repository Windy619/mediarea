import java.util.ArrayList;
import java.util.List;

import metier.media.*;
import dao.media.*;

/**
 * @author Florence
 *
 */

public class BeanMedia {
	private DaoMedia daoMedia;
	
	private Media mediaVisualise;
	
	//constructeur
	public BeanMedia() {
		
		daoMedia = new DaoMedia();
	}
	
	//getter et setter
	

	//méthodes
	public String afficherTitre() { //Media idMedia
		mediaVisualise = daoMedia.getUn(1);
		
		return mediaVisualise.getTitreMedia();
	}
	
	
	
	
	
	
	
}
