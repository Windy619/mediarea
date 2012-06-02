package dao.media;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import metier.media.Categorie;
import metier.media.Commentaire;
import metier.media.Media;
import metier.media.Type_Media;
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
	 * Recuperation du total des téléchargements
	 * @return Une liste de média
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
	
	/**
	 * Recuperation du total des vues
	 * @return Une liste de média
	 */
	public double moyenneVotes(Media media) {
		Media param = media;
		
		Query q = session.createQuery("" +
				"SELECT AVG(note) " +
				"FROM Note " +
				"WHERE media = :media");
		
		q.setParameter("media", param);
		
		if(q.list().toString().equals("[null]")) {
			return 0;
		}
		return (Double) q.uniqueResult();
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
	
	/*public long totalFavoriesMedia(Media media) {
		Media param = media;
		
		Query q = session.createQuery("" +
				"SELECT COUNT(*) " +
				"FROM Playlist p join Type_Playlist tp " +
				//"WHERE n.media = :media");
		
		q.setParameter("media", param);
		
		return (Long) q.uniqueResult();
	}*/
	
	
	
	
	
	
	
	
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
	

	/**
	 * Récupération du nombre de vues par jour TODO
	 * @return Une liste de média
	 * @throws ParseException 
	 */
	//public List<?> statVues(Media media) {
	public Query statVues(Media media) {
		Media param1 = media;
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.add(Calendar.MONTH, -3);
		Date param2 = null;
		Date param3 = null;
			
		param2 = calendar.getTime();
		param3 = new Date();
		
		
		Query q = session.createQuery("" +
				"SELECT DATE_FORMAT(r.dateVues,'%d-%m-%Y'), count(r.idRegarder) " +
				"FROM Regarder r " +
				"WHERE r.media = :media " +
				"AND r.dateVues BETWEEN :dateDebut AND :dateFin " + 
				"GROUP BY DATE_FORMAT(r.dateVues,'%d-%m-%Y')");
		
		q.setParameter("media", param1);
		q.setParameter("dateDebut", param2);
		q.setParameter("dateFin", param3);
		
		//return q.list();
		return q; //retourne un query car plusieurs champs dans SELECT
	}
	
	public long maxNbRegarder(Media media) {
		Media param = media;
		
		Query q = session.createQuery("" +
				"SELECT count(r.idRegarder) " +
				"FROM Regarder r " +
				"WHERE r.media = :media " +
				"GROUP BY DATE_FORMAT(r.dateVues,'%Y-%m-%d') ORDER BY count(r.idRegarder)");
		
		q.setParameter("media", param);
		q.setMaxResults(1); //il n'y a pas de mot-clé LIMIT en HQL
		
		return (Long) q.uniqueResult();
	}
	
	/*public List<?> getCommentaires(Media media) { //à utiliser
		Query q = session.createQuery("" +
				"SELECT m.commentaires " +
				"FROM Media as m join Commentaire as c " + //join à voir TODO
				"WHERE m.media = :media AND m.aCommentairesOuverts = true " +
				"ORDER BY c.dateCommentaire DESC" +
				"");
		q.setParameter("media", media);
		
		return q.list();
	}*/
	
	public List<Commentaire> getCommentaires(Media media) {
		Query query = session.createSQLQuery("" +
				"SELECT c.idCommentaire, c.contenuCommentaire, c.dateCommentaire, c.nbVotes, c.auteur_idUtilisateur " +
				"FROM media_commentaire mc LEFT JOIN commentaire c " +
				"ON mc.commentaires_idCommentaire = c.idCommentaire " +
				"LEFT JOIN media m " +
				"ON mc.Media_idMedia = m.idMedia " +
				"AND m.aCommentairesOuverts = true " +
				"WHERE mc.Media_idMedia = :media " +
				"ORDER BY c.dateCommentaire DESC" +
				""); //requête SQL pour faire un join

		query.setParameter("media", media);
		
		List result = query.list();
		String listids = "";
		Iterator it= result.iterator();
		while (it.hasNext()) // tant que j'ai un element non parcouru
		{
			Object[] o = (Object[]) it.next();
            if(!listids.equals(""))
                   listids += ", ";
            listids += o[0].toString();
		}
		Query q = session.createQuery("" +
				"FROM Commentaire as c " +
				"WHERE c.idCommentaire IN (" + listids +") " + 
				"ORDER BY c.dateCommentaire DESC");
		  
		return q.list();
	}
	
	public Query getReponses(Media media) {
		
		Query q = session.createQuery("" +
			"SELECT pere as PERE, fils as FILS, fils.dateCommentaire " +
			"FROM Media as media JOIN media.commentaires as pere JOIN pere.commentairesFils as fils " +
			"WHERE media.idMedia = :idMedia " +
			"ORDER BY fils.dateCommentaire");
		
		q.setParameter("idMedia", media.getIdMedia());

		return q;
	}
}
