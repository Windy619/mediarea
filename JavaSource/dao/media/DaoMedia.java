package dao.media;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
	public long totalVues(Media media) {		
		//long param = media.getIdMedia();
		Media param = media;
		
		Query q = session.createQuery("" +
				"SELECT COUNT(*) " +
				"FROM Regarder r " +
				"WHERE r.media = :media");
		//( (Integer) session.createQuery("select count(*) from ....").iterate().next() ).intValue()
		
		q.setParameter("media", param);
		
		//System.out.println("Liste : " + q.list());
		if(q.list().toString().equals("[null]")) {
			return 0;
		}
		return (Long) q.uniqueResult();
	}
	
	/**
	 * Recuperation du total des t�l�chargements
	 * @return Une liste de m�dia
	 */
	public long totalTelechargement(Media media) {
		Media param = media;
		
		Query q = session.createQuery("" +
				"SELECT SUM(t.nbTelechargement) " +
				"FROM Telechargement_Media t " +
				"WHERE t.media = :media");
		
		q.setParameter("media", param);		
		
		if(q.list().toString().equals("[null]")) {
			return 0;
		}
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
	 * R�cup�ration du nombre de vues par jour TODO
	 * @return Une liste de m�dia
	 * @throws ParseException 
	 */
	public List<?> statVues(Media media) {
		Media param1 = media;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar calendar = new GregorianCalendar();
		Calendar cal = Calendar.getInstance();
		//Date dateCourante = new Date();
		//calendar.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		calendar.add(Calendar.MONTH, -3);
		Date param2 = null;
		Date param3 = null;
		try {
			param2 = (Date)formatter.parse(formatter.format(calendar.getTime())); //Date TODO
			param3 = (Date)formatter.parse(formatter.format(new Date()));
			

			//System.out.println("===> Date d�but : " + );
			//System.out.println("===> Date fin : " + );
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Query q = session.createQuery("" +
				"SELECT r.dateVues, count(r.idRegarder) " +
				"FROM Regarder r " +
				"WHERE r.media = :media " +
				"AND r.dateVues BETWEEN :dateDebut AND :dateFin " + 
				"GROUP BY r.dateVues");
		
		q.setParameter("media", param1);
		q.setParameter("dateDebut", param2);
		q.setParameter("dateFin", param3);
		
		return q.list();
	}
	
	
	
	
	
	
	

	
	
	
	
	
	
	/**
	 * Nombre de personnes ayant aim� un m�dia
	 * @param idMedia
	 * @return L'id d'un m�dia
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
	 * Nombre de personnes n'ayant pas aim� un m�dia
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
