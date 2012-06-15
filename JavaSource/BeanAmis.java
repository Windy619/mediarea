

import java.util.ArrayList;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import metier.utilisateur.Amitie;
import metier.utilisateur.Notification;
import metier.utilisateur.Utilisateur;

import org.primefaces.event.DragDropEvent;

import dao.utilisateur.DaoAmitie;
import dao.utilisateur.DaoUtilisateur;

/**
 * @author Benjamin
 *
 */
public class BeanAmis {
	// DAO
	private DaoUtilisateur daoUtilisateur;
	private DaoAmitie daoAmitie;
	
	// Propriétés
	private String rechercheUtilisateur;
	private String rechercheAmis;	
	
	private Integer nombreAmis;
	private Integer nombreUtilisateursTrouves;
	private Integer nombreSuggestions;	
	
	private Utilisateur nouvelAmis;
	private Amitie ancienAmis;
	
	private ArrayList<Utilisateur> resultatsRechercheUtilisateur;
	private ArrayList<Amitie> resultatsRechercheAmis;
	private ArrayList<Amitie> suggestionUtilisateurs;
	
	// Boolean
	private boolean panelSuggestionAffiche;
	private boolean panelRechercheAffiche;
	
	// Datagrid
	private int nbColonneRecherche;
	private int nbColonneSuggestion;
	
	// Bean
	private BeanConnexion beanConnexion;

	// Utilisateur connecté actuellement
	private Utilisateur utilisateurConnecte;
		
	/**
	 * Constructeur du bean
	 */
	public BeanAmis() {
		
		// Initialisation des dao
		daoUtilisateur = new DaoUtilisateur();
		daoAmitie = new DaoAmitie();
		
		// Maj des boolean de configuration
		panelSuggestionAffiche = false;
		panelRechercheAffiche = true;
		
		nombreAmis = 0;
		nombreUtilisateursTrouves = 0;
		nombreSuggestions = 0;	
		
		nbColonneRecherche = 5;
		nbColonneSuggestion = 5;		
		
		// Chargement de l'utilisateur connecte
		beanConnexion = (BeanConnexion) FacesContext.getCurrentInstance().getCurrentInstance().getExternalContext().getSessionMap().get("beanConnexion");
		
		if (beanConnexion != null) {
			utilisateurConnecte = beanConnexion.getUser();
			if (utilisateurConnecte != null)
				chargerAmis();			
		}


	}
	
	/**
	 * On Drop d'un utilisateur dans la zone dropable
	 * @param ddEvent
	 */
    public void onUtilisateurDrop(DragDropEvent ddEvent) {  
    	// On récupère l'utilisateur nouvelAmis
        nouvelAmis = ((Utilisateur) ddEvent.getData());  
        // On crée l'amitié
        ajouterAmis();
    }  
    
	/**
	 * On Drop d'un utilisateur dans la zone dropable
	 * @param ddEvent
	 */
    public void onSuggestionDrop(DragDropEvent ddEvent) {  
    	// On récupère l'utilisateur nouvelAmis
    	Amitie amis = ((Amitie) ddEvent.getData());
        nouvelAmis =   amis.getAmi();
        // On crée l'amitié
        ajouterAmis();
    }      
    
	/**
	 * Recherche d'un utilisateur
	 */
	public String rechercherUtilisateur() {
		
		if (rechercheUtilisateur != null && !rechercheUtilisateur.isEmpty()) {
			resultatsRechercheUtilisateur = new ArrayList(daoUtilisateur.rechercheNonAmis(rechercheUtilisateur, utilisateurConnecte));
			nombreUtilisateursTrouves = resultatsRechercheUtilisateur.size();
			
			if (resultatsRechercheUtilisateur.size() < 5) {
				nbColonneRecherche = resultatsRechercheUtilisateur.size();
			}
			
			panelRechercheAffiche = true;
			
			chargerAmis();				
		}
		
		return "rechercher";
	}	
	
	/**
	 * Recherche d'un utilisateur amis
	 */
	public String rechercherAmis() {
		
		if (!rechercheAmis.isEmpty()) {
			resultatsRechercheAmis = new ArrayList(daoUtilisateur.rechercheAmis(rechercheAmis, utilisateurConnecte));				
		}
		
		return "rechercheramis";
	}		
	
	/**
	 * Cherche une suggestion d'utilisateur a ajouter à la liste d'amis
	 * @return
	 */
	public String chargerSuggestion() {
		
		suggestionUtilisateurs = new ArrayList(daoUtilisateur.rechercheAmisPossible(utilisateurConnecte));		
		
		nombreSuggestions = suggestionUtilisateurs.size();
		
		panelSuggestionAffiche = true;
		
		if (suggestionUtilisateurs.size() < 5) {
			nbColonneSuggestion = suggestionUtilisateurs.size();
		}
		
		return "suggestionutilisateur";
	}
	
	/**
	 * Cacher la liste de suggestion
	 * @return
	 */
	public String cacherSuggestion() {
		
		panelSuggestionAffiche = false;
		
		return "cachersuggestionutilisateur";
	}	
		
	/**
	 * Charge la liste d'amis
	 * @return
	 */
	public String chargerAmis() {
		
		resultatsRechercheAmis = new ArrayList<Amitie>(utilisateurConnecte.getAmis());
		nombreAmis = resultatsRechercheAmis.size();
		
		return "chargeramis";
	}

	
	/**
	 * Ajouter un amis
	 */
	public String ajouterAmis() {
		
		utilisateurConnecte.getAmis().add(new Amitie(utilisateurConnecte,nouvelAmis));
		daoUtilisateur.sauvegarder(utilisateurConnecte);
		
		chargerAmis();
		
		// On envoi une notification à l'utilisateur ajouté pour lui signaler
		Notification notification = new Notification("L'utilisateur \"" + utilisateurConnecte.getPseudo() + "\" vous a ajouter à sa liste d'amis !", nouvelAmis);
		notification.setDateEnvoiNotification(new Date());
		// On l'ajoute à l'utilisateur concerné et on le sauvegarde
		nouvelAmis.getNotifications().add(notification);
		daoUtilisateur.sauvegarder(nouvelAmis);	
		
		// On ajoute un message montrant à l'utilisateur que l'amitié a bien été crée !
        FacesMessage msg = new FacesMessage(nouvelAmis.getPseudo() + " a bien été ajouté à votre liste d'amis !");  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
        
		return "ajouteramis";
	}	
	
	/**
	 * Supprimer un amis
	 */
	public String supprimerAmis() {
					
		utilisateurConnecte.getAmis().remove(ancienAmis);
		// On sauvegarde l'utilisateur
		daoUtilisateur.sauvegarder(utilisateurConnecte);
		
		daoAmitie.supprimer(ancienAmis);
		
		// On charge les amis
		chargerAmis();
		
		// On ajoute un message montrant à l'utilisateur que l'amitié a bien été supprimée !
        FacesMessage msg = new FacesMessage(ancienAmis.getAmi().getPseudo() + " a bien été retiré de votre liste d'amis !");  
        FacesContext.getCurrentInstance().addMessage(null, msg);  		

		return "supprimeramis";
	}	
	
	// GETTER / SETTER

	public Utilisateur getNouvelAmis() {
		return nouvelAmis;
	}


	public void setNouvelAmis(Utilisateur nouvelAmis) {
		this.nouvelAmis = nouvelAmis;
	}


	public Amitie getAncienAmis() {
		return ancienAmis;
	}


	public void setAncienAmis(Amitie ancienAmis) {
		this.ancienAmis = ancienAmis;
	}


	public ArrayList<Amitie> getSuggestionUtilisateurs() {
		return suggestionUtilisateurs;
	}


	public void setSuggestionUtilisateurs(
		ArrayList<Amitie> suggestionUtilisateurs) {
		this.suggestionUtilisateurs = suggestionUtilisateurs;
	}


	public Utilisateur getUtilisateurConnecte() {
		utilisateurConnecte = beanConnexion.getUser();
		return utilisateurConnecte;
	}


	public void setUtilisateurConnecte(Utilisateur utilisateurConnecte) {
		this.utilisateurConnecte = utilisateurConnecte;
	}

	public boolean isPanelSuggestionAffiche() {
		return panelSuggestionAffiche;
	}

	public void setPanelSuggestionAffiche(boolean panelSuggestionAffiche) {
		this.panelSuggestionAffiche = panelSuggestionAffiche;
	}


	public int getNbColonneRecherche() {
		return nbColonneRecherche;
	}


	public void setNbColonneRecherche(int nbColonneRecherche) {
		this.nbColonneRecherche = nbColonneRecherche;
	}


	public int getNbColonneSuggestion() {
		return nbColonneSuggestion;
	}


	public void setNbColonneSuggestion(int nbColonneSuggestion) {
		this.nbColonneSuggestion = nbColonneSuggestion;
	}


	public boolean isPanelRechercheAffiche() {
		return panelRechercheAffiche;
	}


	public void setPanelRechercheAffiche(boolean panelRechercheAffiche) {
		this.panelRechercheAffiche = panelRechercheAffiche;
	}


	public String getRechercheUtilisateur() {
		return rechercheUtilisateur;
	}


	public void setRechercheUtilisateur(String rechercheUtilisateur) {
		this.rechercheUtilisateur = rechercheUtilisateur;
	}


	public String getRechercheAmis() {
		return rechercheAmis;
	}


	public void setRechercheAmis(String rechercheAmis) {
		this.rechercheAmis = rechercheAmis;
	}


	public ArrayList<Utilisateur> getResultatsRechercheUtilisateur() {
		return resultatsRechercheUtilisateur;
	}


	public void setResultatsRechercheUtilisateur(
			ArrayList<Utilisateur> resultatsRechercheUtilisateur) {
		this.resultatsRechercheUtilisateur = resultatsRechercheUtilisateur;
	}


	public ArrayList<Amitie> getResultatsRechercheAmis() {
		return resultatsRechercheAmis;
	}


	public void setResultatsRechercheAmis(ArrayList<Amitie> resultatsRechercheAmis) {
		this.resultatsRechercheAmis = resultatsRechercheAmis;
	}


	public Integer getNombreAmis() {
		return nombreAmis;
	}


	public void setNombreAmis(Integer nombreAmis) {
		this.nombreAmis = nombreAmis;
	}


	public Integer getNombreUtilisateursTrouves() {
		return nombreUtilisateursTrouves;
	}


	public void setNombreUtilisateursTrouves(Integer nombreUtilisateursTrouves) {
		this.nombreUtilisateursTrouves = nombreUtilisateursTrouves;
	}


	public Integer getNombreSuggestions() {
		return nombreSuggestions;
	}


	public void setNombreSuggestions(Integer nombreSuggestions) {
		this.nombreSuggestions = nombreSuggestions;
	}





}
