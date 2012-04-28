package metier.media;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * @author Florence
 * @version 1.0
 * @created 26-mars-2012 22:27:00
 */
@Entity
@NamedQueries({
	@NamedQuery(name = Visibilite.NQ_VISIBLE, query = "FROM Visibilite WHERE nomVisibilite = 'visible'"),
	@NamedQuery(name = Visibilite.NQ_NON_VISIBLE, query = "FROM Visibilite WHERE nomVisibilite = 'non_visible'")
})
/**
 * Class Visibilite
 * Visibilite d'un Objet
 * @author Benjamin
 *
 */
public class Visibilite {

	public static final String NQ_VISIBLE = "visible";
	
	public static final String NQ_NON_VISIBLE = "non_visible";
	
	@Id
	@GeneratedValue
	private long idVisibilite;
	
	private String nomVisibilite;

	/**
	 * Constructeur vide
	 */
	public Visibilite(){}
	
	/**
	 * Constructeur par défaut
	 * @param nomVisible
	 * 
	 */
	public Visibilite(String nomVisible) {
		this.nomVisibilite = nomVisible;
	}	

	/**
	 * Suppression d'une instance
	 */
	public void finalize() throws Throwable {

	}

	// GETTER SETTER
	
	public long getIdVisibilite() {
		return idVisibilite;
	}

	public void setIdVisibilite(long idVisibilite) {
		this.idVisibilite = idVisibilite;
	}

	public String getNomVisibilite() {
		return nomVisibilite;
	}

	public void setNomVisible(String nomVisibilite) {
		this.nomVisibilite = nomVisibilite;
	}
	
	

}