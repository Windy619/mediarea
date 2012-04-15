package dao.utilisateur;

import metier.utilisateur.Signalement_Utilisateur;
import dao.Dao;

public class DaoSignalementUtilisateur extends Dao<Signalement_Utilisateur>{

	/**
	 * Constructeur
	 */
	public DaoSignalementUtilisateur() {
		super(Signalement_Utilisateur.class);
	}
	
}
