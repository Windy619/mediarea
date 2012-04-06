package dao.utilisateur;

import java.util.List;

import metier.utilisateur.Utilisateur;
import dao.Dao;

public class Dao_Utilisateur extends Dao<Utilisateur> {
	
	/**
	 * Constructeur
	 */
	public Dao_Utilisateur() {
		super(Utilisateur.class);
	}
	
	/**
	 * Récupère la liste des utilisateurs non bannis et non supprimés
	 */
	public List<?> valides() {
		return session.getNamedQuery(Utilisateur.NQ_VALIDES).list();
	}
	
	/**
	 * Liste des utilisateurs non valides (bannis ou supprimes)
	 */
	public List<?> nonValides() {
		return session.getNamedQuery(Utilisateur.NQ_NON_VALIDES).list();
	}
	
	/**
	 * Liste des utilisateurs bannis
	 */
	public List<?> bannis() {
		return session.getNamedQuery(Utilisateur.NQ_BANNIS).list();
	}
	
	/**
	 * Liste des utilisateurs supprimes
	 */
	public List<?> supprimes() {
		return session.getNamedQuery(Utilisateur.NQ_SUPPRIMES).list();
	}	
	
	/**
	 * Liste des utilisateurs administrateurs
	 */
	public List<?> administrateurs() {
		return session.getNamedQuery(Utilisateur.NQ_ADMINISTRATEURS).list();
	}	
	
	/**
	 * Liste des utilisateurs par pseudo
	 */
	public List<?> pseudo(String pseudo) {
		return session.getNamedQuery(Utilisateur.NQ_PSEUDO).setParameter("pseudo",pseudo).list();
	}	
	
	/**
	 * Liste des utilisateurs par mail
	 */
	public List<?> mail(String mail) {
		return session.getNamedQuery(Utilisateur.NQ_MAIL).setParameter("mail",mail).list();
	}		
	

}
