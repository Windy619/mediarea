package metier.utilisateur;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

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

	/**
	 * Constructeur vide
	 */
	public Message_Prive(){
		super();
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




}