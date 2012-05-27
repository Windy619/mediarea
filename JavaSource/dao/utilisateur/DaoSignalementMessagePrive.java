package dao.utilisateur;

import metier.utilisateur.Signalement_MessagePrive;
import metier.utilisateur.Signalement_Utilisateur;
import dao.Dao;

public class DaoSignalementMessagePrive extends Dao<Signalement_MessagePrive> {

	/**
	 * Constructeur
	 */
	public DaoSignalementMessagePrive() {
		super(Signalement_MessagePrive.class);
	}
}
