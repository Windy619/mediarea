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
 * Class Message_Mural
 * Message sur le mur d'un utilisateur
 * @author Benjamin
 *
 */
public class Message_Mural extends Message {

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Utilisateur destinataire;
	
	/**
	 * Constructeur vide
	 */
	public Message_Mural(){
		super();
	}
	
	/**
	 * Constrcuteur par défaut
	 * @param contenu
	 * @param emetteur
	 * @param destinataire
	 */
	public Message_Mural(String contenu, Utilisateur emetteur, Utilisateur destinataire){
		super(contenu,emetteur);
		this.destinataire = destinataire;
	}	

	/**
	 * Suppression de l'instance
	 */
	public void finalize() throws Throwable {
		super.finalize();
	}
	
	
	// GETTER SETTER
	
	public Utilisateur getDestinataire() {
		return destinataire;
	}
	public void setDestinataire(Utilisateur destinataire) {
		this.destinataire = destinataire;
	}

}