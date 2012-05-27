package metier.media;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * @author Benjamin
 * @version 1.0
 * @created 26-mars-2012 22:26:59
 */

@Entity
@NamedQueries({
	@NamedQuery(name = Categorie.NQ_CATEGORIE, query = "FROM Categorie WHERE nomCategorie = :nom ")
})
/**
 * Class Categorie
 * Represente la Categorie d'un Media
 * @author Benjamin
 *
 */
public class Categorie {
	
	public static final String NQ_CATEGORIE = "categorie";
	

	@Id
	@GeneratedValue
	private long idCategorie;
	
	private String nomCategorie;
	

	/**
	 * Constructeur vide
	 */
	public Categorie(){}
	
	/**
	 * Constructeur par défaut
	 * @param nomCategorie
	 */
	public Categorie(String nomCategorie){
		this.nomCategorie = nomCategorie;
	}	

	/**
	 * Suppression de l'instance
	 */
	public void finalize() throws Throwable {

	}

	// GETTER SETTER
	
	public long getIdCategorie() {
		return idCategorie;
	}

	public void setIdCategorie(long idCategorie) {
		this.idCategorie = idCategorie;
	}

	public String getNomCategorie() {
		return nomCategorie;
	}

	public void setNomCategorie(String nomCategorie) {
		this.nomCategorie = nomCategorie;
	}
	
	

}