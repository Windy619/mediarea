package metier.media;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
/**
 * Classe représentant un fichier (Media)
 * @author Benjamin
 *
 */
public class Fichier {

	@Id
	@GeneratedValue
	private Long idFichier;
	
	private String cheminFichier;
	
	private String nomFichier;
	
	/**
	 * Constructeur par défaut
	 */
	public Fichier() {
		
	}
	
	/**
	 * Constructeur
	 * @param chemin
	 * @param nom
	 */
	public Fichier(String chemin, String nom) {
		this.cheminFichier = chemin;
		this.nomFichier = nom;
	}
	
	/**
	 * Suppression d'une instance
	 */
	public void finalize() throws Throwable {

	}
	
	// GETTER SETTER
	
	public Long getIdFichier() {
		return idFichier;
	}

	public void setIdFichier(Long idFichier) {
		this.idFichier = idFichier;
	}

	public String getCheminFichier() {
		return cheminFichier;
	}

	public void setCheminFichier(String cheminFichier) {
		this.cheminFichier = cheminFichier;
	}

	public String getNomFichier() {
		return nomFichier;
	}

	public void setNomFichier(String nomFichier) {
		this.nomFichier = nomFichier;
	}	
	
}
