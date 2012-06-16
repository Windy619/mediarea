package dao.media;

import metier.media.Categorie_Media;
import metier.media.Playlist_Media;
import dao.Dao;

public class DaoPlaylistMedia extends Dao<Playlist_Media> {
	
	public DaoPlaylistMedia() {
		super(Playlist_Media.class);
	}
}
