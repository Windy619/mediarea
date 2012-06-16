package metier.utilisateur;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * @author Florence
 * @version 1.0
 * @created 26-mars-2012 22:26:59
 */
@Entity
/**
 * Class Message_Prive
 * Message_Prive entre utilisateur
 * @author Benjamin
 *
 */
public class Message_Prive extends Message {

	private String objet;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Utilisateur destinataire;
	
	private Boolean isMessageMere;
	
	@OneToMany(cascade = {CascadeType.ALL})
	private Set<Message_Prive> reponses;
		
	private Date dateSuppressionMessageDestinataire;	


	/**
	 * Constructeur vide
	 */
	public Message_Prive(){
		super();
		isMessageMere = true;
		reponses = new HashSet<Message_Prive>();
	}

	/**
	 * Constructeur par défaut
	 * @param contenu
	 * @param emetteur
	 * @param destinataire
	 * @param objet
	 */
	public Message_Prive(String contenu, Utilisateur emetteur, Utilisateur destinataire, String objet) {
		super(contenu,emetteur);
		this.destinataire = destinataire;
		this.objet = objet;
		this.isMessageMere = true;
		reponses = new HashSet<Message_Prive>();
	}

	/**
	 * Suppression de l'instance
	 */
	public void finalize() throws Throwable {
		super.finalize();
	}

	// GETTER SETTER
	
	public String getObjet() {
		return objet;
	}

	public void setObjet(String objet) {
		this.objet = objet;
	}

	public Utilisateur getDestinataire() {
		return destinataire;
	}

	public void setDestinataire(Utilisateur destinataire) {
		this.destinataire = destinataire;
	}

	public Set<Message_Prive> getReponses() {
		return reponses;
	}

	public void setReponses(Set<Message_Prive> reponses) {
		this.reponses = reponses;
	}

	public Date getDateSuppressionMessageDestinataire() {
		return dateSuppressionMessageDestinataire;
	}

	public void setDateSuppressionMessageDestinataire(
			Date dateSuppressionMessageDestinataire) {
		this.dateSuppressionMessageDestinataire = dateSuppressionMessageDestinataire;
	}

	public Boolean getIsMessageMere() {
		return isMessageMere;
	}

	public void setIsMessageMere(Boolean isMessageMere) {
		this.isMessageMere = isMessageMere;
	}






}