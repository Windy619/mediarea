import java.util.ArrayList;
import java.util.Date;

import javax.faces.context.FacesContext;

import metier.utilisateur.Notification;
import metier.utilisateur.Utilisateur;
import dao.utilisateur.DaoNotification;
import dao.utilisateur.DaoUtilisateur;


public class BeanNotification {

	// Dao Utilisateur
	private DaoNotification daoNotification = new DaoNotification();
	private DaoUtilisateur daoUtilisateur = new DaoUtilisateur();
	
	// Données
	private ArrayList<Notification> listeNotifications;
	private String nbNotifications;
	
	// Utilisateur connecte actuellement
	private Utilisateur utilisateurConnecte;
	
	//Bean
	private BeanConnexion beanConnexion;   	
	
	public BeanNotification() {
				
		// Chargement de l'utilisateur connecte
		beanConnexion = (BeanConnexion) FacesContext.getCurrentInstance().getCurrentInstance().getExternalContext().getSessionMap().get("beanConnexion");
		
		listeNotifications = new ArrayList<Notification>();
				
		if (beanConnexion != null) {
			// On charge l'utilisateur connecte
			utilisateurConnecte = beanConnexion.getUser();		
			if (utilisateurConnecte != null) {
				// Si l'utilisateur n'est pas null, on charge des notifications
				listeNotifications = new ArrayList(daoNotification.notifications(utilisateurConnecte));	
			}
		}	
		
		nbNotifications = String.valueOf(listeNotifications.size());		
		
	}
	
	/**
	 * Une fois qu'elles ont été ouvertes, on considère les notifications comme lues
	 * @return
	 */
	public void lireNotifications() {
		
		// On met à jour les notifications
		for (Notification notification : listeNotifications) {
			notification.setDateLectureNotification(new Date());
			daoNotification.sauvegarder(notification);
		}
		
		// On vide la liste des notifications
		listeNotifications.clear();
		
	}
	
	// GETTER SETTER
	
	public ArrayList<Notification> getListeNotifications() {
		return listeNotifications;
	}

	public void setListeNotifications(ArrayList<Notification> listeNotifications) {
		this.listeNotifications = listeNotifications;
	}

	public String getNbNotifications() {
		return nbNotifications;
	}

	public void setNbNotifications(String nbNotifications) {
		this.nbNotifications = nbNotifications;
	}	
	
	
	
}
