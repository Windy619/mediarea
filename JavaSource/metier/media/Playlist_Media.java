package metier.media;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
/**
 * Classe de jointure entre media et playlist
 * @author Florence
 *
 */
public class Playlist_Media {

	@Id
	@GeneratedValue
	private long idPlaylistMedia;
	
	private long media;
	
	private long playlist;
	
	/**
	 * Constructeur
	 */
	public Playlist_Media() {}
	
	public Playlist_Media(long media, long playlist) {
		this.media = media;
		this.playlist = playlist;
	}
	
	// GETTER SETTER
	public long getIdPlaylistMedia() {
		return idPlaylistMedia;
	}

	public void setIdPlaylistMedia(long idPlaylistMedia) {
		this.idPlaylistMedia = idPlaylistMedia;
	}

	public long getMedia() {
		return media;
	}

	public void setMedia(long media) {
		this.media = media;
	}

	public long getPlaylist() {
		return playlist;
	}

	public void setPlaylist(long playlist) {
		this.playlist = playlist;
	}	
	
}
