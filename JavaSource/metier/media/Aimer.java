package metier.media;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author mediarea
 * @version 1.0
 * @created 26-mars-2012 22:26:59
 */

@Entity
/**
 * Class Aimer 
 * Permet à un utilisateur d'aimer un Media
 * @author MediArea
 */
public class Aimer {

	@Id
	@GeneratedValue
	private long idAimer;
	
	private boolean aAime;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Media media;

	/** 
	 * Constructeur vide
	 */
	public Aimer(){}
	
	/**
	 * Constructeur par défaut
	 * @param aAime
	 * @param media
	 */
	public Aimer(boolean aAime, Media media){
		this.aAime = aAime;
		this.media = media;
	}	

	/** 
	 * Suppression de l'instance
	 */
	public void finalize() throws Throwable {
		
	}

	// GETTER SETTER
	
	public boolean isaAime() {
		return aAime;
	}

	public void setaAime(boolean aAime) {
		this.aAime = aAime;
	}

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

	public long getIdAimer() {
		return idAimer;
	}

	public void setIdAimer(long idAimer) {
		this.idAimer = idAimer;
	}	
	

}