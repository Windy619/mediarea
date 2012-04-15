package dao.media;

import java.util.List;

import metier.media.Categorie;
import dao.Dao;

public class DaoCategorie extends Dao<Categorie> {

	/**
	 * Constructeur
	 */
	public DaoCategorie() {
		super(Categorie.class);
	}
	
	/**
	 * R�cup�re une categorie (la cr�er si n'existe pas)
	 * @return L'Objet Categorie
	 */
	public Categorie categorie(String nom) {
		
		List<?> liste = session.getNamedQuery(Categorie.NQ_CATEGORIE).setParameter("nom", nom).list();

		if (liste.size() == 0) {
			// Si la categorie n'existe pas encore, on la cr�er
			this.ajouter(new Categorie(nom));
			liste = session.getNamedQuery(Categorie.NQ_CATEGORIE).setParameter("nom", nom).list();
		}	
		
		return (Categorie)liste.get(0);
	}

	
	
}
