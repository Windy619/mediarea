package metier.utilisateur;

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
 * Class Signalement_Utilisateur
 * Signalement d'un Utilisateur par un autre Utilisateur
 * @author Benjamin
 *
 */
public class Signalement_Utilisateur {

	@Id
	@GeneratedValue
	private long idSignalementUtilisateur;

	private Date dateSignalementUtilisateur;
	
	private String raisonSignalementUtilisateur;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Utilisateur signale;

	/**
	 * Constructeur vide
	 */
	public Signalement_Utilisateur(){}
	
	/**
	 * Constructeur par défaut
	 * @param raisonSignalement
	 * @param signale
	 */
	public Signalement_Utilisateur(String raisonSignalement, Utilisateur signale) {
		this.dateSignalementUtilisateur = new Date();
		this.raisonSignalementUtilisateur = raisonSignalement;
		this.signale = signale;
	}
	
	/**
	 * Suppression d'une instance
	 */
	public void finalize() throws Throwable {

	}
	
	// GETTER SETTER

	public Date getDateSignalementUtilisateur() {
		return dateSignalementUtilisateur;
	}

	public void setDateSignalementUtilisateur(Date dateSignalementUtilisateur) {
		this.dateSignalementUtilisateur = dateSignalementUtilisateur;
	}

	public String getRaisonSignalementUtilisateur() {
		return raisonSignalementUtilisateur;
	}

	public void setRaisonSignalementUtilisateur(String raisonSignalementUtilisateur) {
		this.raisonSignalementUtilisateur = raisonSignalementUtilisateur;
	}

	public Utilisateur getSignale() {
		return signale;
	}

	public void setSignale(Utilisateur signale) {
		this.signale = signale;
	}
	
	public long getIdSignalementUtilisateur() {
		return idSignalementUtilisateur;
	}

	public void setIdSignalementUtilisateur(long idSignalementUtilisateur) {
		this.idSignalementUtilisateur = idSignalementUtilisateur;
	}	

	
}