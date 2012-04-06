package metier.media;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import metier.utilisateur.Utilisateur;

/**
 * @author Florence
 * @version 1.0
 * @created 26-mars-2012 22:26:59
 */

@Entity
/**
 * Class Commentaire
 * CLasse représentant un commentaire d'un Media
 * @author Benjamin
 *
 */
public class Commentaire {
	
	@Id
	@GeneratedValue
	private long idCommentaire;
	
	private String contenuCommentaire;
	
	private Date dateCommentaire;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Utilisateur auteur;

	private long nbVotes;
	
	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	private Set<Commentaire> commentairesFils;

	/**
	 * Constructeur vide
	 */
	public Commentaire(){
		commentairesFils = new HashSet<Commentaire>();
	}
	
	/**
	 * Constructeur par défaut
	 * @param contenu
	 * @param auteur
	 */
	public Commentaire(String contenu, Utilisateur auteur){
		this.auteur = auteur;
		this.contenuCommentaire = contenu;
		this.dateCommentaire = new Date();
		this.nbVotes = 0;
		commentairesFils = new HashSet<Commentaire>();
	}	

	/**
	 * Suppression de l'instance
	 */
	public void finalize() throws Throwable {

	}

	// GETTER SETTER
	
	public String getContenuCommentaire() {
		return contenuCommentaire;
	}

	public void setContenuCommentaire(String contenuCommentaire) {
		this.contenuCommentaire = contenuCommentaire;
	}

	public Date getDateCommentaire() {
		return dateCommentaire;
	}

	public void setDateCommentaire(Date dateCommentaire) {
		this.dateCommentaire = dateCommentaire;
	}

	public long getIdCommentaire() {
		return idCommentaire;
	}

	public void setIdCommentaire(long idCommentaire) {
		this.idCommentaire = idCommentaire;
	}

	public long getNbVotes() {
		return nbVotes;
	}

	public void setNbVotes(long nbVotes) {
		this.nbVotes = nbVotes;
	}

	public Set<Commentaire> getCommentairesFils() {
		return commentairesFils;
	}

	public void setCommentairesFils(Set<Commentaire> commentairesFils) {
		this.commentairesFils = commentairesFils;
	}

	public Utilisateur getAuteur() {
		return auteur;
	}

	public void setAuteur(Utilisateur auteur) {
		this.auteur = auteur;
	}
	
	



}