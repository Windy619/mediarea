package metier.media;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
/**
 * Classe de jointure entre media et categorie
 * @author Benjamin
 *
 */
public class Categorie_Media {

	@Id
	@GeneratedValue
	private long idCategorieMedia;
	
	private long media;
	
	private long categorie;
	
	/**
	 * Constructeur
	 */
	public Categorie_Media(long media, long categorie) {
		this.media = media;
		this.categorie = categorie;
	}
	
	// GETTER SETTER
	public long getIdCategorieMedia() {
		return idCategorieMedia;
	}

	public void setIdCategorieMedia(long idCategorieMedia) {
		this.idCategorieMedia = idCategorieMedia;
	}

	public long getMedia() {
		return media;
	}

	public void setMedia(long media) {
		this.media = media;
	}

	public long getCategorie() {
		return categorie;
	}

	public void setCategorie(long categorie) {
		this.categorie = categorie;
	}	
	
}
