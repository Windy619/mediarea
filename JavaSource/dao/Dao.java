package dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;



/**
 * <h1> Classe g�n�rique DAO </h1>
 * <br />
 * Classe proposant des fonctions de base d'ajout/suppression/modification des donn�es
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
     * Ajoute un Object en base de donn�es
     * @param o un Object
     * @return Un int sp�cifiant le nombre de ligne modifi�es
     */
    public int ajouter(T o) {
        int resultat = -1;

        try {
            if (o != null) { // V�rification
                hibernate.demarrerTransaction(); // On d�marre une transaction
                resultat = hibernate.save(o); // On ajoute l'objet
                hibernate.commitTransaction(); // On Commit la transaction
            }
        } catch(Exception e) {
        	hibernate.rollBackTransaction(); // on rollBack en cas de probl�me
        }

        return resultat;
    }

    /**
     * Ajoute une Set d'Object en base de donn�es
     * @param o une Set d'Object
     * @return Un int sp�cifiant le nombre de ligne modifi�es
     */
    public int ajouterListe(ArrayList<T> liste) {
        int resultat = 0;

        try {
            if (liste != null) {
            	hibernate.demarrerTransaction(); // On d�marre une transaction

                Iterator<T> it = liste.iterator();
                while(it.hasNext()) {
                    resultat += hibernate.save(it.next()); // On sauvegarde l'objet
                }

                hibernate.commitTransaction(); // On Commit la transaction
            }
        } catch(Exception e) {
        	hibernate.rollBackTransaction(); // on rollBack en cas de probl�me
        }

        return resultat;
    }

    /**
     * Ajoute un Object en base de donn�es
     * @param o un Object
     * @return Un int sp�cifiant le nombre de ligne modifi�es
     */
    public int modifier(T o) {
        int resultat = -1;

        try {
            if (o != null) { // V�rification
            	hibernate.demarrerTransaction(); // On d�marre une transaction
                resultat = hibernate.save(o); // On ajoute l'objet
                hibernate.commitTransaction(); // On Commit la transaction
            }
        } catch(Exception e) {
        	hibernate.rollBackTransaction(); // on rollBack en cas de probl�me
        }

        return resultat;
    }

    /**
     * Ajoute ou modifie une Set d'Object en base de donn�es
     * @param o une Set d'Object
     * @return Un int sp�cifiant le nombre de ligne modifi�es
     */
    public int modifierListe(ArrayList<T> liste) {
        int resultat = 0;

        try {
            if (liste != null) {
            	hibernate.demarrerTransaction(); // On d�marre une transaction

                Iterator<T> it = liste.iterator();
                while(it.hasNext()) {
                    resultat += hibernate.save(it.next()); // On sauvegarde l'objet
                }

                hibernate.commitTransaction(); // On Commit la transaction
            }
        } catch(Exception e) {
        	hibernate.rollBackTransaction(); // on rollBack en cas de probl�me
        }

        return resultat;
    }

    /**
     * Supprime un Object en base de donn�es
     * @param o un Object
     * @return Un int sp�cifiant le nombre de ligne modifi�es
     */
    public int supprimer(T o) {
        int resultat = -1;

        try {
            if (o != null) { // V�rification
            	hibernate.demarrerTransaction(); // On d�marre une transaction
                resultat = hibernate.delete(o); // On supprime l'objet
                hibernate.commitTransaction(); // On Commit la transaction
            }
        } catch(Exception e) {
        	hibernate.rollBackTransaction(); // on rollBack en cas de probl�me
        }

        return resultat;
    }

    /**
     * Supprime une Set d'Object en base de donn�es
     * @param o une Set d'Object
     * @return Un int sp�cifiant le nombre de ligne modifi�es
     */
    public int supprimerListe(ArrayList<T> liste) {

        int resultat = 0;

        try {
            if (liste != null) {
            	hibernate.demarrerTransaction(); // On d�marre une transaction

                Iterator<T> it = liste.iterator();
                while(it.hasNext()) {
                    resultat += hibernate.delete(it.next()); // Suppression de l'objet
                }

                hibernate.commitTransaction(); // On Commit la transaction
            }
        } catch(Exception e) {
        	hibernate.rollBackTransaction(); // on rollBack en cas de probl�me
        }

        return resultat;
    }	
	

	/**
     * R�cup�re tous les enregistrements d'une table
     * @param classe Nom de la classe
     * @return Une ArrayList de Object contenant toutes les donn�es
     */
    @SuppressWarnings("unchecked")
	public List<T> getTous() { 	
        Criteria crit = session.createCriteria(classe);
        return crit.list(); // On ex�cute
    }    

    /**
     * R�cup�re un enregistrement d'une table
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
