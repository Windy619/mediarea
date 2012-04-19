package dao.utilisateur;

import java.util.List;

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
	 * Recherche simple à partir d'une String
	 * @param mot
	 * @return Une liste d'utilisateur
	 */
	public List<?> recherche(String recherche) {
		String param = "%" + recherche + "%";
		
		Query q = session.createQuery("" +
				"FROM Utilisateur " +
				"WHERE adrMail like :recherche or pseudo like :recherche or prenomUtilisateur like :recherche or nomUtilisateur like :recherche " +
				"AND dateBanissement IS NULL AND dateSuppressionUtilisateur IS NULL");
		q.setParameter("recherche", param);		
		
		return q.list();
	}	

}
