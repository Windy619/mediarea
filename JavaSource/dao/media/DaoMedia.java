package dao.media;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import metier.media.Categorie;
import metier.media.Media;
import metier.media.Type_Media;
import metier.utilisateur.Amitie;
import metier.utilisateur.Utilisateur;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

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
	 * Recuperation du top notation générale
	 * @return Une liste de média
	 */
	public List<?> topNotation() {
		
		Query q = session.createQuery("" +
				"SELECT n.media, AVG(n.note) as moyenne, count(*) as nb " +
				"FROM Note as n  " +
				"GROUP BY n.media " +
				"ORDER BY AVG(n.note) DESC, count(*) DESC");		
		
		List<Object[]> lst = q.list();
		List<Media> res = new ArrayList<Media>();
		
		// On récupère les object Media uniquement
		for (Object[] objects : lst) {
			res.add((Media)objects[0]);
		}
		
		return res;
	}	
	
	/**
	 * Recuperation du top notation
	 * @return Une liste de média
	 */
	public List<?> topNotationVideo() {
		
		Query q = session.createQuery("" +
				"SELECT n.media, AVG(n.note) as moyenne, count(*) as nb " +
				"FROM Note as n join n.media as m join m.type as t " +
				"WHERE t.idTypeMedia = 2 " +
				"GROUP BY n.media " +
				"ORDER BY AVG(n.note) DESC, count(*) DESC");		

		List<Object[]> lst = q.list();
		List<Media> res = new ArrayList<Media>();
		
		// On récupère les object Media uniquement
		for (Object[] objects : lst) {
			res.add((Media)objects[0]);
		}
		
		return res;
	}	
	
	/**
	 * Recuperation du top notation
	 * @return Une liste de média
	 */
	public List<?> topNotationAudio() {
		
		Query q = session.createQuery("" +
				"SELECT n.media, AVG(n.note) as moyenne, count(*) as nb " +
				"FROM Note as n join n.media as m join m.type as t " +
				"WHERE t.idTypeMedia = 1 " +
				"GROUP BY n.media " +
				"ORDER BY AVG(n.note) DESC, count(*) DESC");		

		List<Object[]> lst = q.list();
		List<Media> res = new ArrayList<Media>();
		
		// On récupère les object Media uniquement
		for (Object[] objects : lst) {
			res.add((Media)objects[0]);
		}
		
		return res;
	}		
	
	/**
	 * Recuperation du top nouveauté
	 * @return Une liste de média
	 */
	public List<?> topNouveaute() {
		return new ArrayList<Media>();
	}	
	
	/**
	 * Liste des nouvelles vidéos
	 * @return
	 */
	public List<?> nouvellesVideos() {
		
		Query q = session.createQuery("" +
				"FROM Media as m " +
				"WHERE m.type = 2 " +
				"ORDER BY m.datePublication DESC");		

		return q.list();
	}
	
	/**
	 * Liste des nouveaux Sons
	 * @return
	 */
	public List<?> nouveauxSons() {
		
		Query q = session.createQuery("" +
				"FROM Media as m " +
				"WHERE m.type = 1 " +
				"ORDER BY m.datePublication DESC");		

		return q.list();
	}
	
	/**
	 * Recommendation de vidéos pour un utilisateur
	 * @return
	 */
	public List<?> recommendationVideos(Utilisateur u) {
		
		// Chargement des recommendations de l'utilisateur u
		List<?> res = new ArrayList<Media>();
		
		// On ajoute tous les médias crées, par des amis
		Query q1 = session.createQuery("" +
				"SELECT mediasAmi " +
				"FROM Utilisateur u join u.amis as amis join amis.ami.medias as mediasAmi " +
				"WHERE u.idUtilisateur = :idConnecte " +
				" AND mediasAmi.type = 2");		
		q1.setParameter("idConnecte", u.getIdUtilisateur());
		
		res.addAll(q1.list());
		
		// Chargement des medias vu par des amis ( plus de 10 fois )
		Query q2 = session.createQuery("" +
				"SELECT r.media " +
				"FROM Utilisateur u join u.amis as amis join amis.ami.regardeMedias as r " +
				"WHERE u.idUtilisateur = :idConnecte " +
				" AND r.nbVues > 10 " +
				" AND r.media.type = 2");		
		q2.setParameter("idConnecte", u.getIdUtilisateur());
		
		res.addAll(q2.list());
		
		// Chargement des medias présents dans la playlist d'amis
		Query q3 = session.createQuery("" +
				"SELECT playlistAmi " +
				"FROM Utilisateur u join u.amis as amis join amis.ami.playlists as playlistAmi " +
				"WHERE u.idUtilisateur = :idConnecte " +
				"	AND playlistAmi.type = 2");		
		q3.setParameter("idConnecte", u.getIdUtilisateur());
		
		res.addAll(q3.list());	
		
		// Retour des résultats
		return res;
	}	
	
	/**
	 * Recommendation de sons pour un utilisateur
	 * @return
	 */
	public List<?> recommendationAudios(Utilisateur u) {
		
		// Chargement des recommendations de l'utilisateur u
		List<?> res = new ArrayList<Media>();
		
		// On ajoute tous les médias crées, par des amis
		Query q1 = session.createQuery("" +
				"SELECT mediasAmi " +
				"FROM Utilisateur u join u.amis as amis join amis.ami.medias as mediasAmi " +
				"WHERE u.idUtilisateur = :idConnecte " +
				" AND mediasAmi.type = 1");		
		q1.setParameter("idConnecte", u.getIdUtilisateur());
		
		res.addAll(q1.list());
		
		// Chargement des medias vu par des amis ( plus de 10 fois )
		Query q2 = session.createQuery("" +
				"SELECT r.media " +
				"FROM Utilisateur u join u.amis as amis join amis.ami.regardeMedias as r " +
				"WHERE u.idUtilisateur = :idConnecte " +
				" AND r.nbVues > 10 " +
				" AND r.media.type = 1");		
		q2.setParameter("idConnecte", u.getIdUtilisateur());
		
		res.addAll(q2.list());
		
		// Chargement des medias présents dans la playlist d'amis
		Query q3 = session.createQuery("" +
				"SELECT playlistAmi " +
				"FROM Utilisateur u join u.amis as amis join amis.ami.playlists as playlistAmi " +
				"WHERE u.idUtilisateur = :idConnecte " +
				"	AND playlistAmi.type = 1");		
		q3.setParameter("idConnecte", u.getIdUtilisateur());
		
		res.addAll(q3.list());	
		
		// Retour des résultats
		return res;
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
