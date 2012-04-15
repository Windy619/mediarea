package dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;



/**
 * <h1> Classe générique DAO </h1>
 * <br />
 * Classe proposant des fonctions de base d'ajout/suppression/modification des données
 * @author marzolf
 *
 * @param <T>
 */
public abstract class Dao<T> {
	
	/**
	 * Objet Hibernate
	 */
	protected Hibernate hibernate;
	
	/**
	 * Session
	 */
	protected Session session;
	
	/**
	 * Classe
	 */
	protected Class<T> classe;
	
	/**
	 * Constructeur vide
	 */
	public Dao(Class<T> classe) {
		hibernate = Hibernate.getInstance();
		session = hibernate.getSession();
		this.classe = classe;
	}

	
    /**
     * Ajoute un Object en base de données
     * @param o un Object
     * @return Un int spécifiant le nombre de ligne modifiées
     */
    public int ajouter(T o) {
        int resultat = -1;

        try {
            if (o != null) { // Vérification
                hibernate.demarrerTransaction(); // On démarre une transaction
                resultat = hibernate.save(o); // On ajoute l'objet
                hibernate.commitTransaction(); // On Commit la transaction
            }
        } catch(Exception e) {
        	hibernate.rollBackTransaction(); // on rollBack en cas de problème
        }

        return resultat;
    }

    /**
     * Ajoute une Set d'Object en base de données
     * @param o une Set d'Object
     * @return Un int spécifiant le nombre de ligne modifiées
     */
    public int ajouterListe(ArrayList<T> liste) {
        int resultat = 0;

        try {
            if (liste != null) {
            	hibernate.demarrerTransaction(); // On démarre une transaction

                Iterator<T> it = liste.iterator();
                while(it.hasNext()) {
                    resultat += hibernate.save(it.next()); // On sauvegarde l'objet
                }

                hibernate.commitTransaction(); // On Commit la transaction
            }
        } catch(Exception e) {
        	hibernate.rollBackTransaction(); // on rollBack en cas de problème
        }

        return resultat;
    }

    /**
     * Ajoute un Object en base de données
     * @param o un Object
     * @return Un int spécifiant le nombre de ligne modifiées
     */
    public int modifier(T o) {
        int resultat = -1;

        try {
            if (o != null) { // Vérification
            	hibernate.demarrerTransaction(); // On démarre une transaction
                resultat = hibernate.save(o); // On ajoute l'objet
                hibernate.commitTransaction(); // On Commit la transaction
            }
        } catch(Exception e) {
        	hibernate.rollBackTransaction(); // on rollBack en cas de problème
        }

        return resultat;
    }

    /**
     * Ajoute ou modifie une Set d'Object en base de données
     * @param o une Set d'Object
     * @return Un int spécifiant le nombre de ligne modifiées
     */
    public int modifierListe(ArrayList<T> liste) {
        int resultat = 0;

        try {
            if (liste != null) {
            	hibernate.demarrerTransaction(); // On démarre une transaction

                Iterator<T> it = liste.iterator();
                while(it.hasNext()) {
                    resultat += hibernate.save(it.next()); // On sauvegarde l'objet
                }

                hibernate.commitTransaction(); // On Commit la transaction
            }
        } catch(Exception e) {
        	hibernate.rollBackTransaction(); // on rollBack en cas de problème
        }

        return resultat;
    }

    /**
     * Supprime un Object en base de données
     * @param o un Object
     * @return Un int spécifiant le nombre de ligne modifiées
     */
    public int supprimer(T o) {
        int resultat = -1;

        try {
            if (o != null) { // Vérification
            	hibernate.demarrerTransaction(); // On démarre une transaction
                resultat = hibernate.delete(o); // On supprime l'objet
                hibernate.commitTransaction(); // On Commit la transaction
            }
        } catch(Exception e) {
        	hibernate.rollBackTransaction(); // on rollBack en cas de problème
        }

        return resultat;
    }

    /**
     * Supprime une Set d'Object en base de données
     * @param o une Set d'Object
     * @return Un int spécifiant le nombre de ligne modifiées
     */
    public int supprimerListe(ArrayList<T> liste) {

        int resultat = 0;

        try {
            if (liste != null) {
            	hibernate.demarrerTransaction(); // On démarre une transaction

                Iterator<T> it = liste.iterator();
                while(it.hasNext()) {
                    resultat += hibernate.delete(it.next()); // Suppression de l'objet
                }

                hibernate.commitTransaction(); // On Commit la transaction
            }
        } catch(Exception e) {
        	hibernate.rollBackTransaction(); // on rollBack en cas de problème
        }

        return resultat;
    }	
	

	/**
     * Récupère tous les enregistrements d'une table
     * @param classe Nom de la classe
     * @return Une ArrayList de Object contenant toutes les données
     */
    @SuppressWarnings("unchecked")
	public List<T> getTous() { 	
        Criteria crit = session.createCriteria(classe);
        return crit.list(); // On exécute
    }    

    /**
     * Récupère un enregistrement d'une table
     * @param classe Nom de la classe
     * @param id Identifiant de l'objet
     * @return Un Object
     */
    public T getUn(long id) {
        Criteria crit = session.createCriteria(classe);
        crit.setMaxResults(1);
        crit.add(Restrictions.idEq(id));
        
		@SuppressWarnings("unchecked")
		List<T> liste = crit.list();

        T resultat = null;

        if (!liste.isEmpty()) {
            resultat = liste.iterator().next();
        }
        
        return resultat;
    }    
       




}
