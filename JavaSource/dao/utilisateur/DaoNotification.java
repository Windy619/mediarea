package dao.utilisateur;


import java.util.List;

import metier.utilisateur.Notification;
import metier.utilisateur.Utilisateur;

import org.hibernate.Query;

import dao.Dao;

public class DaoNotification extends Dao<Notification>{

	/**
	 * Constructeur
	 */
	public DaoNotification() {
		super(Notification.class);
	}
	
	/**
	 * Récupération des notifications d'un Utilisateur
	 * @param u
	 * @return
	 */
	public List<?> notifications(Utilisateur u) {
		
		Query q = session.createQuery("" +
				"FROM Notification n " +
				"WHERE n.dateSuppressionNotification IS NULL  " +
				"	AND n.dateLectureNotification IS NULL  " +
				"	AND n.destinnataireNotification.idUtilisateur = :idConnecte");
				
		q.setParameter("idConnecte", u.getIdUtilisateur());		

		return q.list();		
	}
}
