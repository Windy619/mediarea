package metier.media;

import java.util.Date;

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
 * Class Signalement_Media
 * Classe représentant le signalement d'un Media par un utilisateur
 * @author Benjamin
 *
 */
public class Signalement_Media {

	@Id
	@GeneratedValue
	private long idSignalementMedia;
	
	private Date dateSignalementMedia;
	
	private String raisonSignalementMedia;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Media media;

	/**
	 * Constructeur vide
	 */
	public Signalement_Media(){
		dateSignalementMedia = new Date();
	}
	
	/**
	 * Constructeur par défaut
	 * @param raison
	 * @param media
	 */
	public Signalement_Media(String raison, Media media) {
		this.dateSignalementMedia = new Date();
		this.raisonSignalementMedia = raison;
		this.media = media;
	}

	public void finalize() throws Throwable {

	}

	public Date getDateSignalementMedia() {
		return dateSignalementMedia;
	}

	public void setDateSignalementMedia(Date dateSignalementMedia) {
		this.dateSignalementMedia = dateSignalementMedia;
	}

	public String getRaisonSignalementMedia() {
		return raisonSignalementMedia;
	}

	public void setRaisonSignalementMedia(String raisonSignalementMedia) {
		this.raisonSignalementMedia = raisonSignalementMedia;
	}

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

	public long getIdSignalement() {
		return idSignalementMedia;
	}

	public void setIdSignalement(long idSignalement) {
		this.idSignalementMedia = idSignalement;
	}

	
	

}