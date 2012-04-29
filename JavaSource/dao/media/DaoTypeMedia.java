package dao.media;

import java.util.List;

import metier.media.Type_Media;
import dao.Dao;

public class DaoTypeMedia extends Dao<Type_Media>{

	/**
	 * Constructeur
	 */
	public DaoTypeMedia() {
		super(Type_Media.class);
	}
	
	/**
	 * R�cup�re le Type_Media video (la cr�er si n'existe pas)
	 * @return L'Objet Categorie
	 */
	public Type_Media typeVideo() {
		
		List<?> liste = session.getNamedQuery(Type_Media.NQ_VIDEO).list();

		if (liste.size() == 0) {
			// Si le type n'existe pas encore, on le cr�er
			this.sauvegarder(new Type_Media("video"));
			liste = session.getNamedQuery(Type_Media.NQ_VIDEO).list();
		}	
		
		return (Type_Media)liste.get(0);
	}
	
	/**
	 * R�cup�re le Type_Media son (la cr�er si n'existe pas)
	 * @return L'Objet Categorie
	 */
	public Type_Media typeSon() {
		
		List<?> liste = session.getNamedQuery(Type_Media.NQ_AUDIO).list();

		if (liste.size() == 0) {
			// Si le type n'existe pas encore, on le cr�er
			this.sauvegarder(new Type_Media("audio"));
			liste = session.getNamedQuery(Type_Media.NQ_AUDIO).list();
		}	
		
		return (Type_Media)liste.get(0);
	}		
}
