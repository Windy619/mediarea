package dao.media;

import java.util.ArrayList;
import java.util.List;

import metier.media.Categorie;
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
	 * Recherche avanc�e
	 * @param titre
	 * @param description
	 * @param estTelechargementAutorise
	 * @param type
	 * @param tag
	 * @param categorie
	 * @return Une liste de m�dia
	 */
	public List<?> rechercheAvance(String titre, String description, boolean estTelechargementAutorise, Type_Media type, String tag, Categorie categorie) {
		return new ArrayList<Media>();
	}
	
	/**
	 * Recherche simple � partir d'une String
	 * @param mot
	 * @return Une liste de m�dia
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
	 * R�cup�ration du top vue
	 * @return Une liste de m�dia
	 */
	public List<?> topVue() {
		return new ArrayList<Media>();
	}
	
	/**
	 * Recuperation du top notation
	 * @return Une liste de m�dia
	 */
	public List<?> topNotation() {
		return new ArrayList<Media>();
	}	
	
	/**
	 * Recuperation du top nouveaut�
	 * @return Une liste de m�dia
	 */
	public List<?> topNouveaute() {
		return new ArrayList<Media>();
	}		
	
	/**
	 * Recuperation du total des vues
	 * @return Une liste de m�dia
	 */
	public List<?> totalVues(Media media) {
		return new ArrayList<Media>();
	}	
}
