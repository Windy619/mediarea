package metier.utilisateur;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

public class Signalement_MessagePrive {

	@Id
	@GeneratedValue
	private long idSignalementMessagePrive;

	private Date dateSignalementMessagePrive;
		
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Message_Prive signale;

	/**
	 * Constructeur vide
	 */
	public Signalement_MessagePrive(){}
	
	/**
	 * Constructeur par défaut
	 * @param raisonSignalement
	 * @param signale
	 */
	public Signalement_MessagePrive(Message_Prive signale) {
		this.dateSignalementMessagePrive = new Date();
		this.signale = signale;
	}
	
	/**
	 * Suppression d'une instance
	 */
	public void finalize() throws Throwable {

	}
	
	// GETTER SETTER

	public long getIdSignalementMessagePrive() {
		return idSignalementMessagePrive;
	}

	public void setIdSignalementMessagePrive(long idSignalementMessagePrive) {
		this.idSignalementMessagePrive = idSignalementMessagePrive;
	}

	public Date getDateSignalementMessagePrive() {
		return dateSignalementMessagePrive;
	}

	public void setDateSignalementMessagePrive(Date dateSignalementMessagePrive) {
		this.dateSignalementMessagePrive = dateSignalementMessagePrive;
	}


	public Message_Prive getSignale() {
		return signale;
	}

	public void setSignale(Message_Prive signale) {
		this.signale = signale;
	}
	
	
}
