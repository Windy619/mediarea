package metier.media;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import metier.utilisateur.Utilisateur;

/**
 * @author Florence
 * @version 1.0
 * @created 26-mars-2012 22:27:00
 */
@Entity
/**
 * Class Signalement_Commentaire
 * Représente le signalement d'un commentaire par un utilisateur
 * @author Benjamin
 *
 */
public class Signalement_Commentaire {

	@Id
	@GeneratedValue
	private long idSignalementCommentaire;
	
	private Date dateSignalementCommentaire;
	
	private String raisonSignalementCommentaire;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Utilisateur auteurSignalementCommentaire;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Commentaire commentaire;

	/**
	 * Constructeur vide
	 */
	public Signalement_Commentaire(){
		dateSignalementCommentaire = new Date();
	}
	
	/**
	 * Constructeur par défaut
	 * @param raison
	 * @param commentaire
	 */
	public Signalement_Commentaire(String raison, Commentaire commentaire, Utilisateur auteur) {
		this.dateSignalementCommentaire = new Date();
		this.raisonSignalementCommentaire = raison;
		this.commentaire = commentaire;
		this.auteurSignalementCommentaire = auteur;
	}	

	/**
	 * Suppression de l'instance
	 */
	public void finalize() throws Throwable {

	}

	// GETTER SETTER
	
	public Date getDateSignalementCommentaire() {
		return dateSignalementCommentaire;
	}

	public void setDateSignalementCommentaire(Date dateSignalementCommentaire) {
		this.dateSignalementCommentaire = dateSignalementCommentaire;
	}

	public String getRaisonSignalementCommentaire() {
		return raisonSignalementCommentaire;
	}

	public void setRaisonSignalementCommentaire(String raisonSignalementCommentaire) {
		this.raisonSignalementCommentaire = raisonSignalementCommentaire;
	}

	public Commentaire getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(Commentaire commentaire) {
		this.commentaire = commentaire;
	}

	public long getIdSignalementCommentaire() {
		return idSignalementCommentaire;
	}

	public void setIdSignalementCommentaire(long idSignalementCommentaire) {
		this.idSignalementCommentaire = idSignalementCommentaire;
	}

	public String toString(){
		return ("S"+String.valueOf(idSignalementCommentaire));
	}

	public Utilisateur getAuteurSignalementCommentaire() {
		return auteurSignalementCommentaire;
	}

	public void setAuteurSignalementCommentaire(
			Utilisateur auteurSignalementCommentaire) {
		this.auteurSignalementCommentaire = auteurSignalementCommentaire;
	}	
	
	
}