package metier.utilisateur;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * @author Florence
 * @version 1.0
 * @created 26-mars-2012 22:26:59
 */
@MappedSuperclass
/**
 * Class Mere Message
 * Mere de tous les Messages
 * @author Benjamin
 *
 */
public abstract class Message {

	@Id
	@GeneratedValue
	private long idMessage;
	
	private String contenuMessage;
	
	private Date dateEnvoi;
	
	private Date dateLecture;
	
	private Date dateSuppressionMessage;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Utilisateur emetteur;

	/**
	 * Constructeur vide
	 */
	public Message(){}
	
	/**
	 * Constructeur par défaut
	 * @param contenu
	 * @param emetteur
	 */
	public Message(String contenu, Utilisateur emetteur){
		this.contenuMessage = contenu;
		this.emetteur = emetteur;
	}

	/**
	 * Suppression de l'instance
	 */
	public void finalize() throws Throwable {

	}

	// GETTER SETTER
	
	public String getContenuMessage() {
		return contenuMessage;
	}

	public void setContenuMessage(String contenuMessage) {
		this.contenuMessage = contenuMessage;
	}

	public Date getDateEnvoi() {
		return dateEnvoi;
	}

	public void setDateEnvoi(Date dateEnvoi) {
		this.dateEnvoi = dateEnvoi;
	}

	public Date getDateLecture() {
		return dateLecture;
	}

	public void setDateLecture(Date dateLecture) {
		this.dateLecture = dateLecture;
	}

	public Date getDateSuppressionMessage() {
		return dateSuppressionMessage;
	}

	public void setDateSuppressionMessage(Date dateSuppressionMessage) {
		this.dateSuppressionMessage = dateSuppressionMessage;
	}

	public long getIdMessage() {
		return idMessage;
	}

	public void setIdMessage(long idMessage) {
		this.idMessage = idMessage;
	}

	public Utilisateur getEmetteur() {
		return emetteur;
	}

	public void setEmetteur(Utilisateur emetteur) {
		this.emetteur = emetteur;
	}
	
	


}