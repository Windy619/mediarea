package dao.media;

import java.util.ArrayList;
import java.util.List;

import metier.media.Categorie;
import metier.media.Commentaire;
import metier.media.Media;
import metier.media.Type_Media;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.Query;


import dao.Dao;

public class DaoMedia extends Dao<Media> {
	
	/**
	 * Constructeur
	 */
	public DaoMedia() {
		super(Media.class);
	}
	
	/**
	 * Recherche avancée
	 * @param titre
	 * @param description
	 * @param estTelechargementAutorise
	 * @param type
	 * @param tag
	 * @param categorie
	 * @return Une liste de média
	 */
	public List<?> rechercheAvance(String titre, String description, boolean estTelechargementAutorise, Type_Media type, String tag, Categorie categorie) {
		return new ArrayList<Media>();
	}
	
	/**
	 * Recherche simple à partir d'une String
	 * @param mot
	 * @return Une liste de média
	 */
	public List<?> recherche(String mot) {
		//return session.getNamedQuery(Media.NQ_RECHERCHE).setParameter("recherche", mot).list();
		
        Criteria crit = session.createCriteria(Media.class);

        Criterion titre = Restrictions.like("titreMedia","%".concat(mot).concat("%"));
        Criterion description = Restrictions.like("descriptionMedia","%".concat(mot).concat("%"));
        
        LogicalExpression orExp = Restrictions.or(titre,description);

        crit.add(orExp);
        crit.createCriteria("tags").add(Restrictions.like("nomTag", mot));

        crit.addOrder(Order.asc("titreMedia")); // On trie correctement
        

        return crit.list(); // On execute la requete		
	}
	
	/**
	 * Récupération du top vue
	 * @return Une liste de média
	 */
	public List<?> topVue() {
		return new ArrayList<Media>();
	}
	
	/**
	 * Recuperation du top notation
	 * @return Une liste de média
	 */
	public List<?> topNotation() {
		return new ArrayList<Media>();
	}	
	
	/**
	 * Recuperation du top nouveauté
	 * @return Une liste de média
	 */
	public List<?> topNouveaute() {
		return new ArrayList<Media>();
	}		
	
	/**
	 * Recuperation du total des vues
	 * @return Une liste de média
	 */
	public long totalVues(Media media) {		
		//long param = media.getIdMedia();
		Media param = media;
		
		Query q = session.createQuery("" +
				"SELECT SUM(r.nbVues) " +
				"FROM Regarder r " +
				"WHERE r.media = :media");
		//( (Integer) session.createQuery("select count(*) from ....").iterate().next() ).intValue()
		
		q.setParameter("media", param);		
		
		return (Long) q.uniqueResult();
	}
	
	public long totalVotes(Media media) {		
		//long param = media.getIdMedia();
		Media param = media;
		
		Query q = session.createQuery("" +
				"SELECT COUNT(*) " +
				"FROM Note n " +
				"WHERE n.media = :media");
		
		q.setParameter("media", param);
		
		return (Long) q.uniqueResult();
	}
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	/**
	 * Nombre de personnes ayant aimé un média
	 * @param idMedia
	 * @return L'id d'un média
	 */
	public List<?> nbAimeMedia(long idMedia) {
		long param = idMedia;
		
		Query q = session.createQuery("" +
				"FROM Aimer " +
				"WHERE media.idMedia = :idMedia " +
				"AND aAime = true");
		
		q.setParameter("idMedia", param);		
		
		return q.list();
	}
	
	/**
	 * Nombre de personnes n'ayant pas aimé un média
	 * @param idMedia
	 * @return Liste
	 */
	public List<?> nbAimeNAimePas(long idMedia) {
		long param = idMedia;
		
		Query q = session.createQuery("" +
				"FROM Aimer " +
				"WHERE media.idMedia = :idMedia ");
		
		q.setParameter("idMedia", param);		
		
		return q.list();
	}
}
