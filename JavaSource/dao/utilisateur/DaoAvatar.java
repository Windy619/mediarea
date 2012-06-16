package dao.utilisateur;

import java.util.List;

import org.hibernate.Query;

import metier.utilisateur.Avatar;
import metier.utilisateur.Utilisateur;
import dao.Dao;

public class DaoAvatar extends Dao<Avatar> {

	/**
	 * Constructeur
	 */
	public DaoAvatar() {
		super(Avatar.class);
	}
	
	public Avatar recherche(long idAvatar) {
		long param = idAvatar;
		
		Query q = session.createQuery("" +
				"FROM Avatar " +
				"WHERE idAvatar = :idAvatar ");
		q.setParameter("idAvatar", param);		
		
		return (Avatar) q.uniqueResult();
	}
	
	public List<?> listAvatar() {		
		Query q = session.createQuery("" +
				"FROM Avatar");
		return q.list();
	}
	
}
