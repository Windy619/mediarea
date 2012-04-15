package dao.utilisateur;


import metier.utilisateur.Notification;
import dao.Dao;

public class DaoNotification extends Dao<Notification>{

	/**
	 * Constructeur
	 */
	public DaoNotification() {
		super(Notification.class);
	}
}
