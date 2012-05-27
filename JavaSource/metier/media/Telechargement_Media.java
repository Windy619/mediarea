package metier.media;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Telechargement_Media {

	@Id
	@GeneratedValue
	private long idTelechargementMedia;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Media media;

	private int nbTelechargement;
	
	/**
	 * Constructeur par défaut
	 */
	public Telechargement_Media() {
		nbTelechargement = 1;
	}
	
	// GETTER SETTER
	

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

	public int getNbTelechargement() {
		return nbTelechargement;
	}

	public void setNbTelechargement(int nbTelechargement) {
		this.nbTelechargement = nbTelechargement;
	}

	public long getIdTelechargementMedia() {
		return idTelechargementMedia;
	}

	public void setIdTelechargementMedia(long idTelechargementMedia) {
		this.idTelechargementMedia = idTelechargementMedia;
	}	
	
	
}
