package metier.utilisateur;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * @author mediarea
 * @version 1.0
 * @created 26-mars-2012 22:26:59
 */

@Entity
/**
 * Classe Amitie
 * Permet de symboliser deux Utilisateur amis
 * @author Benjamin
 *
 */
public class Amitie {

	@Id
	@GeneratedValue
	private long idAmitie;
	
	private Date dateAmitie;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Utilisateur ami;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Utilisateur utilisateur;

	/**
	 * Constructeur vide
	 */
	public Amitie(){}
	
	/**
	 * Constructeur par défaut
	 * @param ami
	 */
	public Amitie(Utilisateur u,Utilisateur ami){
		this.utilisateur = u;
		this.ami = ami;
		dateAmitie = new Date();
	}	

	/**
	 * Suppression de l'instance
	 */
	public void finalize() throws Throwable {

	}

	
	// GETTER SETTER
	public Date getDateAmitie() {
		return dateAmitie;
	}

	public void setDateAmitie(Date dateAmitie) {
		this.dateAmitie = dateAmitie;
	}

	public Utilisateur getAmi() {
		return ami;
	}

	public void setAmi(Utilisateur ami) {
		this.ami = ami;
	}

	public long getIdAmitie() {
		return idAmitie;
	}

	public void setIdAmitie(long idAmitie) {
		this.idAmitie = idAmitie;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

}