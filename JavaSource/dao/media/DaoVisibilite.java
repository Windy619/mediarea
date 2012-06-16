package dao.media;

import java.util.List;

import metier.media.Visibilite;
import dao.Dao;

public class DaoVisibilite extends Dao<Visibilite> {

	/**
	 * Constructeur
	 */
	public DaoVisibilite() {
		super(Visibilite.class);
	}
	
	/**
	 * Récupère la visiblité "Visible"
	 * @return L'Objet Visibilite
	 */
	public Visibilite typeVisible() {

		List<?> liste = session.getNamedQuery(Visibilite.NQ_VISIBLE).list();

		if (liste.size() == 0) {
			// Si le type n'existe pas encore, on le créer
			this.sauvegarder(new Visibilite("Public"));
			liste = session.getNamedQuery(Visibilite.NQ_VISIBLE).list();
		}		
		
		return (Visibilite)liste.get(0);
	}
	
	/**
	 * Récupère la visiblité "Non_Visible"
	 * @return L'Objet Visibilite
	 */
	public Visibilite typeNonVisible() {
		
		List<?> liste = session.getNamedQuery(Visibilite.NQ_NON_VISIBLE).list();
		
		if (liste.size() == 0) {
			// Si le type n'existe pas encore, on le créer
			this.sauvegarder(new Visibilite("Prive"));
			liste = session.getNamedQuery(Visibilite.NQ_NON_VISIBLE).list();
		}
				
		return (Visibilite)liste.get(0);		
	}	
	
	
}
