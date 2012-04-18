package dao.media;

import java.util.List;

import metier.media.Type_Playlist;
import dao.Dao;

public class DaoTypePlaylist extends Dao<Type_Playlist>{

	/**
	 * Constructeur
	 */
	public DaoTypePlaylist() {
		super(Type_Playlist.class);
	}
	
	/**
	 * R�cup�re le Type_Playlist 'Favoris' (la cr�er si n'existe pas)
	 * @return L'Objet Type_Playlist
	 */
	public Type_Playlist typeFavoris() {
		
		List<?> liste = session.getNamedQuery(Type_Playlist.NQ_FAVORIS).list();

		if (liste.size() == 0) {
			// Si le type n'existe pas encore, on le cr�er
			this.sauvegarder(new Type_Playlist("favoris"));
			liste = session.getNamedQuery(Type_Playlist.NQ_FAVORIS).list();
		}	
		
		return (Type_Playlist)liste.get(0);
	}
	
	/**
	 * R�cup�re le Type_Playlist 'en_cours' (la cr�er si n'existe pas)
	 * @return L'Objet Type_Playlist
	 */
	public Type_Playlist typeEnCours() {
		
		List<?> liste = session.getNamedQuery(Type_Playlist.NQ_EN_COURS).list();

		if (liste.size() == 0) {
			// Si le type n'existe pas encore, on le cr�er
			this.sauvegarder(new Type_Playlist("en_cours"));
			liste = session.getNamedQuery(Type_Playlist.NQ_EN_COURS).list();
		}	
		
		return (Type_Playlist)liste.get(0);
	}		
	
	/**
	 * R�cup�re le Type_Playlist 'autre' (la cr�er si n'existe pas)
	 * @return L'Objet Type_Playlist
	 */
	public Type_Playlist typeAutre() {
		
		List<?> liste = session.getNamedQuery(Type_Playlist.NQ_AUTRE).list();

		if (liste.size() == 0) {
			// Si le type n'existe pas encore, on le cr�er
			this.sauvegarder(new Type_Playlist("autre"));
			liste = session.getNamedQuery(Type_Playlist.NQ_AUTRE).list();
		}	
		
		return (Type_Playlist)liste.get(0);
	}		
}

