package metier.media;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	
	//SimpleDateFormat dateFormat;
	private Date dateVues;
	
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
		
		//dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		//System.out.println("Date : " + dateFormat.format(new Date()));
		//try {
		//	this.dateVues = dateFormat.parse(dateFormat.format(new Date()));
		this.dateVues = new Date();
		//} catch (ParseException e) {
		//	e.printStackTrace();
		//}
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

	public Date getDateVues() {
		return dateVues;
	}

	public void setDateVues(Date dateVues) {
		this.dateVues = dateVues;
	}
}