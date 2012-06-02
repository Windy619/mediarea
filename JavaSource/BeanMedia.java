import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
//import org.richfaces.component.SortOrder;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import org.hibernate.Query;
import org.primefaces.event.*;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

import metier.media.*;
import dao.media.*;

import metier.utilisateur.*;
import dao.utilisateur.*;



/**
 * @author Florence
 *
 */
public class BeanMedia {
	// DAO
	private static DaoMedia daoMedia;
	private static DaoCommentaire daoCommentaire;
	private static DaoRegarder daoRegarder;
	public static DaoTelechargementMedia daoTelechargementMedia;
	private static DaoSignalementMedia daoSignalementMedia;
	private static DaoUtilisateur daoUtilisateur;
	private static DaoAimer daoAimer;
	public static DaoTypePlaylist daoTypePlaylist;
	public static DaoPlaylist daoPlaylist;
	public static DaoVisibilite daoVisibilite;
 	private static DaoCategorie daoCategorie;
 	
 	// Propriétés
	private String idMediaVisualise;
	private Media mediaVisualise;
	private String titreMedia; //nécessaire de faire appel à l'objet car #{beanMedia.mediaVisualise.titreMedia} ne fonctionne pas
	private long nbCommentaires;
	private String auteur;
	private String datePublication;
	private String description;
	private List<String> listeNomCategories;
	private List<String> listeNomTags;
	private List<Commentaire> listeCommentaires;
	private List<Commentaire> listeReponses;
	//private DataModel dataModel = new ListDataModel();
	private double note;
	private String nomTypeMedia;
	private String commentaireSaisi;
	List<Media> listeMediasSuggeres = new ArrayList<Media>();
	SimpleDateFormat dateFormat;
	private int nbCaracteresRestants;
	private Commentaire commentaireAffiche;
	@Size(min = 3, max = 12, message = "La taille du mot de passe doit être entre 3 et 12")
	private String motDePasseMedia;
	private Boolean showMotDePasseMedia;
	private Regarder regarder;
	private long resultatTotalVuesMedia;
	private String motVues;
	private String motTelechargement;
	private long resultatTotalTelechargementMedia;
	//private List<Media> mediaDansPanier; //TODO à mettre en SESSION
	private Signalement_Media signalementMedia;
	private String raison; //récupéré de la vue
	private Utilisateur util;
	private Aimer aimer;
	private long resultatNbAime;
	private long resultatNbNAimePas;
	private String nomAvatar;
	private String txtFavori;
	private String imgFavori;
	private List<Playlist> listePlaylistUt;
	private String imgAjoutPlaylist;
	private boolean estAjouteAPlaylist = false;
	@Size(min = 0, message = "Ce champ est requis.")
    private String nomPlaylistACreer;
	private String descriptionPlaylistACreer;
	private String visibilitePlaylistACreer;
	private FacesMessage message;
	private List<Visibilite> listeVisibilite;
	private List<SelectItem> listeNomVisibilite;
	private long resultatTotalVotesMedia;
	private String motVotes;
	private CartesianChartModel categoryModel;
	private long maxY;
	private String codeIntegration;
    private String lien;
    private String url;
    private String tailleLecteur = "";
    private int largeur = 320;
    private int hauteur = 180;
 	private String detailNotifyAjoutAuPanier;
 	private String estNotifieJAime; //TODO suppr
 	private String detailNotificationJAime;
 	private String detailNotificationJeNAimePas;
 	private HashMap<Commentaire, ArrayList<Commentaire>> hmReponses;
 	private FacesContext context = FacesContext.getCurrentInstance();  
     	
    // Bean
 	private BeanConnexion beanConnexion;
 	
 	// Utilisateur connecté actuellement
 	private Utilisateur utilisateurConnecte;
 	
	
 	
 	/**
	 * Constructeur du Bean
	 */
	public BeanMedia() {
		// Chargement de l'utilisateur connecte
		beanConnexion = (BeanConnexion) FacesContext.getCurrentInstance().getCurrentInstance().getExternalContext().getSessionMap().get("beanConnexion");
				
		if (beanConnexion != null) {
			utilisateurConnecte = beanConnexion.getUser();
			//if (utilisateurConnecte != null) {}
		}
		
		//Instantiation des Dao
		daoMedia = new DaoMedia();
		daoCategorie = new DaoCategorie();
		daoUtilisateur = new DaoUtilisateur();
		daoRegarder = new DaoRegarder();
		daoSignalementMedia = new DaoSignalementMedia();
		daoUtilisateur = new DaoUtilisateur();
		daoAimer = new DaoAimer();
		daoTypePlaylist = new DaoTypePlaylist();
		daoPlaylist = new DaoPlaylist();
		daoVisibilite = new DaoVisibilite();
		daoTelechargementMedia = new DaoTelechargementMedia();
		daoCommentaire = new DaoCommentaire();
		nbCaracteresRestants = 500;
		
		
		util = daoUtilisateur.getUn(1);
		//mediaDansPanier = new ArrayList<Media>();
		commentaireSaisi = "Réagir à propos de ce média.";
		
		//estNotifieJAime = false;
		if(utilisateurConnecte != null) {
			if(daoMedia.getUn(2).getVisibilite().equals(daoVisibilite.getUn(2))) { //si privé
				 if(! utilisateurConnecte.getAmis().contains(daoMedia.getUn(2).getAuteurMedia())) {//et que l'utilisateur n'est pas un ami
					 System.out.println("demande d'un mot de passe"); //demande d'un mot de passe
				 }
				 else { //mais que l'utilisateur est ami
					 System.out.println("demande rien"); //demande rien
				 }
			}
			else {
				detailNotificationJAime = "Merci !";
				detailNotificationJeNAimePas = "Vous n'aimez pas ce média. Merci de votre commentaire !";
			}
		}
		else {
			detailNotificationJAime = "Connectez-vous ou inscrivez-vous dès maintenant !";
			detailNotificationJeNAimePas = "Connectez-vous ou inscrivez-vous dès maintenant !";
		}
	}
	

	/** 
	 * Fonction appelée avant l'affichage de la page
	 * @return
	 */
	public void processRecherche() throws IOException {
		estNotifieJAime = null;
		if(idMediaVisualise == null || idMediaVisualise == "") {
//			FacesContext.getCurrentInstance().getExternalContext().redirect("/MediArea/pages/erreur.jsf"); //redirection vers la page d'erreur
			//TODO redirection vers page media indisponible
		}
		else {
			mediaVisualise = daoMedia.getUn(Long.parseLong(idMediaVisualise));
			if(mediaVisualise == null) { //id de media passé en paramètre GET n'existe pas
//				FacesContext.getCurrentInstance().getExternalContext().redirect("/MediArea/pages/erreur.jsf"); //redirection vers la page d'erreur
			}
		}
		
				//Media
				//mediaVisualise = daoMedia.getUn(2);
						
				titreMedia = mediaVisualise.getTitreMedia();
				
				//nbCommentaires = String.valueOf(mediaVisualise.getCommentaires().size()); //toujours renseigné une chaîne de caractères pour un outputText
				nbCommentaires = mediaVisualise.getCommentaires().size(); //Converter en JSF
				
				auteur = mediaVisualise.getAuteurMedia().getNomUtilisateur();
				
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy H:m");
				datePublication = dateFormat.format(mediaVisualise.getDatePublication());
				
				description = mediaVisualise.getDescriptionMedia();
				
				listeNomCategories = new ArrayList<String>();
				Set<Categorie_Media> setCategoriesMedia = mediaVisualise.getCategories();
				
				Set<Categorie> setCategories = new HashSet<Categorie>();
				
				for (Categorie_Media categorie_Media : setCategoriesMedia) {
					setCategories.add(daoCategorie.getUn(categorie_Media.getCategorie()));
				}
				
			
				Iterator<Categorie> i = setCategories.iterator(); // on crée un Iterator pour parcourir notre Set
				while(i.hasNext()) { // tant qu'on a un suivant
					//System.out.println(i.next().getNomCategorie()); // on affiche le suivant
					listeNomCategories.add(i.next().getNomCategorie());
				}
				//System.out.println("Categories : " + listeNomCategories.toString());
				
				listeNomTags = new ArrayList<String>();
				Set<Tag> setTags = mediaVisualise.getTags();
				Iterator<Tag> tagCompteur=setTags.iterator();
				while(tagCompteur.hasNext()) {
					listeNomTags.add(tagCompteur.next().getNomTag());
				}
				
				chargerCommentaires();
				chargerReponses();
				
				nomTypeMedia = mediaVisualise.getType().getNomTypeMedia();

				showMotDePasseMedia = true;
				
				algorithmeSuggestions();
					
				//***********************************************
				
				//Regarder
				
				resultatTotalVuesMedia = daoMedia.totalVues(mediaVisualise);
				if(resultatTotalVuesMedia > 0) {
					motVues = "s"; //"vues" au pluriel
				}
				
				//***********************************************
				
				resultatTotalTelechargementMedia = daoMedia.totalTelechargement(mediaVisualise);
				
				if(resultatTotalTelechargementMedia > 0) {
					motTelechargement = "s"; //"telechargements" au pluriel
				}
				
				//***********************************************

				//Aimer
				resultatNbAime = daoMedia.nbAimeMedia(mediaVisualise.getIdMedia()).size();
				resultatNbNAimePas = daoMedia.nbAimeNAimePas(mediaVisualise.getIdMedia()).size();
				
				//***********************************************
				
				//Avatar
				nomAvatar = util.getAvatar().getNomAvatar();
				
				//***********************************************
								
				//Playlist
				imgFavori = "add-star-award-icone-8518-16.png";
				Set<Playlist> playlistsUtilisateur = daoUtilisateur.getUn(1).getPlaylists();
				for(Playlist pl : playlistsUtilisateur) {
					if(pl.getType().equals(daoTypePlaylist.getUn(2))) {
						if(pl.getMedias().contains(mediaVisualise)) {
							txtFavori = "Retirer des favoris";
							imgFavori = "star-award-delete-icone-5901-16.png";
						}
						/*else {
							txtFavori = "Favori";
							imgFavori = "add-star-award-icone-8518-16.png";
						}*/
					}
				}
				
				listePlaylistUt = new ArrayList<Playlist>(daoUtilisateur.getUn(1).getPlaylists());
			
				
				Set<Playlist> setPlaylistUt = util.getPlaylists();
				imgAjoutPlaylist = "accepter-check-ok-oui-icone-4851-16.png"; //TODO
				
				for(Playlist playlistUt : setPlaylistUt) {
						if(playlistUt.getMedias().contains(mediaVisualise)) {
							imgAjoutPlaylist = "fermer-croix-supprimer-erreurs-sortie-icone-4368-16.png";
							estAjouteAPlaylist = true;
							break;
						}
				}

				if(!estAjouteAPlaylist) {
					imgAjoutPlaylist = "accepter-check-ok-oui-icone-4851-16.png"; //TODO
				}
				
				listeVisibilite = daoVisibilite.getTous();
				listeNomVisibilite = new ArrayList<SelectItem>();
				SelectItem optionVisibilite;
				for(Visibilite visible : listeVisibilite) {
					optionVisibilite = new SelectItem(visible.getIdVisibilite(), visible.getNomVisibilite(), visible.getNomVisibilite(), false);
					//listeNomVisibilite.add(visible.getNomVisibilite());
					listeNomVisibilite.add(optionVisibilite);
				}
				Collections.reverse(listeNomVisibilite); //inversion pour mettre "Prive" au début
				
				//***********************************************
				
				//Note
				resultatTotalVotesMedia = daoMedia.totalVotes(mediaVisualise);
				if(resultatTotalVotesMedia > 0)	{
					motVotes = "s"; //"vues" au pluriel
				}
				
				//moyenne des notes
				note = daoMedia.moyenneVotes(mediaVisualise); //note initialement affichée
				
				//***********************************************
				
				//Autre
		        //createLinearModel();
				createCategoryModel();
		        
		        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		        url = req.getRequestURL().toString();
		        
		        codeIntegration = "<iframe width='320' height='180' src='" + url + "' frameborder='0' allowfullscreen></iframe>";
	}
	
	
	
	/** 
	 * J'aime
	 * @return
	 */
	public String jAime() {
		System.out.println("Méthode jAime");
		
		//message = new FacesMessage(FacesMessage.SEVERITY_INFO, "J'aime ce contenu", detailNotificationJAime);
		context.addMessage(null, new FacesMessage("J'aime ce contenu", detailNotificationJAime));  
        		
		util.getAimeMedias().add(new Aimer(true,mediaVisualise));
		daoUtilisateur.sauvegarder(util);
		
		estNotifieJAime = "true";
		
		return "jAime";
	}
	
	/** 
	 * Je n'aime pas
	 * @return
	 */
	public String jeNAimePas() {
		System.out.println("Méthode jeNAimePas");
		
		message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Je n'aime pas ce contenu", detailNotificationJeNAimePas);
		
		util.getAimeMedias().add(new Aimer(false,mediaVisualise));
		daoUtilisateur.sauvegarder(util);
		
		return "jeNAimePas";
	}
	
	/** 
	 * Envoi du rapport de signalement du média
	 * @return
	 */
	public String signalerMedia() { //retourné obligatoirement un String et non un void
		System.out.println("Méthode signalerMedia");
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Signalement du média", "Votre rapport a été pris en compte.")); //TODO ne plus afficher ce message lorsque l'on revient sur la pop-up
		
		util.getSignalementsMedias().add(new Signalement_Media(raison, daoMedia.getUn(2)));
		daoUtilisateur.sauvegarder(util);
		
		return "signalerMedia";
	}
	
	/** 
	 * Notation par estimation d'étoiles du média
	 * @return
	 */
	public void handleRate(RateEvent rateEvent) {
		note = ((Double) rateEvent.getRating()).intValue();
		
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Notation", "Votre note : " + note);  
        FacesContext.getCurrentInstance().addMessage(null, message);
        
        vote();
        
    }
	
	/** 
	 * Notation par étoile du média (suite)
	 * @return
	 */
	public String vote() {
		System.out.println("Méthode vote");
		System.out.println("Note : " + note);
		System.out.println("Média : " + mediaVisualise);
		
		util.getNoteMedias().add(new Note((int) note, daoMedia.getUn(2))); //media_idMedia + interdire de noter plusieurs fois TODO
		daoUtilisateur.sauvegarder(util);
				
		return "vote";
	}
	
	/** 
	 * Incrémentation du nombre de vues du média
	 * @return
	 */
	public String incrNbVues() { //incrémenter seulement si clic sur Play et par utilisateur XXX
		System.out.println("Incrémentation du nombre de vues");
						
		/*List<Regarder> listeRegarder = daoRegarder.getTous();
		boolean existeRegMedia = false;
		for(Regarder reg : listeRegarder) {
			if(reg.getMedia() == daoMedia.getUn(2))	{
				System.out.println("Incrémentation (existe)");
				reg.setNbVues(reg.getNbVues() + 1);
				daoRegarder.sauvegarder(reg);
				
				existeRegMedia = true;
				break;
			}
		}
		
		if(!existeRegMedia) { //il crée un objet Regarder correspond au média visualisé
		*/	//System.out.println("Incrémentation (n'existe pas)");
			util.getRegardeMedias().add(new Regarder(daoMedia.getUn(2)));
			daoUtilisateur.sauvegarder(util);
		/*}*/
		
		return "incrNbVues";
	}
	
	/** 
	 * Ajout du média à un favori
	 * @return
	 */
	public String ajouterAFavori() {
		Set<Playlist> playlistsUtilisateur = daoUtilisateur.getUn(1).getPlaylists();
		
		if(txtFavori == "Favori")
		{
			//System.out.println("Ajout au favori");			

			message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Favori", "Ajoutée à Favoris");
			
			Playlist plFavoris = new Playlist();
			
			boolean possedeFavori = false;
			for(Playlist pl : playlistsUtilisateur) {
				//System.out.println("nom playlist : " + pl.getNomPlaylist());
				//vérification si l'utilisateur possède déjà une playlist ayant pour type "Favoris"
				//System.out.println(pl.getType() + " * " + daoTypePlaylist.getUn(2));
				if(pl.getType().equals(daoTypePlaylist.getUn(2))) { //daoTypePlaylist.getUn(2) <=> favoris
					possedeFavori = true;
					plFavoris = pl;
					break;
				}
			}
			
			if(!possedeFavori) { //si la playlist de type Favori n'existe pas
				//création d'une playlist de type Favoris pour l'utilisateur connecté
				plFavoris = new Playlist("Mes favoris2","Favoris","Description",daoTypePlaylist.typeFavoris(),daoVisibilite.typeVisible());
				//daoPlaylist.sauvegarder(plFavoris);
				daoUtilisateur.getUn(1).getPlaylists().add(plFavoris);
				daoUtilisateur.sauvegarder(daoUtilisateur.getUn(1));
			}
			
			plFavoris.getMedias().add(daoMedia.getUn(2)); //ajout du média visualisé à la playlist de type Favori de l'utilisateur
			daoPlaylist.sauvegarder(plFavoris);
			
			txtFavori = "Retirer des favoris";
		}
		else {
			//System.out.println("Retrait au favori");
			
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Favori", "Retirée à Favoris");
			
			for(Playlist pl : playlistsUtilisateur) {
				if(pl.getType().equals(daoTypePlaylist.getUn(2))) { //possède déjà une playlist de type Favori
					pl.getMedias().remove(daoMedia.getUn(2)); //suppression du média visualisé de la liste des favoris
					daoPlaylist.sauvegarder(pl);
				}
			}
		}
		
		return "ajouterAFavori";
	}
	
	/** 
	 * Ajout du média à la playlist
	 * @return
	 */
	public String ajouterMediaAPlaylist() {
		System.out.println("ajouterMediaAPlaylist");
		
		Set<Playlist> setPlaylistUt = util.getPlaylists();
		if(!estAjouteAPlaylist) {
			System.out.println("Ajouter une vidéo à une playlist");
			for(Playlist playlistUt : setPlaylistUt) {
				//System.out.println(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idPlaylist"));
				if(playlistUt.getIdPlaylist() == Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idPlaylist"))) {
					playlistUt.getMedias().add(daoMedia.getUn(2));
					daoPlaylist.sauvegarder(playlistUt);
					break;
				}
			}
		}
		else { //retirer
			System.out.println("Retirer une vidéo à une playlist");
			for(Playlist playlistUt : setPlaylistUt) {
				if(playlistUt.getIdPlaylist() == Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idPlaylist"))) {
					playlistUt.getMedias().remove(daoMedia.getUn(2));
					daoPlaylist.sauvegarder(playlistUt);
					break;
				}
			}

			imgAjoutPlaylist = "accepter-check-ok-oui-icone-4851-16.png"; //TODO
		}
		
		return "ajouterMediaAPlaylist";
	}
	
	/** 
	 * Création d'une playlist
	 * @return
	 */
	public String creerPlaylist() { //TODO à tester
		System.out.println("Création d'une playlist");
		//défaut privée TODO
		
		Playlist nvlPlaylist = new Playlist(nomPlaylistACreer, descriptionPlaylistACreer, "", daoTypePlaylist.typeAutre(), daoVisibilite.typeVisible()); //visibilité TODO
		
		if(!daoUtilisateur.getUn(1).getPlaylists().contains(nvlPlaylist)) {
			daoUtilisateur.getUn(1).getPlaylists().add(nvlPlaylist);
			daoUtilisateur.sauvegarder(daoUtilisateur.getUn(1));
			FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_INFO, "Mission accomplie !", "Ce média a été ajouté à votre playlist: ..."));
		}
		else { //playlist déjà existante
			message = new FacesMessage("La playlist existe déjà");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(
					"...", message);
		}
		
		return "creerPlaylist";
	}

	/** 
	 * Génération du graphique, du nombre de vues du média
	 * @return
	 */
	private void createCategoryModel() {
		categoryModel = new CartesianChartModel();  

		ChartSeries graphiqueVues = new ChartSeries();  
		graphiqueVues.setLabel("Vues totales");

        Query resultatStatVues = daoMedia.statVues(daoMedia.getUn(2));
        //suivant heure et mois TODO
        for(Iterator it = resultatStatVues.iterate();it.hasNext();) {
        	Object[] row = (Object[]) it.next();
        
        	graphiqueVues.set(row[0], Integer.parseInt(row[1].toString()));
        }
        
        maxY = daoMedia.totalVues(daoMedia.getUn(2));

		categoryModel.addSeries(graphiqueVues);
	}
	
	/** 
	 * Désactivation du lecteur iframe (code d'intégration)
	 * @return
	 */
	public void desactiverLecteurIframe(AjaxBehaviorEvent e) {
		System.out.println("desactiverLecteurIframe");
		
		//UIComponent source = (UIComponent)e.getSource();
	    //System.out.println("Value:"+((HtmlSelectBooleanCheckbox)source).getValue());
		//UIInput input = (UIInput) e.getComponent();
		//Object contentValue = (Content) input.getValue();
		//System.out.println("Value : "+contentValue);
		//Object value = ((UIInput) e.getSource()).getSubmittedValue();
		HtmlSelectBooleanCheckbox check=(HtmlSelectBooleanCheckbox)e.getSource();
		Object checkvar = check.getValue();
		System.out.println("Value : "+checkvar); //false true TODO
		
	    if(! Boolean.parseBoolean(checkvar.toString()))	{
			codeIntegration = "<object width='" + largeur + "' height='" + hauteur + "'><param name='movie' value='" + url + "'></param><param name='allowFullScreen' value='true'></param><param name='allowscriptaccess' value='always'></param><embed src='http://www.youtube.com/v/ZQ2nCGawrSY?version=3&amp;hl=fr_FR' type='application/x-shockwave-flash' width='" + largeur + "' height='" + hauteur + "' allowscriptaccess='always' allowfullscreen='true'></embed></object>";	
		}
		else { //!
			codeIntegration = "<iframe width='" + largeur + "' height='" + hauteur + "' src='" + url + "' frameborder='0' allowfullscreen></iframe>";
		}
	}
	
	/** 
	 * Changement de la taille du lecteur (code d'intégration)
	 * @return
	 */
	public void changerTailleLecteur(AjaxBehaviorEvent e) {
		System.out.println("changerTailleLecteur");
		
		if(tailleLecteur.equals("petit")) { //TODO tailleLecteur
        	largeur = 320;
        	hauteur = 180;
        }
        else if(tailleLecteur.equals("moyen")) {
        	largeur = 480;
        	hauteur = 270;
        }
        else if(tailleLecteur.equals("grand")) {
        	largeur = 560;
        	hauteur = 315;
        }
		//taille personnalisée TODO
		System.out.println("/" + tailleLecteur + " ==> " + largeur + "***" + hauteur);
		codeIntegration = "<object width='" + largeur + "' height='" + hauteur + "'><param name='movie' value='" + url + "'></param><param name='allowFullScreen' value='true'></param><param name='allowscriptaccess' value='always'></param><embed src='http://www.youtube.com/v/ZQ2nCGawrSY?version=3&amp;hl=fr_FR' type='application/x-shockwave-flash' width='" + largeur + "' height='" + hauteur + "' allowscriptaccess='always' allowfullscreen='true'></embed></object>";	
		//code object aussi TODO
	}
	
	/** 
	 * Ajout du média au panier
	 * @return
	 */
	public String ajouterAuPanier() {
		System.out.println("ajouterAuPanier");
		
		message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Notification", detailNotifyAjoutAuPanier);
        
		//mediaDansPanier.add(daoMedia.getUn(2));
		if(beanConnexion.getMediaDansPanier().contains(daoMedia.getUn(2))) {
			detailNotifyAjoutAuPanier = "Le média " + daoMedia.getUn(2).getTitreMedia() + " a déjà été ajouté au panier.";
		}
		else {
			beanConnexion.getMediaDansPanier().add(daoMedia.getUn(2));
			detailNotifyAjoutAuPanier = "Le média " + daoMedia.getUn(2).getTitreMedia() + " a été ajouté au panier avec succès !";
		}
		
		//déjà ajouté
		
		return "ajouterAuPanier";
	}
	
	/** 
	 * Décrémentation du nombre de caractères restants (composition d'un commentaire)
	 * @return
	 */
	public String decrementerNbCaracteresRestants() {
		System.out.println("decrementerNbCaracteresRestants");
		
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
		
		Commentaire c = new Commentaire(commentaireSaisi,util);
		daoMedia.getUn(2).getCommentaires().add(c); //mediaVisualise. TODO
		daoMedia.sauvegarder(daoMedia.getUn(2));
		
		/*if(commentaires == null)
		{
			System.out.println("c'est la liste de commentaires qui pose problème (null)");
		}*/
		
		//commentaires.add(c);
		//rechargement de la liste
		chargerCommentaires();
		
		return "publierCommentaire";
	}
	
	/** 
	 * Chargement de la liste de commentaires
	 * @return
	 */
	public void chargerCommentaires() {
		//System.out.println("chargerCommentaires");
		
		// On charge les liste des commentaires
		listeCommentaires = daoMedia.getCommentaires(daoMedia.getUn(2));
	}
	
	/** 
	 * Chargement de la liste des réponses associée à un commentaire
	 * @return
	 */
	public String chargerReponses() {
		//System.out.println("chargerReponses");
				
		Query resultatReponses = daoMedia.getReponses(daoMedia.getUn(2));
		hmReponses = new HashMap<Commentaire, ArrayList<Commentaire>>();
		
		Commentaire pere = null;
		ArrayList<Commentaire> lstFils = new ArrayList<Commentaire>();
		Commentaire tmp = null;
		
        for(Iterator it = resultatReponses.iterate();it.hasNext();) {
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
    		System.out.println("Contenu comm père : " + cle.getContenuCommentaire());
    		System.out.println("Contenu comms fils : " + valeur);
    	}*/
		
		return "chargerReponses";
	}
	
	public ArrayList<Commentaire> mapValue(Commentaire c) {
		return hmReponses.get(c);
	}
	
	/** 
	 * Suppression du commentaire
	 * @return
	 */
	public String supprimerCommentaire() {
		System.out.println("supprimerCommentaire");
		
		message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Suppression du commentaire", "Le commentaire a été supprimé avec succès !");
		
		daoMedia.getUn(2).getCommentaires().remove(daoCommentaire.getUn(Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idCommentaire"))));
		
		daoMedia.sauvegarder(daoMedia.getUn(2));
		
		//rafraîchir TODO
		
		return "supprimerCommentaire";
	}

	/** 
	 * Réponse à un commentaire
	 * @return
	 */
	public String repondreCommentaire() {
		System.out.println("repondreCommentaire");
		
		return "repondreCommentaire";
	}
	
	/** 
	 * Signalement d'un commentaire
	 * @return
	 */
	public String signalerCommentaire() {
		System.out.println("signalerCommentaire");
		
		
		return "signalerCommentaire";
	}
	
	/** 
	 * Vote pour un commentaire
	 * @return
	 */
	public String voterPourCommentaire() {
		System.out.println("voterPour");
		
		
		return "voterPour";
	}

	/** 
	 * Modification des catégories et tags d'un média (si propriétaire)
	 * @return
	 */
	public String modifierCategoriesTags() { //TODO
		System.out.println("===>");
		
		//Set<Categorie> setCategorie = new HashSet<Categorie>();
		/*for(String nomCateg : listeNomCategories)
		{
			setCategorie.add(new Categorie(nomCateg));
		}
		daoMedia.getUn(2).setCategories(setCategorie);
		*/
		//sauvegarder
		
		return "modifierCategoriesTags";
	}


	/** 
	 * Algorithme des suggestions de média
	 * @return
	 */
	public void algorithmeSuggestions() {
		//Tag
		Set<Tag> tagMedia = mediaVisualise.getTags(); //tags du média visualisé
		
		List<Media> listeMedia = daoMedia.getTous();
		
		HashMap<Media, Integer> map = new HashMap<Media, Integer>(); //HashMap contenant le nb d'occurrences de tags correspondants dans les médias
		
		Iterator<Tag> iteratorMedia = tagMedia.iterator();
		while(iteratorMedia.hasNext()) { //parcours des tags du média visualisé
			//System.out.println("Set tagMedia : " + i.next());
			Tag svg = iteratorMedia.next();
			for(Media elMedia : listeMedia) { //parcours de tous les médias
				if(! elMedia.equals(mediaVisualise)) {//tout sauf le média actuellement visualisé
					Set<Tag> setTagMediaCourant = daoMedia.getUn(elMedia.getIdMedia()).getTags();
					for(Tag tagMediaCourant : setTagMediaCourant) {
						//System.out.println(svg + "***" + tagMediaCourant + " (" + elMedia.getIdMedia() + ")");
						if(svg.toString().equals(tagMediaCourant.toString())) {
							if(map.containsKey(elMedia)) {
								map.put(elMedia, map.get(elMedia) + 1); //tag correspond supplémentaire
							}
							else {
								map.put(elMedia, 1);
							}
						}
					}
				}
			}
		}
		
		/*//Affichage de la HashMap
		Set cles = map.keySet();
		Iterator it = cles.iterator();
		while (it.hasNext()){
		   Object cle = it.next();
		   Object valeur = map.get(cle);
		   System.out.println(cle + " => " + valeur);
		}*/

		
		listeMediasSuggeres = new ArrayList<Media>(map.keySet());
		//System.out.println("size listeMediasSuggeres : " + listeMediasSuggeres.size());
		if(listeMediasSuggeres.size() < 20) {
			listeMediasSuggeres = listeMediasSuggeres.subList(0, listeMediasSuggeres.size());
		}
		else {
			listeMediasSuggeres = listeMediasSuggeres.subList(0, 20);
		}
		//suggestion en tenant compte du titre et catégories XXX
	}

	
	
	
	// GETTER / SETTER
	
	public Media getMediaVisualise() {	
		return mediaVisualise;
	}
	
	public void setMediaVisualise(Media mediaVisualise) {
		this.mediaVisualise = mediaVisualise;
	}
	
	public String getTitreMedia() {
		return titreMedia;
	}
	
	public void setTitreMedia(String titreMedia) {
		this.titreMedia = titreMedia;
	}
	
	public long getNbCommentaires() {
		return nbCommentaires;
	}
	
	public void setNbCommentaires(long nbCommentaires) {
		this.nbCommentaires = nbCommentaires;
	}
	
	public String getAuteur() {
		return auteur;
	}
	
	public void setAuteur(String auteur) {
		this.auteur = auteur;
	}
	
	public String getDatePublication() {
		return datePublication;
	}
	
	public void setDatePublication(String datePublication) {
		this.datePublication = datePublication;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}	
	
	public String getRaison() {
		return raison;
	}
	
	public void setRaison(String raison) {
		this.raison = raison;
	}
	
	public String getMotVues() {
		return motVues;
	}
	
	public void setMotVues(String motVues) {
		this.motVues = motVues;
	}
		
	public double getNote() {
		return note;
	}
	
	public void setNote(double note) {
		this.note = note;
	}
	
	public List<String> getListeNomCategories() {
		return listeNomCategories;
	}
	public void setListeNomCategories(List<String> listeNomCategories) {
		this.listeNomCategories = listeNomCategories;
	}
	
	public long getResultatNbAime() {
		return resultatNbAime;
	}
	
	public void setResultatNbAime(long resultatNbAime) {
		this.resultatNbAime = resultatNbAime;
	}
	
	public long getResultatNbNAimePas() {
		return resultatNbNAimePas;
	}
	
	public void setResultatNbNAimePas(long resultatNbNAimePas) {
		this.resultatNbNAimePas = resultatNbNAimePas;
	}
	
	public String getNomTypeMedia() {
		return nomTypeMedia;
	}
	
	public void setNomTypeMedia(String nomTypeMedia) {
		this.nomTypeMedia = nomTypeMedia;
	}
	
	public String getNomAvatar() {
		return nomAvatar;
	}
	
	public void setNomAvatar(String nomAvatar) {
		this.nomAvatar = nomAvatar;
	}
	
	public String getCommentaireSaisi() {
		return commentaireSaisi;
	}
	
	public void setCommentaireSaisi(String commentaireSaisi) {
		this.commentaireSaisi = commentaireSaisi;
	}
	
	public List<Media> getListeMediasSuggeres() {
		return listeMediasSuggeres;
	}
	
	public void setListeMediasSuggeres(List<Media> listeMediasSuggeres) {
		this.listeMediasSuggeres = listeMediasSuggeres;
	}
	
	public List<String> getListeNomTags() {
		return listeNomTags;
	}
	
	public void setListeNomTags(List<String> listeNomTags) {
		this.listeNomTags = listeNomTags;
	}
	
	public String getTxtFavori() {
		return txtFavori;
	}
	
	public void setTxtFavori(String txtFavori) {
		this.txtFavori = txtFavori;
	}
	
	public long getResultatTotalVuesMedia() {
		return resultatTotalVuesMedia;
	}
	
	public void setResultatTotalVuesMedia(long resultatTotalVuesMedia) {
		this.resultatTotalVuesMedia = resultatTotalVuesMedia;
	}
	
	public List<Playlist> getListePlaylistUt() {
		return listePlaylistUt;
	}
	
	public void setListePlaylistUt(List<Playlist> listePlaylistUt) {
		this.listePlaylistUt = listePlaylistUt;
	}
	
	public String getImgAjoutPlaylist() {
		return imgAjoutPlaylist;
	}
	
	public void setImgAjoutPlaylist(String imgAjoutPlaylist) {
		this.imgAjoutPlaylist = imgAjoutPlaylist;
	}
	
	public List<SelectItem> getListeNomVisibilite() {
		return listeNomVisibilite;
	}
	
	public void setListeNomVisibilite(List<SelectItem> listeNomVisibilite) {
		this.listeNomVisibilite = listeNomVisibilite;
	}
	
	public String getDescriptionPlaylistACreer() {
		return descriptionPlaylistACreer;
	}
	
	public void setDescriptionPlaylistACreer(String descriptionPlaylistACreer) {
		this.descriptionPlaylistACreer = descriptionPlaylistACreer;
	}
	
	public String getNomPlaylistACreer() {
		return nomPlaylistACreer;
	}
	
	public void setNomPlaylistACreer(String nomPlaylistACreer) {
		this.nomPlaylistACreer = nomPlaylistACreer;
	}
	
	public String getVisibilitePlaylistACreer() {
		return visibilitePlaylistACreer;
	}
	
	public void setVisibilitePlaylistACreer(String visibilitePlaylistACreer) {
		this.visibilitePlaylistACreer = visibilitePlaylistACreer;
	}
	
	public long getResultatTotalVotesMedia() {
		return resultatTotalVotesMedia;
	}
	
	public void setResultatTotalVotesMedia(long resultatTotalVotesMedia) {
		this.resultatTotalVotesMedia = resultatTotalVotesMedia;
	}
	
	public String getMotVotes() {
		return motVotes;
	}
	
	public void setMotVotes(String motVotes) {
		this.motVotes = motVotes;
	}
	
	/*public CartesianChartModel getLinearModel() {  
	    return linearModel;  
	}*/
	
	public String getCodeIntegration() {
		return codeIntegration;
	}
	
	public void setCodeIntegration(String codeIntegration) {
		this.codeIntegration = codeIntegration;
	}
	
	public String getLien() {
		return lien;
	}
	
	public void setLien(String lien) {
		this.lien = lien;
	}
	
	public String getTailleLecteur() {
		return tailleLecteur;
	}
	
	public void setTailleLecteur(String tailleLecteur) {
		this.tailleLecteur = tailleLecteur;
	}
	
	public String getIdMediaVisualise() {
		return idMediaVisualise;
	}
	
	public void setIdMediaVisualise(String idMediaVisualise) {
		this.idMediaVisualise = idMediaVisualise;
	}
	
	public Utilisateur getUtilisateurConnecte() {
		return utilisateurConnecte;
	}
	
	public void setUtilisateurConnecte(Utilisateur utilisateurConnecte) {
		this.utilisateurConnecte = utilisateurConnecte;
	}
	
	public int getNbCaracteresRestants() {
		return nbCaracteresRestants;
	}
	
	public void setNbCaracteresRestants(int nbCaracteresRestants) {
		this.nbCaracteresRestants = nbCaracteresRestants;
	}
	
	public Commentaire getCommentaireAffiche() {
		return commentaireAffiche;
	}
	
	public void setCommentaireAffiche(Commentaire commentaireAffiche) {
		this.commentaireAffiche = commentaireAffiche;
	}
	
	public long getResultatTotalTelechargementMedia() {
		return resultatTotalTelechargementMedia;
	}
	
	public void setResultatTotalTelechargementMedia(
			long resultatTotalTelechargementMedia) {
		this.resultatTotalTelechargementMedia = resultatTotalTelechargementMedia;
	}
	
	public String getDetailNotifyAjoutAuPanier() {
		return detailNotifyAjoutAuPanier;
	}
	
	public void setDetailNotifyAjoutAuPanier(String detailNotifyAjoutAuPanier) {
		this.detailNotifyAjoutAuPanier = detailNotifyAjoutAuPanier;
	}
	
	public CartesianChartModel getCategoryModel() {  
		return categoryModel;  
	}
    
    public long getMaxY() {
		return maxY;
	}
	public void setMaxY(long maxY) {
		this.maxY = maxY;
	}
	
	public String getEstNotifieJAime() {
		return estNotifieJAime;
	}
	
	public void setEstNotifieJAime(String estNotifieJAime) {
		this.estNotifieJAime = estNotifieJAime;
	}
	
	public String getDetailNotificationJAime() {
		return detailNotificationJAime;
	}
	
	public void setDetailNotificationJAime(String detailNotificationJAime) {
		this.detailNotificationJAime = detailNotificationJAime;
	}
	
	public String getDetailNotificationJeNAimePas() {
		return detailNotificationJeNAimePas;
	}
	
	public void setDetailNotificationJeNAimePas(String detailNotificationJeNAimePas) {
		this.detailNotificationJeNAimePas = detailNotificationJeNAimePas;
	}
	
	public String getImgFavori() {
		return imgFavori;
	}
	
	public void setImgFavori(String imgFavori) {
		this.imgFavori = imgFavori;
	}
	
	public String getMotDePasseMedia() {
		return motDePasseMedia;
	}
	
	public void setMotDePasseMedia(String motDePasseMedia) {
		this.motDePasseMedia = motDePasseMedia;
	}
	
	public boolean isShowMotDePasseMedia() {
		return showMotDePasseMedia;
	}
	
	public void setShowMotDePasseMedia(boolean showMotDePasseMedia) {
		this.showMotDePasseMedia = showMotDePasseMedia;
	}
	
	public List<Commentaire> getListeCommentaires() {
		return listeCommentaires;
	}
	
	public void setListeCommentaires(List<Commentaire> listeCommentaires) {
		this.listeCommentaires = listeCommentaires;
	}
	
	public List<Commentaire> getListeReponses() {
		return listeReponses;
	}
	
	public void setListeReponses(List<Commentaire> listeReponses) {
		this.listeReponses = listeReponses;
	}
	
	public String getMotTelechargement() {
		return motTelechargement;
	}
	
	public void setMotTelechargement(String motTelechargement) {
		this.motTelechargement = motTelechargement;
	}
}
