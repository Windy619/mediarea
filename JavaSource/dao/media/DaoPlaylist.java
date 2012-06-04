package dao.media;

import java.util.List;

import org.hibernate.Query;

import metier.media.Media;
import metier.media.Playlist;
import metier.utilisateur.Utilisateur;
import dao.Dao;

public class DaoPlaylist extends Dao<Playlist>{

	/**
	 * Constructeur
	 */
	public DaoPlaylist() {
		super(Playlist.class);
	}	
	
	public Playlist rechercheSurID(Long recherche) {
		Long param = recherche;
		
		Query q = session.createQuery("" +
				"FROM Playlist " +
				"WHERE idPlaylist = :recherche");
		q.setParameter("recherche", param);		
		
		return (Playlist) q.uniqueResult();
	}
	
}
