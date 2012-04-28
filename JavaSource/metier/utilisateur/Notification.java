package metier.utilisateur;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Florence
 * @version 1.0
 * @created 26-mars-2012 22:26:59
 */
@Entity
/**
 * Class Notification
 * Notification pour un Utilisateur
 * @author Benjamin
 *
 */
public class Notification {

	@Id
	@GeneratedValue
	private long idNotification;
	
	private String contenuNotification;
	
	private Date dateEnvoiNotification;
	
	private Date dateLectureNotification;
	
	private Date dateSuppressionNotification;

	/**
	 * Constructeur vide
	 */
	public Notification(){}
	
	/**
	 * Constructeur par défaut
	 * @param contenuNotification
	 */
	public Notification(String contenuNotification){
		this.contenuNotification = contenuNotification;
		this.dateEnvoiNotification = new Date();
		
	}	

	/**
	 * Suppression de l'instance
	 */
	public void finalize() throws Throwable {

	}

	// GETTER SETTER
	
	public String getContenuNotification() {
		return contenuNotification;
	}

	public void setContenuNotification(String contenuNotification) {
		this.contenuNotification = contenuNotification;
	}

	public Date getDateEnvoiNotification() {
		return dateEnvoiNotification;
	}

	public void setDateEnvoiNotification(Date dateEnvoiNotification) {
		this.dateEnvoiNotification = dateEnvoiNotification;
	}

	public Date getDateLectureNotification() {
		return dateLectureNotification;
	}

	public void setDateLectureNotification(Date dateLectureNotification) {
		this.dateLectureNotification = dateLectureNotification;
	}

	public Date getDateSuppressionNotification() {
		return dateSuppressionNotification;
	}

	public void setDateSuppressionNotification(Date dateSuppressionNotification) {
		this.dateSuppressionNotification = dateSuppressionNotification;
	}

	public long getIdNotification() {
		return idNotification;
	}

	public void setIdNotification(long idNotification) {
		this.idNotification = idNotification;
	}

	
}