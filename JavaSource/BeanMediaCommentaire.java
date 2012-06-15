import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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
	private Media mediaVisualise;
	private long nbCommentaires;
	private String commentaireSaisi;
	private List<Commentaire> listeCommentaires;
	private Utilisateur util;
	private Query resultatReponses;
 	private HashMap<Commentaire, ArrayList<Commentaire>> hmReponses;
 	private Commentaire pere;
 	private ArrayList<Commentaire> lstFils;
 	private FacesContext context;
	private String reponseSaisie;
	private String raisonCommentaire;
 	private boolean estCommentairesAutorise;
	private int nbCaracteresRestants;
	private List<Commentaire> listeReponses;
	private Commentaire commentaireAffiche;
	
	// Bean
	private BeanMedia beanMedia;
	 	
	
	/**
	 * Constructeur du Bean
	 */
	public BeanMediaCommentaire() {
		// Chargement du m�dia visualis�
		beanMedia = (BeanMedia) FacesContext.getCurrentInstance().getCurrentInstance().getExternalContext().getSessionMap().get("beanMedia");
		
		//Instantiation des Dao
		daoMedia = new DaoMedia();
		daoUtilisateur = new DaoUtilisateur();
		daoCommentaire = new DaoCommentaire();
		
		util = daoUtilisateur.getUn(1);
		context = FacesContext.getCurrentInstance();
		
		//nbCommentaires = String.valueOf(mediaVisualise.getCommentaires().size()); //toujours renseign� une cha�ne de caract�res pour un outputText
		nbCommentaires = beanMedia.getMediaVisualise().getCommentaires().size(); //Converter en JSF
		
		estCommentairesAutorise = beanMedia.getMediaVisualise().isaCommentairesOuverts();
		
		nbCaracteresRestants = 500;
		
		//Chargement des commentaires
		chargerCommentaires();
		
		//Chargement des r�ponses
		chargerReponses();
	}
	
	/** 
	 * D�cr�mentation du nombre de caract�res restants (composition d'un commentaire)
	 * @return
	 */
	public String decrementerNbCaracteresRestants() {
		System.out.println("decrementerNbCaracteresRestants");
		
		//D�cr�mentation du nombre de caract�res restants pour la saisie du commentaire
		nbCaracteresRestants--;
		
		return "decrementerNbCaracteresRestants";
	}
	
	/** 
	 * Publication du commentaire
	 * @return
	 */
	//public void publierCommentaire(AjaxBehaviorEvent e) {
	public String publierCommentaire() {
		System.out.println("publierCommentaire");
		System.out.println("commentaire saisi : " + commentaireSaisi);
		
		//Cr�ation du commentaire
		Commentaire c = new Commentaire(commentaireSaisi,util);
		
		//Ajout du commentaire � la liste de commentaires du m�dia
		beanMedia.getMediaVisualise().getCommentaires().add(c);
		
		//Enregistrement de l'ajout
		daoMedia.sauvegarder(beanMedia.getMediaVisualise());
		
		//Rechargement de la liste de commentaires
		//commentaires.add(c);
		chargerCommentaires();
		
		return "publierCommentaire";
	}
	
	/** 
	 * Chargement de la liste de commentaires
	 * @return
	 */
	public void chargerCommentaires() {
		//System.out.println("chargerCommentaires");
		
		//Chargement de la liste des commentaires associ� au m�dia
		listeCommentaires = daoMedia.getCommentaires(beanMedia.getMediaVisualise());
	}
	
	/** 
	 * Chargement de la liste des r�ponses associ�e � un commentaire
	 * @return
	 */
	public String chargerReponses() {
		//System.out.println("chargerReponses");
		
		//R�cup�ration de la liste des commentaires r�ponse du m�dia
		resultatReponses = daoMedia.getReponses(beanMedia.getMediaVisualise());
		
		//Cr�ation de la HashMap avec en cl� le commentaire p�re et en valeur la liste des commentaires fils
		hmReponses = new HashMap<Commentaire, ArrayList<Commentaire>>();
		
		//Remplissage de la HashMap ...
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
    	
    	/*Set cles = hmReponses.keySet();
    	Iterator itHm = cles.iterator();
    	while(itHm.hasNext()) {
    		Commentaire cle = (Commentaire)itHm.next();
    		Object valeur = hmReponses.get(cle); //parcourir l'Arraylist
    		System.out.println("Contenu comm p�re : " + cle.getContenuCommentaire());
    		System.out.println("Contenu comms fils : " + valeur);
    	}*/
		
		return "chargerReponses";
	}
	
	/** 
	 * R�cup�ration de la liste des commentaires fils associ�s au commentaire pass� en param�tre
	 * @return
	 */
	public ArrayList<Commentaire> mapValue(Commentaire c) {
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
	public String supprimerCommentaire() {
		System.out.println("supprimerCommentaire");
		
		// Si celui qui tente de supprimer un commentaire est l'utilisateur connect�
		//if (commSelectionne.getAuteur() == utilisateurConnecte) { //TODO
			//Suppression du commentaire trait� de la liste des commentaires du m�dia
			beanMedia.getMediaVisualise().getCommentaires().remove(daoCommentaire.getUn(Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idCommentaire"))));
			
			//Enregistrement de la modification
			daoMedia.sauvegarder(beanMedia.getMediaVisualise());
			
			//Affichage de la notification
			context.addMessage(null, new FacesMessage("Suppression du commentaire", "Le commentaire a �t� supprim�"));
		//}
		
		//Rafra�chissement des listes
		chargerCommentaires();
		chargerReponses();
		
		//update + 2 min TODO
		
		return "supprimerCommentaire";
	}

	/** 
	 * R�ponse � un commentaire
	 * @return
	 */
	public String repondreCommentaire() {
		System.out.println("repondreCommentaire");
		
		// Cr�ation d'une nouvelle r�ponse
		Commentaire c = new Commentaire(reponseSaisie,util);
		
		// Ajout aux r�ponses du commentaire p�re
		daoCommentaire.getUn(2).getCommentairesFils().add(c); //TODO bon commentaire
		
		// Sauvegarde de l'ajout
		daoCommentaire.sauvegarder(daoCommentaire.getUn(2));
		
		// Rechargement de la liste de r�ponses
		chargerReponses();
		
		return "repondreCommentaire";
	}
	
	/** 
	 * Signalement d'un commentaire
	 * @return
	 */
	public String signalerCommentaire() {
		System.out.println("signalerCommentaire");
		
		//interdiction de signaler soi-m�me
		//if (commentaireSelectionne.getAuteur() != utilisateurConnecte) {
			//Cr�ation du signalement
			Signalement_Commentaire sc = new Signalement_Commentaire(raisonCommentaire, daoCommentaire.getUn(2), daoUtilisateur.getUn(1)); //TODOO bon commentaire signal�
		
			//Ajout du signalement aux signalements du commentaire existants
			util.getSignalementsCommentaires().add(sc);
			
			//Sauvegarde de l'ajout
			daoUtilisateur.sauvegarder(util);
			// TODO => update (car suppression)
			
			//Rechargement des listes
			chargerCommentaires();
			chargerReponses();
		//}
		
		return "signalerCommentaire";
	}
	

	
	// GETTER / SETTER
	
	public int getNbCaracteresRestants() {
		return nbCaracteresRestants;
	}
	
	public void setNbCaracteresRestants(int nbCaracteresRestants) {
		this.nbCaracteresRestants = nbCaracteresRestants;
	}

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
		return commentaireAffiche;
	}
	
	public void setCommentaireAffiche(Commentaire commentaireAffiche) {
		this.commentaireAffiche = commentaireAffiche;
	}
	
	public List<Commentaire> getListeReponses() {
		return listeReponses;
	}
	
	public void setListeReponses(List<Commentaire> listeReponses) {
		this.listeReponses = listeReponses;
	}
}
