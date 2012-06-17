package dao.media;

import metier.media.Telechargement_Media;
import dao.Dao;

/**
 * Dao de la classe Téléchargement Média
 * @author Florence
 *
 */
public class DaoTelechargementMedia extends Dao<Telechargement_Media> {
	/**
	 * Constructeur
	 */
	public DaoTelechargementMedia() {
		super(Telechargement_Media.class);
	}
}
