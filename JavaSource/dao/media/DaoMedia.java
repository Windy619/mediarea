package dao.media;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import metier.media.Commentaire;
import metier.media.Media;
import metier.media.Type_Media;
import metier.utilisateur.Utilisateur;

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
	 * @param value
	 * @param description
	 * @param estTelechargementAutorise
	 * @param type
	 * @param tag
	 * @param categorie
	 * @return Une liste de média
	 */
	public List<?> rechercheAvance(String value, String titre, String description, boolean estTelechargementAutorise, Type_Media type, String tag, Long categorie, String typeRecherche) {
		// A FAIRE
		Query q = session.createQuery("" +
				"FROM Media as m " +
				"WHERE (a.ami.idUtilisateur IN (SELECT a.ami.idUtilisateur FROM Amitie as a WHERE a.utilisateur.idUtilisateur = :idConnecte) " +
				"	OR a.utilisateur.idUtilisateur IN (SELECT a.ami.idUtilisateur FROM Amitie as a WHERE a.utilisateur.idUtilisateur = :idConnecte) " +
				"	OR a.utilisateur.idUtilisateur NOT IN (SELECT a.ami FROM Amitie as a WHERE a.utilisateur.idUtilisateur = :idConnecte)) " +
				"	AND a.utilisateur.idUtilisateur <> :idConnecte " +
				"");
		q.setParameter("idConnecte", "bla");	
		
		return q.list();
	}
	
	/**
	 * Recherche simple à partir d'une String
	 * @param mot
	 * @return Une liste de média
	 */
	public HashMap<String, Object> recherche(String mot, String typeMedia, Boolean seulementTelechargeable, String tri, String tri_ordre, String page, String nb_par_page, String titre, String description, String auteur, String tag, Long categorie, String typeRecherche) {
		//return session.getNamedQuery(Media.NQ_RECHERCHE).setParameter("recherche", mot).list();
		/*
        Criteria crit = session.createCriteria(Media.class);

        Criterion titre = Restrictions.like("titreMedia","%".concat(mot).concat("%"));
        Criterion description = Restrictions.like("descriptionMedia","%".concat(mot).concat("%"));
        
        LogicalExpression orExp = Restrictions.or(titre,description);

        crit.add(orExp);
        crit.createCriteria("tags").add(Restrictions.like("nomTag", mot));

        crit.addOrder(Order.asc("titreMedia")); // On trie correctement
        

        return crit.list(); // On execute la requete
        //*/	
		/*
		Query q = session.createQuery("" +
				"FROM Media as m " +
				"WHERE m.titreMedia LIKE :rech_value" +
				"");
		q.setParameter("rech_value", mot);
		//*/
		///*
		//*/
		//*
		String clauseWhere = "";
		System.out.println("Creation where");
		if(typeRecherche != null && typeRecherche.equals("2")) {
			System.out.println("Avancee");
			if(titre == null)
				titre = "";
			else
				titre = titre.toUpperCase();
			clauseWhere += " WHERE (UPPER(m.titreMedia) LIKE '%"+titre.toUpperCase()+"%' ";
			if(description != null && !description.equals("")) 
				clauseWhere += "AND UPPER(m.descriptionMedia) LIKE '%"+description.toUpperCase()+"%' ";
			if(auteur != null && !auteur.equals("")) 
				clauseWhere += "AND UPPER(ut.pseudo) LIKE '%"+auteur.toUpperCase()+"%' ";
			if(tag != null && !tag.equals("")) 
				clauseWhere += "AND UPPER(ta.nomTag) LIKE '%"+tag+"%' ";
			if(categorie != null && categorie > 0)
				clauseWhere += "AND mc.categories_idCategorie = " + categorie;
			clauseWhere += " )";
			System.out.println(clauseWhere);
			/*
			clauseWhere +=  " WHERE (UPPER(m.titreMedia) LIKE '%"+titre+"%'" 
					+ " AND UPPER(m.descriptionMedia) LIKE '%"+description+"%' "
					+ " AND UPPER(ut.pseudo) LIKE '%"+auteur+"%' "
					+ " AND UPPER(ta.nomTag) LIKE '%"+tag+"%' ";
			System.out.print(clauseWhere);
			*/
		}
		else {
			if(mot == null) 
				mot = "";
			else 
				mot = mot.toUpperCase();
			clauseWhere +=  " WHERE (UPPER(m.titreMedia) LIKE '%"+mot+"%'" 
							+ " OR UPPER(m.descriptionMedia) LIKE '%"+mot+"%') ";
			
			titre = mot;
			description = mot;
		}
		if(seulementTelechargeable != null && seulementTelechargeable == true)
			clauseWhere += "AND m.estTelechargementAutorise = '1' ";
		if(typeMedia != null && (typeMedia.equals("2") || typeMedia.equals("3"))) {
			String typeM = "son";
			if(typeMedia.equals("2"))
				typeM = "video";
			clauseWhere += " AND t.nomTypeMedia = '"+ typeM +"'";
		}
		
		HashMap<String, Object> resultat = new HashMap<String, Object>();
		
		int nbMedias = 0;
		HashMap<Long, Integer> l_nbComms = new HashMap<Long, Integer>();
		HashMap<Long, Integer> l_nbVues = new HashMap<Long, Integer>();
		
		Query query_count = session.createSQLQuery(
				"SELECT COUNT(DISTINCT m.idMedia) AS NB_MEDIA"
				+ " FROM Media m "
				+ " LEFT JOIN  utilisateur ut ON m.auteurMedia_idUtilisateur = ut.idUtilisateur "
				//+ " LEFT JOIN  media_categorie mc ON m.idmedia =  mc.media_idmedia "
				+ " LEFT JOIN  media_categorie_media mc ON m.idmedia =  mc.media_idmedia "
				+ " LEFT JOIN  media_tag mt ON m.idmedia = mt.media_idmedia  "
				+ " LEFT JOIN  tag ta ON mt.tags_idTag = ta.idTag"
				+ " LEFT JOIN  note n ON m.idmedia = n.media_idmedia "
				+ " LEFT JOIN  regarder r ON m.idmedia = r.media_idmedia "
				//+ " LEFT JOIN  media_commentaire c ON m.idmedia = c.media_idmedia " ??
				+ " LEFT JOIN  type_media t ON m.type_idTypeMedia = t.idTypeMedia "
				+ clauseWhere
				);
		List result_count = query_count.list();
		Iterator it_count= result_count.iterator();
		Object o_count = (Object) it_count.next();
		nbMedias = Integer.parseInt(o_count.toString());
		
		String typeOrder = " DESC";
		if(tri_ordre != null && tri_ordre.equals("1"))
			typeOrder += " ASC";
				
		String clauseOrder = " ORDER BY ";
		String orderChamps = "NB_TITRE"+typeOrder+", NB_DESCRIPTION"+typeOrder+", VUES_NB"+typeOrder+", m.datePublication"+typeOrder+", NOTE_AVG"+typeOrder+"";
		if(tri != null && !tri.equals("1")) {
			if(tri.equals("2"))
				orderChamps = "VUES_NB"+typeOrder+", NB_TITRE"+typeOrder+", NB_DESCRIPTION"+typeOrder+", m.datePublication"+typeOrder+", NOTE_AVG"+typeOrder+"";
			else if(tri.equals("3"))
				orderChamps = "m.datePublication"+typeOrder+", NB_TITRE"+typeOrder+", NB_DESCRIPTION, VUES_NB, NOTE_AVG";
			else if(tri.equals("4"))
				orderChamps = "NOTE_AVG"+typeOrder+", NB_TITRE"+typeOrder+", NB_DESCRIPTION"+typeOrder+", VUES_NB "+typeOrder+", m.datePublication"+typeOrder+"";
		}
		clauseOrder += orderChamps;
		
		
		int pageI = 1;
		int nbParPageI = 15;
		if(page != null && !page.equals("")) {
			try {
				pageI = Integer.parseInt(page);
			}
			catch(Exception e) {}
		}
		if(nb_par_page != null && !nb_par_page.equals("")) {
			try {
				nbParPageI = Integer.parseInt(nb_par_page);
			}
			catch(Exception e) {}
		}
		int depart = (pageI - 1) * nbParPageI;
		String clauseLimit = " LIMIT " + depart + ", " + nbParPageI;
	
		
		Query query = session.createSQLQuery(
		"SELECT m.idmedia, COUNT(mt.media_idmedia) AS TAG_NB, COUNT(mc.media_idmedia) AS CAT_NB, AVG(n.note) AS NOTE_AVG, SUM(n.note) AS NOTE_NB, SUM(r.nbVues) AS VUES_NB, COUNT(c.media_idmedia) AS COMM_NB, (LENGTH(m.titreMedia) - LENGTH(REPLACE(UPPER(m.titreMedia), '"+titre+"', '')) + 1) AS NB_TITRE, (LENGTH(m.titreMedia) - LENGTH(REPLACE(UPPER(m.descriptionMedia), '"+description+"', '')) + 1) AS NB_DESCRIPTION "
		+ " FROM Media m "
		+ " LEFT JOIN  utilisateur ut ON m.auteurMedia_idUtilisateur = ut.idUtilisateur "
		+ " LEFT JOIN  media_categorie_media mc ON m.idmedia =  mc.media_idmedia "
		+ " LEFT JOIN  media_tag mt ON m.idmedia = mt.media_idmedia  "
		+ " LEFT JOIN  tag ta ON mt.tags_idTag = ta.idTag"
		+ " LEFT JOIN  note n ON m.idmedia = n.media_idmedia "
		+ " LEFT JOIN  regarder r ON m.idmedia = r.media_idmedia "
		+ " LEFT JOIN  media_commentaire c ON m.idmedia = c.media_idmedia "
		+ " LEFT JOIN  type_media t ON m.type_idTypeMedia = t.idTypeMedia "
		+ clauseWhere
		+ " GROUP BY m.idmedia" +
		clauseOrder + clauseLimit);
		//*/
		/*
		Query query = session.createSQLQuery(
				"SELECT * "
				+ " FROM Media m ").addEntity(Media.class);
		//*/
		List result = query.list();
		String listids = "";
		Iterator it= result.iterator();
		while (it.hasNext()) // tant que j'ai un element non parcouru
		{
		  Object[] o = (Object[]) it.next();
		  if(!listids.equals(""))
			 listids += ", ";
		  listids += o[0].toString();
		  
		  if(o[5] != null && !o[5].toString().equals(""))
			  l_nbVues.put(Long.parseLong(o[0].toString()), Integer.parseInt(o[5].toString()));
		  else
			  l_nbVues.put(Long.parseLong(o[0].toString()), 0);
		  
		  if(o[6] != null && !o[6].toString().equals(""))
			  l_nbComms.put(Long.parseLong(o[0].toString()), Integer.parseInt(o[6].toString()));
		  else
			  l_nbComms.put(Long.parseLong(o[0].toString()), 0);
			  
		  //mes opérations
		}
		if(nbMedias > 0 && listids != null && !listids.equals("")) {
			Query q = session.createQuery("" +
					" FROM Media as m " +
					"WHERE m.idMedia IN (" + listids +")"
					+ " ORDER BY FIELD(m.idMedia, " + listids + ")");
			
			resultat.put("nbMedias", nbMedias);
			resultat.put("l_nbComs", l_nbComms);
			resultat.put("l_nbVues", l_nbVues);
			resultat.put("l_medias", q.list());
		}
		else {
			resultat.put("nbMedias", nbMedias);
			resultat.put("l_nbComs", null);
			resultat.put("l_nbVues", null);
			resultat.put("l_medias", null);			
		}
		return resultat;
	}
	
	public Media rechercheSurID(Long recherche) {
		Long param = recherche;
		
		Query q = session.createQuery("" +
				"FROM Media " +
				"WHERE idMedia = :recherche");
		q.setParameter("recherche", param);		
		
		return (Media) q.uniqueResult();
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
	 * Recommendations de médias suivant le média visualisé
	 * @param Média
	 * @return Liste de médias
	 */
	public List<?> recommendationMediasSuivantMediaVisualise(Media media) {
		String param = media.getTitreMedia();
		
		Query q = session.createQuery("" +
				"FROM Media " +
				"WHERE titreMedia LIKE :titreMedia " + //artiste XXX
				"AND visibilite.idVisibilite = 1"); //média ayant une visibilité "Public" 
		
		q.setParameter("titreMedia", param + "%"); //like	
		
		return q.list();
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
		
		//Si la requête ne retourne rien
		if(q.list().toString().equals("[null]")) {
			return 0;
		}
		
		//Retour d'un nombre
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
				"WHERE media.idMedia = :idMedia " +
				"AND aAime = false");
		
		q.setParameter("idMedia", param);		
		
		return q.list();
	}
	

	/**
	 * Récupération du nombre de vues par jour
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
		//Retour d'un query car plusieurs champs dans SELECT
		return q; 
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
	
	/*public List<?> getCommentaires(Media media) {
		Query q = session.createQuery("" +
				"SELECT m.commentaires " +
				"FROM Media as m join Commentaire as c " +
				"WHERE m.media = :media AND m.aCommentairesOuverts = true " +
				"ORDER BY c.dateCommentaire DESC" +
				"");
		q.setParameter("media", media);
		
		return q.list();
	}*/
	
	public List<Commentaire> getCommentaires(Media media) {
		/*Query query = session.createSQLQuery("" +
				"SELECT c.idCommentaire, c.contenuCommentaire, c.dateCommentaire, c.nbVotes, c.auteur_idUtilisateur " +
				"FROM media_commentaire mc LEFT JOIN commentaire c " +
				"ON mc.commentaires_idCommentaire = c.idCommentaire " +
				"LEFT JOIN media m " +
				"ON mc.Media_idMedia = m.idMedia " +
				"LEFT JOIN signalement_commentaire sc " +
				"ON mc.commentaires_idCommentaire = sc.commentaire_idCommentaire " +
				"WHERE mc.Media_idMedia = :media " +
				"AND m.aCommentairesOuverts = true " +
				"AND c.idCommentaire NOT IN (SELECT commentaire_idCommentaire " +
				                            "FROM signalement_commentaire) " +
				"ORDER BY c.dateCommentaire DESC " +
				""); //requête SQL (pour faire un join)*/
		
		Query query = session.createSQLQuery("" +
				"SELECT c.idCommentaire, c.contenuCommentaire, c.dateCommentaire, c.nbVotes, c.auteur_idUtilisateur " +
				"FROM media_commentaire mc LEFT JOIN commentaire c " +
				"ON mc.commentaires_idCommentaire = c.idCommentaire " +
				"LEFT JOIN media m " +
				"ON mc.Media_idMedia = m.idMedia " +
				"LEFT JOIN signalement_commentaire sc " +
				"ON mc.commentaires_idCommentaire = sc.commentaire_idCommentaire " +
				"WHERE mc.Media_idMedia = :media " +
				"AND m.aCommentairesOuverts = true " +
				"ORDER BY c.dateCommentaire DESC " +
				""); //requête SQL (pour faire un join)

		query.setParameter("media", media);
				
		List result = query.list();
		if(! result.toString().equals("[]"))
		{
			String listids = "";
			Iterator it= result.iterator();
			while (it.hasNext()) // tant que l'on a un élément non parcouru
			{
				Object[] o = (Object[]) it.next();
	            if(!listids.equals(""))
	                   listids += ", ";
	            listids += o[0].toString();
			}
			
			Query q = session.createQuery("" +
					"FROM Commentaire as c " +
					"WHERE c.idCommentaire IN (" + listids +") " + //identifiants des commentaires récupérés avec la requête SQL
					"ORDER BY c.dateCommentaire DESC");
			  
			return q.list();
		}
		else
		{
			return null;
		}
	}
	
	public Query getReponses(Media media) {
		
		Query q = session.createQuery("" +
			"SELECT pere as PERE, fils as FILS, fils.dateCommentaire " +
			"FROM Media as media JOIN media.commentaires as pere JOIN pere.commentairesFils as fils " +
			"WHERE media.idMedia = :idMedia " +
			"ORDER BY fils.dateCommentaire"); //et non signalé XXX
		
		q.setParameter("idMedia", media.getIdMedia());

		return q;
	}
}
