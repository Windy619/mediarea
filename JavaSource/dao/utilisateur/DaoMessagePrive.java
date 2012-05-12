package dao.utilisateur;

import java.util.List;

import metier.utilisateur.Message_Prive;
import metier.utilisateur.Utilisateur;

import org.hibernate.Query;

import dao.Dao;

public class DaoMessagePrive extends Dao<Message_Prive>{

	/**
	 * Constructeur
	 */
	public DaoMessagePrive() {
		super(Message_Prive.class);
	}
	
	public List<?> getMessages(Utilisateur u) {
		Query q = session.createQuery("" +
				"FROM Message_Prive as mp " +
				"WHERE (mp.emetteur.idUtilisateur = :idConnecte AND mp.dateSuppressionMessage IS NULL AND mp.isMessageMere = true ) OR (mp.destinataire.idUtilisateur = :idConnecte AND mp.dateSuppressionMessageDestinataire IS NULL AND mp.isMessageMere = true )  " +
				"ORDER BY mp.dateEnvoi DESC" +
				"");
		q.setParameter("idConnecte", u.getIdUtilisateur());	
		
		return q.list();		
	}	
	
	public List<?> getMessagesEnvoyes(Utilisateur u) {
		Query q = session.createQuery("" +
				"FROM Message_Prive as mp " +
				"WHERE mp.emetteur.idUtilisateur = :idConnecte AND mp.dateSuppressionMessage IS NULL  " +
				"AND mp.isMessageMere = true " +
				"ORDER BY mp.dateEnvoi DESC" +
				"");
		q.setParameter("idConnecte", u.getIdUtilisateur());	
		
		return q.list();		
	}	
	
	public List<?> getMessagesRecus(Utilisateur u) {
		Query q = session.createQuery("" +
				"FROM Message_Prive as mp " +
				"WHERE mp.destinataire.idUtilisateur = :idConnecte AND mp.dateSuppressionMessageDestinataire IS NULL  " +
				"AND mp.isMessageMere = true " +
				"ORDER BY mp.dateEnvoi DESC" +
				"");
		q.setParameter("idConnecte", u.getIdUtilisateur());	
		
		return q.list();		
	}	
	
	public List<?> getReponses(Message_Prive mp, Utilisateur u) {
		Query q = session.createQuery("" +
				"SELECT reponse " +
				"FROM Message_Prive as mp join mp.reponses as reponse  " +
				"WHERE mp.idMessage = :idMp " +
				"	AND  (reponse.emetteur.idUtilisateur = :idConnecte AND reponse.dateSuppressionMessage IS NULL ) " +
				" 	OR (reponse.destinataire.idUtilisateur = :idConnecte AND reponse.dateSuppressionMessageDestinataire IS NULL) " +
				"ORDER BY reponse.dateEnvoi DESC  " +
				"");
		q.setParameter("idMp", mp.getIdMessage());	
		q.setParameter("idConnecte", u.getIdUtilisateur());
		
		return q.list();		
	}		
	
	public List<?> rechercherMessage(String recherche) {
		
		String param = "%" + recherche + "%";
		
		Query q = session.createQuery("" +
				"FROM Message_Prive as mp " +
				"WHERE (mp.destinataire.pseudo LIKE :recherche " +
				"	OR mp.emetteur.pseudo LIKE :recherche " +
				"	OR mp.objet LIKE :recherche) " +
				"	AND mp.dateSuppressionMessage IS NULL " +
				"	AND mp.isMessageMere = true " +
				"	AND mp.dateLecture IS NULL " +			
				"");
		q.setParameter("recherche", param);	
		
		return q.list();		
	}
	
	public List<?> getMessagesNonLus(Utilisateur u) {
		Query q = session.createQuery("" +
				"FROM Message_Prive as mp " +
				"WHERE mp.destinataire.idUtilisateur = :idConnecte " +
				"	AND mp.dateSuppressionMessage IS NULL " +
				"	AND mp.isMessageMere = true " +
				"	AND mp.dateLecture IS NULL " +			
				"");
		q.setParameter("idConnecte", u.getIdUtilisateur());	
		
		return q.list();		
	}		
	
}
