package metier.media;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import metier.utilisateur.Utilisateur;

/**
 * @author MediArea
 * @version 1.0
 * @created 26-mars-2012 22:26:59
 */

@Entity
/**
 * Class Media
 * @author MediArea
 *
 */
public class Media {

	public static final String NQ_RECHERCHE = "recherche_media";
	
	@Id
	@GeneratedValue
	private long idMedia;
	
	private boolean aCommentairesOuverts;
	
	private Date datePublication;
	
	private String descriptionMedia;
	
	private boolean estTelechargementAutorise;

	private String mdpMedia;
	
	private String titreMedia;
	
	@ManyToOne(cascade = {CascadeType.ALL})
	private Utilisateur auteurMedia;
	
	@ManyToOne(cascade = {CascadeType.ALL})
	private Visibilite visibilite;
	
	@ManyToOne(cascade = {CascadeType.ALL})
	private Photo_Couverture photo;
	
	@ManyToOne(cascade = {CascadeType.ALL})
	private Type_Media type;	
	
	@OneToMany(cascade = {CascadeType.ALL})
	private Set<Categorie> categories;
	
	@OneToMany(cascade = {CascadeType.ALL})
	private Set<Tag> tags;
	
	@OneToMany(cascade = {CascadeType.ALL})
	private Set<Commentaire> commentaires;

	/**
	 * Constructeur vide
	 */
	public Media(){
		categories = new HashSet<Categorie>();
		tags = new HashSet<Tag>();
		commentaires = new HashSet<Commentaire>();
	}
	
	/**
	 * Constructeur par défaut
	 * @param aCommentairesOuverts
	 * @param auteurMedia
	 * @param descriptionMedia
	 * @param estTelechargementAutorise
	 * @param mdpMedia
	 * @param titreMedia
	 * @param visibilite
	 * @param photo
	 */
	public Media(boolean aCommentairesOuverts,Utilisateur auteurMedia,String descriptionMedia, boolean estTelechargementAutorise, String mdpMedia, 
			String titreMedia, Visibilite visibilite,Photo_Couverture photo){
		this.aCommentairesOuverts = aCommentairesOuverts;
		this.auteurMedia = auteurMedia;
		this.datePublication = new Date();
		this.descriptionMedia = descriptionMedia;
		this.estTelechargementAutorise = estTelechargementAutorise;
		this.mdpMedia = mdpMedia;
		this.titreMedia = titreMedia;
		this.visibilite = visibilite;
		this.photo = photo;
		categories = new HashSet<Categorie>();
		tags = new HashSet<Tag>();
		commentaires = new HashSet<Commentaire>();
	}	

	/**
	 * Suppression de l'instance
	 */
	public void finalize() throws Throwable {

	}

	// GETTER SETTER
	
	public boolean isaCommentairesOuverts() {
		return aCommentairesOuverts;
	}

	public void setaCommentairesOuverts(boolean aCommentairesOuverts) {
		this.aCommentairesOuverts = aCommentairesOuverts;
	}

	public Utilisateur getAuteurMedia() {
		return auteurMedia;
	}

	public void setAuteurMedia(Utilisateur auteurMedia) {
		this.auteurMedia = auteurMedia;
	}

	public Date getDatePublication() {
		return datePublication;
	}

	public void setDatePublication(Date datePublication) {
		this.datePublication = datePublication;
	}

	public String getDescriptionMedia() {
		return descriptionMedia;
	}

	public void setDescriptionMedia(String descriptionMedia) {
		this.descriptionMedia = descriptionMedia;
	}

	public boolean isEstTelechargementAutorise() {
		return estTelechargementAutorise;
	}

	public void setEstTelechargementAutorise(boolean estTelechargementAutorise) {
		this.estTelechargementAutorise = estTelechargementAutorise;
	}

	public long getIdMedia() {
		return idMedia;
	}

	public void setIdMedia(long idMedia) {
		this.idMedia = idMedia;
	}

	public String getMdpMedia() {
		return mdpMedia;
	}

	public void setMdpMedia(String mdpMedia) {
		this.mdpMedia = mdpMedia;
	}

	public String getTitreMedia() {
		return titreMedia;
	}

	public void setTitreMedia(String titreMedia) {
		this.titreMedia = titreMedia;
	}

	public Visibilite getVisibilite() {
		return visibilite;
	}

	public void setVisibilite(Visibilite visibilite) {
		this.visibilite = visibilite;
	}

	public Photo_Couverture getPhoto() {
		return photo;
	}

	public void setPhoto(Photo_Couverture photo) {
		this.photo = photo;
	}

	public Set<Categorie> getCategories() {
		return categories;
	}

	public void setCategories(Set<Categorie> categories) {
		this.categories = categories;
	}

	public Type_Media getType() {
		return type;
	}

	public void setType(Type_Media type) {
		this.type = type;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public Set<Commentaire> getCommentaires() {
		return commentaires;
	}

	public void setCommentaires(Set<Commentaire> commentaires) {
		this.commentaires = commentaires;
	}

	

}