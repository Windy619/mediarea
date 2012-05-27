package metier.media;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author Florence
 * @version 1.0
 * @created 26-mars-2012 22:27:00
 */
@Entity
/**
 * Class Regarder
 * Regarder représente une visualisation d'un Media
 * @author Benjamin
 *
 */
public class Regarder {

	@Id
	@GeneratedValue
	private long idRegarder;
	
	private long nbVues;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Media media;

	/**
	 * Constructeur vide
	 */
	public Regarder(){}
	
	/**
	 * Constructeur par défaut
	 * @param media
	 */
	public Regarder(Media media){
		this.media = media;
		this.nbVues = 0;
	}	

	/**
	 * Suppression de l'instance
	 */
	public void finalize() throws Throwable {

	}
	
	// GETTER SETTER

	public long getNbVues() {
		return nbVues;
	}

	public void setNbVues(long nbVues) {
		this.nbVues = nbVues;
	}

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

	public long getIdRegarder() {
		return idRegarder;
	}

	public void setIdRegarder(long idRegarder) {
		this.idRegarder = idRegarder;
	}

	
	

}