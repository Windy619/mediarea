package metier.media;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Florence
 * @version 1.0
 * @created 26-mars-2012 22:26:59
 */
@Entity
/**
 * CLass Photo_Couverture
 * Photo de couverture d'un Media
 * @author Benjamin
 *
 */
public class Photo_Couverture {

	@Id
	@GeneratedValue
	private long idPhotoCouverture;
	
	private String cheminPhotoCouverture;

	private String nomPhotoCouverture;

	/**
	 * Constructeur vide
	 */
	public Photo_Couverture(){}
	
	/**
	 * Constructeur par défaut
	 * @param chemin
	 * @param nom
	 */
	public Photo_Couverture(String chemin,String nom){
		this.cheminPhotoCouverture = chemin;
		this.nomPhotoCouverture = nom;
	}	

	/**
	 * Suppression d'une instance
	 */
	public void finalize() throws Throwable {

	}

	// GETTER SETTER
	
	public String getCheminPhotoCouverture() {
		return cheminPhotoCouverture;
	}

	public void setCheminPhotoCouverture(String cheminPhotoCouverture) {
		this.cheminPhotoCouverture = cheminPhotoCouverture;
	}

	public long getIdPhotoCouverture() {
		return idPhotoCouverture;
	}

	public void setIdPhotoCouverture(long idPhotoCouverture) {
		this.idPhotoCouverture = idPhotoCouverture;
	}

	public String getNomPhotoCouverture() {
		return nomPhotoCouverture;
	}

	public void setNomPhotoCouverture(String nomPhotoCouverture) {
		this.nomPhotoCouverture = nomPhotoCouverture;
	}


	

}