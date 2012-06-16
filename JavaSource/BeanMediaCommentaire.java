import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.hibernate.Query;

import metier.media.*;
import dao.media.*;

import metier.utilisateur.*;
import dao.utilisateur.*;

/**
 * @author Florence
 *
 */
public class BeanMediaCommentaire {
	// DAO
	private static DaoMedia daoMedia;
	private static DaoCommentaire daoCommentaire;
	private static DaoUtilisateur daoUtilisateur;
	
	// Propri�t�s
	private long nbCommentaires;
	private String commentaireSaisi;
	private List<Commentaire> listeCommentaires;
	private Query resultatReponses;
 	private HashMap<Commentaire, ArrayList<Commentaire>> hmReponses;
 	private Commentaire pere;
 	private ArrayList<Commentaire> lstFils;
	private String reponseSaisie;
	private String raisonCommentaire;
 	private boolean estCommentairesAutorise;
	//private int nbCaracteresRestants;
	private List<Commentaire> listeReponses;
	private Commentaire commentaireAffiche;
	private FacesMessage message;
	
	// Bean
	private BeanMedia beanMedia;
	private BeanConnexion beanConnexion;
	
	// Utilisateur connect� actuellement
	private Utilisateur utilisateurConnecte;
	 	
	
	/**
	 * Constructeur du Bean
	 */
	public BeanMediaCommentaire() {
		// Chargement du m�dia visualis�
		beanMedia = (BeanMedia) FacesContext.getCurrentInstance().getCurrentInstance().getExternalContext().getSessionMap().get("beanMedia");
		beanConnexion = (BeanConnexion) FacesContext.getCurrentInstance().getCurrentInstance().getExternalContext().getSessionMap().get("beanConnexion");
		
		if (beanConnexion != null) {
			// R�cup�ration des informations de l'utilisateur connect�
			utilisateurConnecte = beanConnexion.getUser();
		}		
		
		// Instantiation des Dao
		daoMedia = new DaoMedia();
		daoUtilisateur = new DaoUtilisateur();
		daoCommentaire = new DaoCommentaire();
				
		nbCommentaires = beanMedia.getMediaVisualise().getCommentaires().size(); //Converter en JSF
		
		estCommentairesAutorise = beanMedia.getMediaVisualise().isaCommentairesOuverts();
		
		//nbCaracteresRestants = 500;
		
		// Chargement des commentaires
		chargerCommentaires();
		
		// Chargement des r�ponses
		chargerReponses();
	}
	
	/** 
	 * D�cr�mentation du nombre de caract�res restants (composition d'un commentaire)
	 * @return
	 */
	/*public String decrementerNbCaracteresRestants() {
		System.out.println("decrementerNbCaracteresRestants");
		
		//D�cr�mentation du nombre de caract�res restants pour la saisie du commentaire
		nbCaracteresRestants--;
		
		return "decrementerNbCaracteresRestants";
	}*/
	
	/** 
	 * Publication du commentaire
	 * @return
	 */
	//public void publierCommentaire(AjaxBehaviorEvent e) {
	public String publierCommentaire() {
		System.out.println("publierCommentaire");

		if(utilisateurConnecte != null) {
			System.out.println("Commentaire saisi : " + commentaireSaisi);			
			
			// Cr�ation du commentaire
			Commentaire c = new Commentaire(commentaireSaisi, utilisateurConnecte);
			
			// Ajout du commentaire � la liste de commentaires du m�dia
			beanMedia.getMediaVisualise().getCommentaires().add(c);
			
			// Enregistrement de l'ajout
			daoMedia.sauvegarder(beanMedia.getMediaVisualise());
			
			// Rechargement de la liste de commentaires
			//commentaires.add(c);
			chargerCommentaires();
			
			commentaireSaisi = "";
		}
		else {
			// Pr�paration du message de la notification
			message = new FacesMessage("Publication : Connectez-vous ou inscrivez-vous d�s maintenant !");
			message.setSeverity(FacesMessage.SEVERITY_WARN);
			
			// Affichage de la notification
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		
		return "publierCommentaire";
	}
	
	/** 
	 * Chargement de la liste de commentaires
	 * @return
	 */
	public void chargerCommentaires() {
		//System.out.println("chargerCommentaires");
		
		// Chargement de la liste des commentaires associ� au m�dia
		listeCommentaires = daoMedia.getCommentaires(beanMedia.getMediaVisualise());
	}
	
	/** 
	 * Chargement de la liste des r�ponses associ�e � un commentaire
	 * @return
	 */
	public String chargerReponses() {
		//System.out.println("chargerReponses");
		
		// R�cup�ration de la liste des commentaires r�ponse du m�dia
		resultatReponses = daoMedia.getReponses(beanMedia.getMediaVisualise());
		
		// Cr�ation de la HashMap avec en cl� le commentaire p�re et en valeur la liste des commentaires fils
		hmReponses = new HashMap<Commentaire, ArrayList<Commentaire>>();
		
		// Remplissage de la HashMap ...
		pere = null;
		lstFils = new ArrayList<Commentaire>();
		Commentaire tmp = null;
		
        for(Iterator<?> it = resultatReponses.iterate(); it.hasNext(); ) {
        	Object[] rowCommentaire = (Object[]) it.next();
        	
        	pere = (Commentaire)rowCommentaire[0];
        	
        	if (tmp == null || tmp == pere ) {
        		tmp = pere;
            	lstFils.add((Commentaire)rowCommentaire[1]);        		
        	} else  {
        		hmReponses.put(tmp, lstFils);
        		tmp = null;
        		lstFils.clear();
        	}
		}  
        
        if (tmp != null && lstFils != null) {
        	hmReponses.put(tmp, lstFils);
        }
    	
    	Set cles = hmReponses.keySet();
    	Iterator itHm = cles.iterator();
    	while(itHm.hasNext()) {
    		Commentaire cle = (Commentaire)itHm.next();
    		Object valeur = hmReponses.get(cle); //parcourir l'Arraylist
    		System.out.println("Contenu comm p�re : " + cle.getContenuCommentaire());
    		System.out.println("Contenu comms fils : " + valeur);
    	}
		
		return "chargerReponses";
	}
	
	/** 
	 * R�cup�ration de la liste des commentaires fils associ�s au commentaire pass� en param�tre
	 * @return
	 */
	public ArrayList<Commentaire> mapValue(Commentaire c) {
		//System.out.println("m�thode mapValue");
		
		//System.out.println("Commentaire p�re : " + c);
		
		//System.out.println("HashMap : " + hmReponses.get(c));
		
		return hmReponses.get(c);
	}
	
	/** 
	 * Nombre de commentaires fils associ�s au commentaire pass� en param�tre
	 * @return
	 */
	public int nbReponses(Commentaire c) {
		if(hmReponses.get(c) == null) {
			return 0;
		}
		else {
			return hmReponses.get(c).size();
		}
	}
	
	/** 
	 * Suppression du commentaire
	 * @return
	 */
	public String supprimerCommentaire() { //dateSuppression XXX
		System.out.println("supprimerCommentaire");
		
		if(utilisateurConnecte != null) {
			System.out.println("Commentaire affich� : " + commentaireAffiche);
			
			// Si celui qui tente de supprimer un commentaire est l'utilisateur connect�
			//if (commSelectionne.getAuteur() == utilisateurConnecte) {
				//Suppression du commentaire trait� de la liste des commentaires du m�dia
				beanMedia.getMediaVisualise().getCommentaires().remove(commentaireAffiche);
				
				//Enregistrement de la modification
				daoMedia.sauvegarder(beanMedia.getMediaVisualise());				
			//}
			
			// Rafra�chissement des listes
			chargerCommentaires();
			chargerReponses();		
	
			// Pr�paration du message de la notification
			message = new FacesMessage("Suppression du commentaire : Message supprim� avec succ�s !");
			
			//2 min XXX
		}
		else {
			// Pr�paration du message de la notification
			message = new FacesMessage("Suppression du commentaire  : Connectez-vous ou inscrivez-vous d�s maintenant !");
			message.setSeverity(FacesMessage.SEVERITY_WARN);
		}
		
		// Affichage de la notification
		FacesContext.getCurrentInstance().addMessage(null, message);
		
		return "supprimerCommentaire";
	}

	/** 
	 * R�ponse � un commentaire
	 * @return
	 */
	public String repondreCommentaire() {
		System.out.println("repondreCommentaire");
		
		if(utilisateurConnecte == null) {
			System.out.println("Commentaire affich� : " + commentaireAffiche);
			
			// Cr�ation d'une nouvelle r�ponse
			Commentaire c = new Commentaire(reponseSaisie, utilisateurConnecte);
			
			// Ajout aux r�ponses du commentaire p�re
			commentaireAffiche.getCommentairesFils().add(c);
			
			// Sauvegarde de l'ajout
			daoCommentaire.sauvegarder(commentaireAffiche);
			
			// Rechargement de la liste de r�ponses
			chargerReponses();
			
			// Pr�paration du message de la notification
			message = new FacesMessage("R�ponse au commentaire : R�ponse envoy� avec succ�s !");
			
			commentaireSaisi = "";
		}
		else {
			// Pr�paration du message de la notification
			message = new FacesMessage("R�ponse au commentaire : Connectez-vous ou inscrivez-vous d�s maintenant !");
			message.setSeverity(FacesMessage.SEVERITY_WARN);
		}
		
		// Affichage de la notification
		FacesContext.getCurrentInstance().addMessage(null, message);
		
		return "repondreCommentaire";
	}
	
	/** 
	 * Signalement d'un commentaire
	 * @return
	 */
	public String signalerCommentaire() {
		System.out.println("signalerCommentaire");
		
		if(utilisateurConnecte != null) {
			System.out.println("Commentaire affich� : " + commentaireAffiche);
			
			// Interdiction de signaler soi-m�me
			//if (commentaireSelectionne.getAuteur() != utilisateurConnecte) {
				// Cr�ation du signalement
				Signalement_Commentaire sc = new Signalement_Commentaire(raisonCommentaire, commentaireAffiche, daoUtilisateur.getUn(1));
			
				// Ajout du signalement aux signalements du commentaire existants
				utilisateurConnecte.getSignalementsCommentaires().add(sc);
				
				// Sauvegarde de l'ajout
				daoUtilisateur.sauvegarder(utilisateurConnecte);
				
				// Cr�ation de la notification
				Notification notification = new Notification("Votre message : \"" + commentaireAffiche.getContenuCommentaire() + "\" a fait l'objet d'un signalement !", commentaireAffiche.getAuteur());
				notification.setDateEnvoiNotification(new Date());
				// Ajout de la notification � l'utilisateur concern� et sauvegarde de celui-ci
				commentaireAffiche.getAuteur().getNotifications().add(notification);
				daoUtilisateur.sauvegarder(commentaireAffiche.getAuteur());
					        
				// Rechargement des listes
				chargerCommentaires();
				chargerReponses();
	
				// Pr�paration du message de la notification
		        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Signalement d'un commentaire :", "Signalement du commentaire effectu� avec succ�s !");
			//}
		}
		else {
			// Pr�paration du message de la notification
			message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Signalement d'un commentaire :", "Connectez-vous ou inscrivez-vous d�s maintenant !");
		}
		
		// Affichage de la notification
		FacesContext.getCurrentInstance().addMessage(null, message);
		
		return "signalerCommentaire";
	}
	

	
	// GETTER / SETTER
	
	/*public int getNbCaracteresRestants() {
		return nbCaracteresRestants;
	}
	
	public void setNbCaracteresRestants(int nbCaracteresRestants) {
		this.nbCaracteresRestants = nbCaracteresRestants;
	}*/

	public String getCommentaireSaisi() {
		return commentaireSaisi;
	}
	
	public void setCommentaireSaisi(String commentaireSaisi) {
		this.commentaireSaisi = commentaireSaisi;
	}
	

	public List<Commentaire> getListeCommentaires() {
		return listeCommentaires;
	}
	
	public void setListeCommentaires(List<Commentaire> listeCommentaires) {
		this.listeCommentaires = listeCommentaires;
	}

	public String getReponseSaisie() {
		return reponseSaisie;
	}

	public void setReponseSaisie(String reponseSaisie) {
		this.reponseSaisie = reponseSaisie;
	}
	
	public String getRaisonCommentaire() {
		return raisonCommentaire;
	}

	public void setRaisonCommentaire(String raisonCommentaire) {
		this.raisonCommentaire = raisonCommentaire;
	}
	
	public long getNbCommentaires() {
		return nbCommentaires;
	}
	
	public void setNbCommentaires(long nbCommentaires) {
		this.nbCommentaires = nbCommentaires;
	}

	public boolean isEstCommentairesAutorise() {
		return estCommentairesAutorise;
	}

	public void setEstAutoriseCommentaires(boolean estAutoriseCommentaires) {
		this.estCommentairesAutorise = estAutoriseCommentaires;
	}
	
	public Commentaire getCommentaireAffiche() {
		System.out.println("GET Commentaire affich� : " + commentaireAffiche);
		return commentaireAffiche;
	}
	
	public void setCommentaireAffiche(Commentaire commentaireAffiche) {
		this.commentaireAffiche = commentaireAffiche;
		System.out.println("SET Commentaire affich� : " + commentaireAffiche);
	}
	
	public List<Commentaire> getListeReponses() {
		return listeReponses;
	}
	
	public void setListeReponses(List<Commentaire> listeReponses) {
		this.listeReponses = listeReponses;
	}
}
