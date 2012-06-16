package metier.media;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author Florence
 * @version 1.0
 * @created 26-mars-2012 22:26:59
 */
@Entity
/**
 * Class Note
 * Note d'un Utilisateur pour un Media
 * @author Benjamin
 *
 */
public class Note {

	@Id
	@GeneratedValue
	private long idNote;
	
	private int note;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Media media;

	/**
	 * Constructeur vide
	 */
	public Note(){}
	
	/**
	 * Constructeur par défaut
	 * @param note
	 * @param media
	 */
	public Note(int note, Media media) {
		this.note = note;
		this.media = media;
	}

	/**
	 * Suppression de l'instance
	 */
	public void finalize() throws Throwable {

	}

	// GETTER SETTER
	
	public long getIdNote() {
		return idNote;
	}

	public void setIdNote(long idNote) {
		this.idNote = idNote;
	}

	public int getNote() {
		return note;
	}

	public void setNote(int note) {
		this.note = note;
	}

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}



}