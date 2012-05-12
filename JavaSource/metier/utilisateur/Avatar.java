package metier.utilisateur;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author mediarea
 * @version 1.0
 * @created 26-mars-2012 22:26:59
 */
@Entity
/**
 * Class Avatar
 * Represente l'avatar d'un Utilisateur
 * @author Benjamin
 *
 */
public class Avatar {

	@Id
	@GeneratedValue
	private long idAvatar;
	
	private String cheminAvatar;

	private String nomAvatar;

	/**
	 * Constructeur vide
	 */
	public Avatar(){}
	
	/**
	 * Constructeur par défaut
	 * @param nomAvatar
	 */
	public Avatar(String nomAvatar){
		this.nomAvatar = nomAvatar;
	}	
	
	public Avatar(String cheminAvatar, String nomAvatar){
		this.cheminAvatar = cheminAvatar;
		this.nomAvatar = nomAvatar;
	}

	/**
	 * Suppression de l'instance
	 */
	public void finalize() throws Throwable {

	}

	// GETTER SETTER
	
	public String getCheminAvatar() {
		return cheminAvatar;
	}

	public void setCheminAvatar(String cheminAvatar) {
		this.cheminAvatar = cheminAvatar;
	}

	public long getIdAvatar() {
		return idAvatar;
	}

	public void setIdAvatar(long idAvatar) {
		this.idAvatar = idAvatar;
	}

	public String getNomAvatar() {
		return nomAvatar;
	}

	public void setNomAvatar(String nomAvatar) {
		this.nomAvatar = nomAvatar;
	}



}