package dao.media;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
				+ " LEFT JOIN  media_categorie mc ON m.idmedia =  mc.media_idmedia "
				+ " LEFT JOIN  media_tag mt ON m.idmedia = mt.media_idmedia  "
				+ " LEFT JOIN  tag ta ON mt.tags_idTag = ta.idTag"
				+ " LEFT JOIN  note n ON m.idmedia = n.media_idmedia "
				+ " LEFT JOIN  regarder r ON m.idmedia = r.media_idmedia "
				+ " LEFT JOIN  media_commentaire c ON m.idmedia = c.media_idmedia "
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
		+ " LEFT JOIN  media_categorie mc ON m.idmedia =  mc.media_idmedia "
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
