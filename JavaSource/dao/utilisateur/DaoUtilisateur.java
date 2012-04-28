package dao.utilisateur;

import java.util.List;

import metier.utilisateur.Amitie;
import metier.utilisateur.Utilisateur;

import org.hibernate.Query;

import dao.Dao;

public class DaoUtilisateur extends Dao<Utilisateur> {
	
	/**
	 * Constructeur
	 */
	public DaoUtilisateur() {
		super(Utilisateur.class);
	}
		
	/**
	 * Récupère la liste des utilisateurs non bannis et non supprimés
	 */
	public List<?> getValides() {
		return session.getNamedQuery(Utilisateur.NQ_VALIDES).list();
	}
	
	/**
	 * Liste des utilisateurs non valides (bannis ou supprimes)
	 */
	public List<?> getNonValides() {
		return session.getNamedQuery(Utilisateur.NQ_NON_VALIDES).list();
	}
	
	/**
	 * Liste des utilisateurs bannis
	 */
	public List<?> getBannis() {
		return session.getNamedQuery(Utilisateur.NQ_BANNIS).list();
	}
	
	/**
	 * Liste des utilisateurs supprimes
	 */
	public List<?> getSupprimes() {
		return session.getNamedQuery(Utilisateur.NQ_SUPPRIMES).list();
	}	
	
	/**
	 * Liste des utilisateurs administrateurs
	 */
	public List<?> getAdministrateurs() {
		return session.getNamedQuery(Utilisateur.NQ_ADMINISTRATEURS).list();
	}	
	
	/**
	 * Liste des utilisateurs par pseudo
	 */
	public List<?> getPseudo(String pseudo) {
		return session.getNamedQuery(Utilisateur.NQ_PSEUDO).setParameter("pseudo",pseudo).list();
	}	
	
	/**
	 * Liste des utilisateurs par mail
	 */
	public List<?> getMail(String mail) {
		return session.getNamedQuery(Utilisateur.NQ_MAIL).setParameter("mail",mail).list();
	}		
	
	/**
	 * Recherche simple à partir d'un String
	 * @param mot
	 * @return Une liste d'utilisateur
	 */
	public List<?> recherche(String recherche) {
		String param = "%" + recherche + "%";
		
		Query q = session.createQuery("" +
				"FROM Utilisateur " +
				"WHERE (adrMail like :recherche or pseudo like :recherche or prenomUtilisateur like :recherche or nomUtilisateur like :recherche) " +
				"AND dateBanissement IS NULL AND dateSuppressionUtilisateur IS NULL");
		q.setParameter("recherche", param);		

		return q.list();
	}	
	
	/**
	 * Recherche simple d'un amis à partir d'un String
	 * @param mot
	 * @return Une liste d'amitie
	 */
	public List<?> rechercheAmis(String recherche, Utilisateur u) {
		String param = "%" + recherche + "%";
		
		Query q = session.createQuery("" +
				"FROM Amitie as a " + 
				"WHERE (a.ami.adrMail like :recherche or a.ami.pseudo like :recherche or a.ami.prenomUtilisateur like :recherche or a.ami.nomUtilisateur like :recherche) " + 
					"AND dateBanissement IS NULL AND dateSuppressionUtilisateur IS NULL " + 
					"AND a.utilisateur.idUtilisateur = :idConnecte");
		q.setParameter("recherche", param);		
		q.setParameter("idConnecte", u.getIdUtilisateur());	

		return q.list();
	}	
	
	/**
	 * Recherche simple à partir d'un String (sans les amis)
	 * @return Une liste d'utilisateur
	 */
	public List<?> rechercheNonAmis(String recherche, Utilisateur u) {
		String param = "%" + recherche + "%";
		
		Query q = session.createQuery("" +
				"FROM Utilisateur as u " +
				"WHERE (u.adrMail like :recherche or u.pseudo like :recherche or u.prenomUtilisateur like :recherche or u.nomUtilisateur like :recherche) " +
				"AND u.dateBanissement IS NULL AND u.dateSuppressionUtilisateur IS NULL " +
				"AND u.idUtilisateur <> :idConnecte " +
				"AND u.idUtilisateur NOT IN (SELECT a.ami FROM Amitie as a WHERE a.utilisateur.idUtilisateur = :idConnecte) " +
				"");
		
		q.setParameter("recherche", param);		
		q.setParameter("idConnecte", u.getIdUtilisateur());	
		
		return q.list();
	}	
	
	/**
	 * Recherche d'amis possible d'un utilisateur
	 * @return Une liste d'utilisateur
	 */
	public List<?> rechercheAmisPossible(Utilisateur u) {

		Query q = session.createQuery("" +
				"FROM Amitie as a " +
				"WHERE (a.ami.idUtilisateur IN (SELECT a.ami.idUtilisateur FROM Amitie as a WHERE a.utilisateur.idUtilisateur = :idConnecte) " +
				"	OR a.utilisateur.idUtilisateur IN (SELECT a.ami.idUtilisateur FROM Amitie as a WHERE a.utilisateur.idUtilisateur = :idConnecte) " +
				"	OR a.utilisateur.idUtilisateur NOT IN (SELECT a.ami FROM Amitie as a WHERE a.utilisateur.idUtilisateur = :idConnecte)) " +
				"	AND a.utilisateur.idUtilisateur <> :idConnecte " +
				"");
		q.setParameter("idConnecte", u.getIdUtilisateur());	
		
		return q.list();
	}	
	
	/**
	 * Trouve les utilisateurs avec lequels on a entamer une discussion par message prive
	 * @param u
	 * @return
	 */
	
	/*
	public List<?> getCorrespondancesPrives(Utilisateur u) {
		Query q = session.createQuery("" +
				"FROM Utilisateur as u " +
				"WHERE u.idUtilisateur <> :idConnecte " +
				"	AND (u.idUtilisateur IN ( SELECT mp.emetteur.idUtilisateur FROM Message_Prive as mp WHERE mp.destinataire = :idConnecte) " +
				"OR u.idUtilisateur IN ( SELECT mp.destinataire.idUtilisateur FROM Message_Prive as mp WHERE mp.emetteur = :idConnecte)) " +
				"");
		q.setParameter("idConnecte", u.getIdUtilisateur());	
		
		return q.list();		
	}*/
}
