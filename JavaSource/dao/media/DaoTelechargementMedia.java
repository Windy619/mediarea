package dao.media;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import metier.media.Telechargement_Media;
import dao.Dao;

/**
 * Dao de la classe Téléchargement Média
 * @author Florence
 *
 */
public class DaoTelechargementMedia extends Dao<Telechargement_Media> {
	/**
	 * Constructeur
	 */
	public DaoTelechargementMedia() {
		super(Telechargement_Media.class);
	}
	
	public Telechargement_Media getByIdmedia(long idMedia) {
		long param = idMedia;
		Telechargement_Media resultat = null;
		Query q = session.createSQLQuery("" +
				"SELECT idTelechargementMedia " +
				"FROM TELECHARGEMENT_MEDIA " +
				"WHERE media_idMedia = :idMedia");
		q.setParameter("idMedia", param);
		List result = q.list();
		if(!result.toString().equals("[]"))
		{
			Iterator it= result.iterator();
			while (it.hasNext()) // tant que l'on a un élément non parcouru
			{
				Object o = (Object) it.next();
				int i = Integer.parseInt(o.toString());
				resultat = super.getUn(i);
			}
		}		
		
		return resultat;
	}
}
