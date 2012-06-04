package dao.media;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import metier.media.Commentaire;
import metier.media.Signalement_Commentaire;
import metier.utilisateur.Utilisateur;
import dao.Dao;

public class DaoSignalementCommentaire extends Dao<Signalement_Commentaire> {

	/**
	 * Constructeur
	 */
	public DaoSignalementCommentaire() {
		super(Signalement_Commentaire.class);
	}	
	
	
	public List<?> rechercheListSC(
			Commentaire commentaire) {
		Query q = session.createQuery("" +
				"FROM Signalement_Commentaire " +
				"WHERE commentaire = :commentaire " +
				"");
		
		q.setParameter("commentaire", commentaire);	
		
		return q.list();
	}


}
