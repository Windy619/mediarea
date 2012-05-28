package metier.media;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Florence
 * @version 1.0
 * @created 26-mars-2012 22:27:00
 */
@Entity
/**
 * Class Tag
 * Tag d'un Media
 * @author Benjamin
 *
 */
public class Tag {

	@Id
	@GeneratedValue
	private long idTag;
	
	private String nomTag;

	/**
	 * Constructeur vide
	 */
	public Tag(){}
	
	/**
	 * Constructeur par défaut
	 * @param nomTag
	 */
	public Tag(String nomTag){
		this.nomTag = nomTag;
	}
	
	@Override
	public String toString() {
		return nomTag;
	}
	
	// GETTER SETTER

	public long getIdTag() {
		return idTag;
	}

	public void setIdTag(long idTag) {
		this.idTag = idTag;
	}

	public String getNomTag() {
		return nomTag;
	}

	public void setNomTag(String nomTag) {
		this.nomTag = nomTag;
	}	

	


}