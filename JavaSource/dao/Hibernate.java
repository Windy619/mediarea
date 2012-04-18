package dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;

/**
 * <h1>Classe Hibernate</h1>
 * Cette classe permet d'initialiser le Framework Hibernate <br />
 * <b> Cette classe ne peut être instanciée qu'une seule fois </b>
 * @author marzolf
 */
public class Hibernate {

	/** 
	 * Instance 
	 */
	public static Hibernate instance;
	
	/** 
	 * Usine à Session 
	 */
	private SessionFactory sessionFactory;
	
	/**
	 * Session Hibernate
	 */
	private Session session;
	
	/**
	 * Constructeur privé
	 */
	private Hibernate() {
		// Configuration de la factory
        sessionFactory = new Configuration()
        .configure() // configuration des données depuis le fichier hibernate.cfg
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
     * @return un booleen indiquant si l'opération a fonctionné
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
     * @return un booleen indiquant si l'opération a fonctionné
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
     * @return un booleen indiquant si l'opération a fonctionné
     */
    public boolean demarrerTransaction() {
        boolean resultat = true;
        try {
            if (session.isOpen()) {
                session.beginTransaction(); // On débute la transaction
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
     * @return Un booleen indiquant si l'opération a réussie
     */
    public boolean rollBackTransaction() {
        boolean resultat = true;
        try {
            Transaction tx = session.getTransaction(); // ON récupère la transaction

            if (tx != null && tx.wasCommitted() && !tx.wasRolledBack()) { // Si la transaction réunnie les conditions nécéssaires
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
     * @return un booleen indiquant si l'opération a fonctionnée
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
     * Sauvegarde d'un Object en base de données
     * @param o un Object
     * @return un int spécifiant le nombre de ligne modifiées
     */
    public int save(Object o) {
        int resultat = -1;

        try {
            if (o != null && session.isConnected() && session.isOpen()) { // On vérifie que la session est prète
                session.saveOrUpdate(o); // On sauvegarde l'objet
                resultat = 1;
            }
        } catch (Exception e) { //TODO Créer une bonne Exception
            System.err.println("Erreur dans la classe Hibernate");
            e.printStackTrace();
        }

        return resultat;
    }
    
    /**
     * Suppression d'un Object en base de donnÃ©es
     * @param o un Object
     * @return un int spÃ©cifiant le nombre de ligne modifiÃ©es
     */
    public int delete(Object o) {
        int resultat = -1;

        try {
            if (o != null && session.isConnected() && session.isOpen()) { // On vérifie que la session est prÃªte
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
     * Sélection d'une liste d'Object en base de données
     * @param requete
     * @return un int spécifiant le nombre de ligne modifiées
     */
    public List<?> select(String requete) {

        List<?> list = null;

        try {
            if (session != null && session.isConnected() && session.isOpen()) { // On vérifie que la session est prète
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
