package dao.utilisateur;

import metier.utilisateur.Message_Mural;
import dao.Dao;

public class DaoMessageMural extends Dao<Message_Mural>{

	/**
	 * Constructeur
	 */
	public DaoMessageMural() {
		super(Message_Mural.class);
	}
}
