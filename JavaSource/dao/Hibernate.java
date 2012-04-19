package dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;

/**
 * <h1>Classe Hibernate</h1>
 * Cette classe permet d'initialiser le Framework Hibernate <br />
 * <b> Cette classe ne peut �tre instanci�e qu'une seule fois </b>
 * @author marzolf
 */
public class Hibernate {

	/** 
	 * Instance 
	 */
	public static Hibernate instance;
	
	/** 
	 * Usine � Session 
	 */
	private SessionFactory sessionFactory;
	
	/**
	 * Session Hibernate
	 */
	private Session session;
	
	/**
	 * Constructeur priv�
	 */
	private Hibernate() {
		// Configuration de la factory
        sessionFactory = new Configuration()
        .configure() // configuration des donn�es depuis le fichier hibernate.cfg
        .buildSessionFactory();		
        
        ouvrirSession(); // On ouvre une session
    }
	
	/**
	 * Destructeur de la classe
	 */
	protected void finalize() throws Throwable {
		fermerSession(); // On ferme la session avant de supprimer l'objet
    } 	
	
	
    /**
     * Ouvre la session
     * @return un booleen indiquant si l'op�ration a fonctionn�
     */
    private boolean ouvrirSession() {
        boolean resultat = true;
        try {
            session = sessionFactory.openSession(); // On ouvre la session
        } catch (Exception e) {
            System.err.println("Erreur dans la classe Hibernate");
            e.printStackTrace();
            //TODO REDIGER EXCEPTION
        }

        return resultat;
    }

    /**
     * Ferme la session
     * @return un booleen indiquant si l'op�ration a fonctionn�
     */
    private boolean fermerSession() {
        boolean resultat = true;
        try {
            if (session.isOpen()) {
                session.close(); // On ferme la session
            } else {
                resultat = false;
            }
        } catch (Exception e) {
            System.err.println("Erreur dans la classe Hibernate");
            e.printStackTrace();
            //TODO REDIGER EXCEPTION
        }

        return resultat;
    }	
    
    /**
     * Demarrer la transaction
     * @return un booleen indiquant si l'op�ration a fonctionn�
     */
    public boolean demarrerTransaction() {
        boolean resultat = true;
        try {
            if (session.isOpen()) {
                session.beginTransaction(); // On d�bute la transaction
            } else {
                resultat = false;
            }
        } catch (Exception e) {
            System.err.println("Erreur dans la classe Hibernate");
            e.printStackTrace();
        }

        return resultat;
    }

    /**
     * Annulation d'une transaction
     * @return Un booleen indiquant si l'op�ration a r�ussie
     */
    public boolean rollBackTransaction() {
        boolean resultat = true;
        try {
            Transaction tx = session.getTransaction(); // ON r�cup�re la transaction

            if (tx != null && tx.wasCommitted() && !tx.wasRolledBack()) { // Si la transaction r�unnie les conditions n�c�ssaires
                tx.rollback(); // On annule le commit
            } else {
                resultat = false;
            }
        } catch (Exception e) {
            System.err.println("Erreur dans la classe Hibernate");
            e.printStackTrace();
        }

        return resultat;
    }

    /**
     * Commit la transaction
     * @return un booleen indiquant si l'op�ration a fonctionn�e
     */
    public boolean commitTransaction() {
        boolean resultat = true;
        try {
            if (session.getTransaction().isActive()) {
                session.getTransaction().commit(); // On commit la session
            } else {
                resultat = false;
            }
        } catch (Exception e) {
            System.err.println("Erreur dans la classe Hibernate");
            e.printStackTrace();
        }

        return resultat;
    }

    /**
     * Sauvegarde d'un Object en base de donn�es
     * @param o un Object
     * @return un int sp�cifiant le nombre de ligne modifi�es
     */
    public int save(Object o) {
        int resultat = -1;

        try {
            if (o != null && session.isConnected() && session.isOpen()) { // On v�rifie que la session est pr�te
                session.saveOrUpdate(o); // On sauvegarde l'objet
                resultat = 1;
            }
        } catch (Exception e) { //TODO Cr�er une bonne Exception
            System.err.println("Erreur dans la classe Hibernate");
            e.printStackTrace();
        }

        return resultat;
    }
    
    /**
     * Suppression d'un Object en base de données
     * @param o un Object
     * @return un int spécifiant le nombre de ligne modifiées
     */
    public int delete(Object o) {
        int resultat = -1;

        try {
            if (o != null && session.isConnected() && session.isOpen()) { // On v�rifie que la session est prête
                session.delete(o); // On sauvegarde l'objet
                resultat = 1;
            }
        } catch (Exception e) {
            System.err.println("Erreur dans la classe Hibernate");
            e.printStackTrace();
        }


        return resultat;
    }

    /**
     * S�lection d'une liste d'Object en base de donn�es
     * @param requete
     * @return un int sp�cifiant le nombre de ligne modifi�es
     */
    public List<?> select(String requete) {

        List<?> list = null;

        try {
            if (session != null && session.isConnected() && session.isOpen()) { // On v�rifie que la session est pr�te
                list = session.createQuery(requete).list();
            }
        } catch (Exception e) {
            System.err.println("Erreur dans la classe Hibernate");
            e.printStackTrace();
        }

        return list;
    }
    


	/**
	 * Getter de l'instance de Hibernate
	 * @return Un Objet Hibernate
	 */
	public static Hibernate getInstance() {
		if (instance == null)
			instance = new Hibernate();
		return instance;
	}
	
	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}	
}
