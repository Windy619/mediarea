package metier.media;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * @author Benjamin
 * @version 1.0
 * @created 26-mars-2012 22:27:00
 */
@Entity
@NamedQueries({
	@NamedQuery(name = Type_Playlist.NQ_FAVORIS, query = "FROM Type_Playlist WHERE nomTypePlaylist = 'favoris' "),
	@NamedQuery(name = Type_Playlist.NQ_EN_COURS, query = "FROM Type_Playlist WHERE nomTypePlaylist = 'en_cours' "),
	@NamedQuery(name = Type_Playlist.NQ_AUTRE, query = "FROM Type_Playlist WHERE nomTypePlaylist = 'autre' ")
})
/**
 * Class Type_Playlist
 * Représente le type d'une Playlist 
 * @author Benjamin
 *
 */
public class Type_Playlist {

	public static final String NQ_FAVORIS = "type_playlist_favoris";
	
	public static final String NQ_EN_COURS = "type_playlist_en_cours";
	
	public static final String NQ_AUTRE = "type_playlist_autre";
	
	@Id
	@GeneratedValue
	private long idTypePlaylist;
	
	private String nomTypePlaylist;

	/**
	 * Constructeur vide
	 */
	public Type_Playlist(){}
	
	/**
	 * Constructeur par défaut
	 * @param nomTypePlaylist
	 */
	public Type_Playlist(String nomTypePlaylist){
		this.nomTypePlaylist = nomTypePlaylist;
	}	

	/**
	 * Supression d'une instance
	 */
	public void finalize() throws Throwable {

	}
	
	// GETTER SETTER

	public long getIdTypePlaylist() {
		return idTypePlaylist;
	}

	public void setIdTypePlaylist(long idTypePlaylist) {
		this.idTypePlaylist = idTypePlaylist;
	}

	public String getNomTypePlaylist() {
		return nomTypePlaylist;
	}

	public void setNomTypePlaylist(String nomTypePlaylist) {
		this.nomTypePlaylist = nomTypePlaylist;
	}
	
	

}