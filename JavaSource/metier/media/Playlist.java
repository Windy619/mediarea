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

/**
 * @author Florence
 * @version 1.0
 * @created 26-mars-2012 22:26:59
 */
@Entity
/**
 * Class Playlist
 * Represente la Playlist d'un Utilisateur
 * @author Benjamin
 *
 */
public class Playlist {

	@Id
	@GeneratedValue
	private long idPlaylist;
	
	private Date dateCreationPlaylist;
	
	private String descriptionPlaylist;

	private long nbRegard;
	
	private String nomPlaylist;
	
	private String remarqueMedia;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Type_Playlist type;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Visibilite visibilite;
	
	@OneToMany(cascade = {CascadeType.ALL})
	private Set<Media> medias;

	/**
	 * Constructeur vide
	 */
	public Playlist(){
		medias = new HashSet<Media>();
	}	
	
	/**
	 * Constructeur par défaut
	 * @param nomPlaylist
	 * @param descriptionPlaylist
	 * @param remarqueMedia
	 * @param type
	 * @param visibilite
	 */
	public Playlist(String nomPlaylist,String descriptionPlaylist, String remarqueMedia,Type_Playlist type, Visibilite visibilite) {
		this.dateCreationPlaylist = new Date();
		this.descriptionPlaylist = descriptionPlaylist;
		this.nbRegard = 0;
		this.nomPlaylist = nomPlaylist;
		this.remarqueMedia = remarqueMedia;
		this.type = type;
		this.visibilite = visibilite;
		medias = new HashSet<Media>();
	}
	
	public Playlist(String nomPlaylist,String descriptionPlaylist) {
		this.descriptionPlaylist = descriptionPlaylist;
		this.nomPlaylist = nomPlaylist;
	}
	
	/**
	 * Suppression de l'instance
	 */
	public void finalize() throws Throwable {

	}

	// GETTER SETTER
	
	public Date getDateCreationPlaylist() {
		return dateCreationPlaylist;
	}

	public void setDateCreationPlaylist(Date dateCreationPlaylist) {
		this.dateCreationPlaylist = dateCreationPlaylist;
	}

	public String getDescriptionPlaylist() {
		return descriptionPlaylist;
	}

	public void setDescriptionPlaylist(String descriptionPlaylist) {
		this.descriptionPlaylist = descriptionPlaylist;
	}

	public long getIdPlaylist() {
		return idPlaylist;
	}

	public void setIdPlaylist(long idPlaylist) {
		this.idPlaylist = idPlaylist;
	}

	public long getNbRegard() {
		return nbRegard;
	}

	public void setNbRegard(long nbRegard) {
		this.nbRegard = nbRegard;
	}

	public String getNomPlaylist() {
		return nomPlaylist;
	}

	public void setNomPlaylist(String nomPlaylist) {
		this.nomPlaylist = nomPlaylist;
	}

	public String getRemarqueMedia() {
		return remarqueMedia;
	}

	public void setRemarqueMedia(String remarqueMedia) {
		this.remarqueMedia = remarqueMedia;
	}

	public Type_Playlist getType() {
		return type;
	}

	public void setType(Type_Playlist type) {
		this.type = type;
	}

	public Visibilite getVisibilite() {
		return visibilite;
	}

	public void setVisibilite(Visibilite visibilite) {
		this.visibilite = visibilite;
	}

	public Set<Media> getMedias() {
		return medias;
	}

	public void setMedias(Set<Media> medias) {
		this.medias = medias;
	}
	
	public String toString(){
		return ("P"+String.valueOf(idPlaylist));
	}

}