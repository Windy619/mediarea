package metier.media;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;



/**
 * @author Benjamin
 * @version 1.0
 * @created 26-mars-2012 22:27:00
 */
@Entity
@NamedQueries({
	@NamedQuery(name = Type_Media.NQ_VIDEO, query = "FROM Type_Media WHERE nomTypeMedia = 'video' "),
	@NamedQuery(name = Type_Media.NQ_SON, query = "FROM Type_Media WHERE nomTypeMedia = 'son' ")
})
/**
 * Class Type_Media
 * Type de media
 * @author Benjamin
 *
 */
public class Type_Media {

	public static final String NQ_VIDEO = "categorie_video";
	
	public static final String NQ_SON = "categorie_son";
	
	@Id
	@GeneratedValue
	private long idTypeMedia;
	
	private String nomTypeMedia;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Photo_Couverture photo;

	/**
	 * Constructeur vide
	 */
	public Type_Media(){}
	
	/**
	 * Constructeur par défaut
	 * @param nomTypeMedia
	 */
	public Type_Media(String nomTypeMedia){
		this.nomTypeMedia = nomTypeMedia;
		this.photo = null;
	}	

	/**
	 * Suppression d'une instance
	 */
	public void finalize() throws Throwable {

	}
	
	// GETTER SETTER

	public long getIdTypeMedia() {
		return idTypeMedia;
	}

	public void setIdTypeMedia(long idTypeMedia) {
		this.idTypeMedia = idTypeMedia;
	}

	public String getNomTypeMedia() {
		return nomTypeMedia;
	}

	public void setNomTypeMedia(String nomTypeMedia) {
		this.nomTypeMedia = nomTypeMedia;
	}

	public Photo_Couverture getPhoto() {
		return photo;
	}

	public void setPhoto(Photo_Couverture photo) {
		this.photo = photo;
	}

	
}