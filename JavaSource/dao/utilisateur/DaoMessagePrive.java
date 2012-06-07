package dao.utilisateur;

import metier.utilisateur.Message_Prive;
import dao.Dao;

public class DaoMessagePrive extends Dao<Message_Prive>{

	/**
	 * Constructeur
	 */
	public DaoMessagePrive() {
		super(Message_Prive.class);
	}
	
}
