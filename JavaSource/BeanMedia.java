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
import java.io.InputStream;

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
import javax.servlet.ServletContext;

import org.hibernate.Query;
import org.primefaces.event.*;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.DefaultStreamedContent;  
import org.primefaces.model.StreamedContent;
import org.primefaces.model.tagcloud.DefaultTagCloudItem;  
import org.primefaces.model.tagcloud.DefaultTagCloudModel;  
import org.primefaces.model.tagcloud.TagCloudModel;

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
	public static DaoTelechargementMedia daoTelechargementMedia;
	private static DaoUtilisateur daoUtilisateur;
	public static DaoTypePlaylist daoTypePlaylist;
	public static DaoPlaylist daoPlaylist;
	public static DaoVisibilite daoVisibilite;
 	private static DaoCategorie daoCategorie;
 	private static DaoAimer daoAimer;
 	private static DaoRegarder daoRegarder;
 	private static DaoSignalementMedia daoSignalementMedia;
 	
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
	private String reponseSaisie;
	private List<Media> listeMediasSuggeres = new ArrayList<Media>();
	private SimpleDateFormat dateFormat;
	private int nbCaracteresRestants;
	private Commentaire commentaireAffiche;
	@Size(min = 3, max = 12, message = "La taille du mot de passe doit être entre 3 et 12")
	private String motDePasseMedia;
	private long resultatTotalVuesMedia;
	private String motVues;
	private String motTelechargement;
	private long resultatTotalTelechargementMedia;
	//private List<Media> mediaDansPanier; //TODO à mettre en SESSION
	private String raisonMedia;
	private String raisonCommentaire;
	private Utilisateur util;
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
	private CartesianChartModel graphiqueStatVues;
	private CartesianChartModel graphiqueAimeNAimePas;
	private List<?> carouselRecommendationMedias;
	private long maxY;
	private String codeIntegration;
    private String lien;
    private String url;
    private String tailleLecteur = "";
    private int largeur = 320;
    private int hauteur = 180;
 	private String detailNotificationJAime;
 	private String detailNotificationJeNAimePas;
 	private HashMap<Commentaire, ArrayList<Commentaire>> hmReponses;
 	private FacesContext context = FacesContext.getCurrentInstance();
 	private List<Media> listeMediasDeAuteur;
 	private List<Playlist> listePlaylistsAvecMedia;
 	private List<Playlist> listeTousPlaylist;
 	private Set<Categorie_Media> setCategoriesMedia;
 	private Set<Categorie> setCategories;
 	private Iterator<Categorie> categoriesCompteur;
 	private Set<Tag> setTags;
 	private Iterator<Tag> tagCompteur;
 	private Set<Playlist> playlistsUtilisateur;
 	private Set<Playlist> setPlaylistUt;
 	private SelectItem optionVisibilite;
 	private HttpServletRequest req;
 	private Playlist plFavoris;
 	private Playlist nvlPlaylist;
 	private ChartSeries graphiqueVues;
 	private HtmlSelectBooleanCheckbox check;
 	Query resultatReponses;
 	private Commentaire pere;
 	private ArrayList<Commentaire> lstFils;
 	private Set<Tag> tagMedia;
 	private List<Media> listeTousMedia;
 	private Iterator<Tag> iteratorMedia;
 	private HashMap<Media, Integer> mapOccurrenceTags;
 	private Tag tagMediaCourant;
 	private Set<Tag> setTagMediaCourant;
 	private StreamedContent file;
 	private TagCloudModel tagCloud;
 	private boolean estCommentairesAutorise;
 	
 	// Compteur
 	
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
		//commentaireSaisi = "Réagir à propos de ce média.";
		
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
		
		InputStream stream = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/images/optimusprime.jpg");  
        file = new DefaultStreamedContent(stream, "image/jpg", "downloaded_optimus.jpg"); //TODO chemin vers média à télécharger
	}
	

	/** 
	 * Fonction appelée avant l'affichage de la page
	 * @return
	 */
	public void processRecherche() throws IOException {
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
				
				dateFormat = new SimpleDateFormat("dd MMMM yyyy H:m");
				datePublication = dateFormat.format(mediaVisualise.getDatePublication());
				
				description = mediaVisualise.getDescriptionMedia();
				
				estCommentairesAutorise = mediaVisualise.isaCommentairesOuverts();
				
				listeNomCategories = new ArrayList<String>();
				setCategoriesMedia = mediaVisualise.getCategories();
				
				setCategories = new HashSet<Categorie>();
				
				for (Categorie_Media categorie_Media : setCategoriesMedia) {
					setCategories.add(daoCategorie.getUn(categorie_Media.getCategorie()));
				}
				
			
				categoriesCompteur = setCategories.iterator(); // on crée un Iterator pour parcourir notre Set
				while(categoriesCompteur.hasNext()) { // tant qu'on a un suivant
					//System.out.println(categoriesCompteur.next().getNomCategorie()); // on affiche le suivant
					listeNomCategories.add(categoriesCompteur.next().getNomCategorie());
				}
				//System.out.println("Categories : " + listeNomCategories.toString());
				
				listeNomTags = new ArrayList<String>();
				setTags = mediaVisualise.getTags();
				tagCompteur = setTags.iterator();
				tagCloud = new DefaultTagCloudModel();
				Tag tagNext;
				while(tagCompteur.hasNext()) {
					tagNext = tagCompteur.next();
					listeNomTags.add(tagNext.getNomTag());
					tagCloud.addTag(new DefaultTagCloudItem(tagNext.getNomTag(), "recherche.jsf?", (int) Math.random() * 5)); //TODO lien
				}
		        
				//Chargement des commentaires
				chargerCommentaires();
				
				//Chargement des réponses
				chargerReponses();
				
				nomTypeMedia = mediaVisualise.getType().getNomTypeMedia();

				listeMediasDeAuteur = new ArrayList<Media>(daoUtilisateur.getUn(1).getMedias());
				
				listeTousPlaylist = daoPlaylist.getTous();
				listePlaylistsAvecMedia = new ArrayList<Playlist>();
				for (Playlist elPlaylist : listeTousPlaylist) {
					if(elPlaylist.getMedias().contains(daoMedia.getUn(2)))
						listePlaylistsAvecMedia.add(elPlaylist);
				}
				
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
				playlistsUtilisateur = daoUtilisateur.getUn(1).getPlaylists();
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
			
				
				setPlaylistUt = util.getPlaylists();
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
				creerGraphiqueStatVues();
				creerGraphiqueAimeNAimePas();
				alimenterCarouselRecommendationMedias();
		        
		        req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		        url = req.getRequestURL().toString();
		        
		        codeIntegration = "<iframe width='320' height='180' src='" + url + "' frameborder='0' allowfullscreen></iframe>";
	}
	
	
	
	/** 
	 * J'aime
	 * @return
	 */
	public String jAime() {
		System.out.println("Méthode jAime");
		
		//Création de l'objet Aimer (true)
		Aimer a = new Aimer(true,mediaVisualise);
		
		//Ajout à la liste des médias aimés
		util.getAimeMedias().add(a);
		
		//Sauvegarde de l'ajout
		daoUtilisateur.sauvegarder(util);
				
		//Affichage de la notification
		context.addMessage(null, new FacesMessage("J'aime ce contenu", detailNotificationJAime));
		
		return "jAime";
	}
	
	/** 
	 * Je n'aime pas
	 * @return
	 */
	public String jeNAimePas() {
		System.out.println("Méthode jeNAimePas");
		
		//Création de l'objet Aimer (false)
		Aimer a = new Aimer(false,mediaVisualise);
		
		//Ajout à la liste des médias aimés
		util.getAimeMedias().add(a);
		
		//Sauvegarde de l'ajout
		daoUtilisateur.sauvegarder(util);

		//Affichage de la notification
		context.addMessage(null, new FacesMessage("Je n'aime pas ce contenu", detailNotificationJeNAimePas));
		
		return "jeNAimePas";
	}
	
	/** 
	 * Envoi du rapport de signalement du média
	 * @return
	 */
	public String signalerMedia() { //retourné obligatoirement un String et non un void
		System.out.println("Méthode signalerMedia");
		
		//Création du signalement du média
		Signalement_Media sm = new Signalement_Media(raisonMedia, daoMedia.getUn(2));
		
		//Ajout à la liste des signalements du média
		util.getSignalementsMedias().add(sm);
		
		//Sauvegarde de l'ajout
		daoUtilisateur.sauvegarder(util);
		
		//Affichage de la notification
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Signalement du média", "Votre rapport a été pris en compte.")); //TODO ne plus afficher ce message lorsque l'on revient sur la pop-up
		
		return "signalerMedia";
	}
	
	/** 
	 * Téléchargement d'un média
	 * @return
	 */
	public String telechargerMedia() { //TODO rentrer dedans
		System.out.println("méthode telechargerMedia");
		
		//Création de l'objet Telechargement_Media
		Telechargement_Media tm = new Telechargement_Media(daoMedia.getUn(2));
		
		//Ajout du téléchargement à la liste de médias téléchargés
		daoUtilisateur.getUn(1).getTelechargementsMedias().add(tm);
		
		//Sauvegarde de l'ajout
		daoUtilisateur.sauvegarder(daoUtilisateur.getUn(1));
		
		return "telechargerMedia";
	}
	
	/** 
	 * Notation par estimation d'étoiles du média
	 * @return
	 */
	public void handleRate(RateEvent rateEvent) {
		//Récupération de la note assignée
		note = ((Double) rateEvent.getRating()).intValue();
		
        vote();        

        //Affichage de la notification
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Notation", "Votre note : " + note);  
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
	/** 
	 * Notation par étoile du média (suite)
	 * @return
	 */
	public String vote() {
		System.out.println("Méthode vote");
		
		//Création de la note assignée au média
		Note n = new Note((int) note, daoMedia.getUn(2));
		
		//Ajout à la liste des notes
		util.getNoteMedias().add(n); //media_idMedia + interdire de noter plusieurs fois TODO
		
		//Sauvegarde de l'ajout
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
		
			//Création de l'objet Regarder
			Regarder r = new Regarder(daoMedia.getUn(2));
			
			//Ajout à la liste des médias regardés
			util.getRegardeMedias().add(r);
			
			//Enregistrement de l'ajout
			daoUtilisateur.sauvegarder(util);
		/*}*/
		
		return "incrNbVues";
	}
	
	/** 
	 * Ajout du média à un favori
	 * @return
	 */
	public String ajouterAFavori() {
		//Récupération des playlists appartenant à l'utilisateur connecté
		playlistsUtilisateur = daoUtilisateur.getUn(1).getPlaylists();
		
		//Ajout au favori
		if(txtFavori == "Favori")
		{
			//System.out.println("Ajout au favori");			

			plFavoris = new Playlist();
			
			boolean possedeFavori = false;
			//Parcours de la liste des playlists de l'utilisateur connecté
			for(Playlist pl : playlistsUtilisateur) {
				//System.out.println("nom playlist : " + pl.getNomPlaylist());
				//System.out.println(pl.getType() + " * " + daoTypePlaylist.getUn(2));

				//Vérification si l'utilisateur possède déjà une playlist ayant pour type "Favoris"
				if(pl.getType().equals(daoTypePlaylist.getUn(2))) { //daoTypePlaylist.getUn(2) <=> favoris
					possedeFavori = true;
					plFavoris = pl;
					break;
				}
			}
			
			if(!possedeFavori) { //Si la playlist de type Favori n'existe pas
				//Création d'une playlist de type Favoris pour l'utilisateur connecté
				plFavoris = new Playlist("Mes favoris","Favoris","Description",daoTypePlaylist.typeFavoris(),daoVisibilite.typeVisible());

				//Ajout de la playlist de type Favoris à la liste des playlists de l'utilisateur connecté
				daoUtilisateur.getUn(1).getPlaylists().add(plFavoris);
				
				//Enregistrement de l'ajout
				daoUtilisateur.sauvegarder(daoUtilisateur.getUn(1));
			}
			
			//Ajout du média visualisé à la playlist de type Favoris de l'utilisateur
			plFavoris.getMedias().add(daoMedia.getUn(2));
			
			//Sauvegarde de l'ajout
			daoPlaylist.sauvegarder(plFavoris);
			
			//Modification du texte affiché sur la vue
			txtFavori = "Retirer des favoris";

			//Affichage de la notification
			context.addMessage(null, new FacesMessage("Favori", "Ajoutée à Favoris"));			
		}
		else { //Retrait au favori
			//System.out.println("Retrait au favori");
			
			//Parcours de la liste des playlists de l'utilisateur connecté
			for(Playlist pl : playlistsUtilisateur) {
				//Si possède déjà une playlist de type Favori
				if(pl.getType().equals(daoTypePlaylist.getUn(2))) {
					//Suppression du média visualisé de la liste des favoris
					pl.getMedias().remove(daoMedia.getUn(2));
					
					//Enregistrement de la suppression
					daoPlaylist.sauvegarder(pl);
				}
			}
			
			//Affichage de la notification
			context.addMessage(null, new FacesMessage("Favori", "Retirée à Favoris"));
		}
		
		return "ajouterAFavori";
	}
	
	/** 
	 * Ajout du média à la playlist
	 * @return
	 */
	public String ajouterMediaAPlaylist() {
		System.out.println("ajouterMediaAPlaylist");
		
		//Récupération de la liste des playlists de l'utilisateur connecté
		setPlaylistUt = util.getPlaylists();
		
		//Si la playlist ne contient pas le média
		if(!estAjouteAPlaylist) {
			System.out.println("Ajouter un média à une playlist");
			
			//Parcours de la liste des playlists de l'utilisateur
			for(Playlist playlistUt : setPlaylistUt) {
				//Si la playlist courante parcourue correspond à la playlist à traiter
				if(playlistUt.getIdPlaylist() == Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idPlaylist"))) {
					//Ajout du média à la liste des playlists de l'utilisateur
					playlistUt.getMedias().add(daoMedia.getUn(2));
					
					//Enregistrement de l'ajout
					daoPlaylist.sauvegarder(playlistUt);
					break;
				}
			}
		}
		else { //Retrait d'un média à une playlist
			System.out.println("Retirer un média à une playlist");
			
			//Parcours de la liste des playlists de l'utilisateur
			for(Playlist playlistUt : setPlaylistUt) {
				//Si la playlist courante parcourue correspond à la playlist à traiter
				if(playlistUt.getIdPlaylist() == Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idPlaylist"))) {
					//Suppression du média à la liste des playlists de l'utilisateur
					playlistUt.getMedias().remove(daoMedia.getUn(2));
					
					//Enregistrement de la suppression
					daoPlaylist.sauvegarder(playlistUt);
					break;
				}
			}

			//Changement de l'image affichée sur la vue
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
		
		//Création de la nouvelle playlist
		nvlPlaylist = new Playlist(nomPlaylistACreer, descriptionPlaylistACreer, "", daoTypePlaylist.typeAutre(), daoVisibilite.typeVisible()); //visibilité TODO
		
		//Si l'utilisateur ne possède pas déjà cette nouvelle playlist
		if(!daoUtilisateur.getUn(1).getPlaylists().contains(nvlPlaylist)) {
			//Ajout de la nouvelle playlist à la liste des playlists de l'utilisateur
			daoUtilisateur.getUn(1).getPlaylists().add(nvlPlaylist);
			
			//Sauvegarde de l'ajout
			daoUtilisateur.sauvegarder(daoUtilisateur.getUn(1));
			
			//Affichage de la notification
			FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage("Mission accomplie !", "Ce média a été ajouté à votre playlist: ..."));
		}
		else { //Si la playlist est déjà existante
			
			//Affichage de la notification
			message = new FacesMessage("Attention ! La playlist existe déjà");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(
					"...", message);
		}
		
		return "creerPlaylist";
	}

	/** 
	 * Génération du graphique du nombre de vues du média (ligne)
	 * @return
	 */
	private void creerGraphiqueStatVues() {
		//Création du graphique
		graphiqueStatVues = new CartesianChartModel();

		//Création de la série du graphique
		graphiqueVues = new ChartSeries();
		//Affectation du label associé à la série
		graphiqueVues.setLabel("Vues totales");

		//Récupération des données nécessaires à la génération du graphique (requête HQL)
        Query resultatStatVues = daoMedia.statVues(daoMedia.getUn(2));
        //suivant heure et mois XXX
        
        //Parcours du SELECT de la requête HQL
        for(Iterator<?> it = resultatStatVues.iterate();it.hasNext();) {
        	Object[] row = (Object[]) it.next();
        
        	//Mise en place des données pour la génération du graphique (date en abscisse et nombre de vues en ordonnée)
        	graphiqueVues.set(row[0], Integer.parseInt(row[1].toString()));
        }
        
        //Mise en place de l'échelle des ordonnées (requête HQL)
        maxY = daoMedia.totalVues(daoMedia.getUn(2));

        //Ajout de la série créée au graphique
		graphiqueStatVues.addSeries(graphiqueVues);
	}
	
	/** 
	 * Génération du graphique du nombre de J'aime / Je n'aime pas (histogramme horizontal)
	 * @return
	 */
	private void creerGraphiqueAimeNAimePas() {
		//Création du graphique
		graphiqueAimeNAimePas = new CartesianChartModel();  
  
		//Création de la série 1 du graphique
        ChartSeries utilisateursAyantAime = new ChartSeries();  
        //Affectation du label associé à la série 1
        utilisateursAyantAime.setLabel("Utilisateurs ayant aimé");  
  
        //Mise en place des données pour la génération de la série 1 du graphique
    	utilisateursAyantAime.set("1", resultatNbAime);
  
    	//Création de la série 2 du graphique
        ChartSeries utilisateursNAyantPasAime = new ChartSeries();
        //Affectation du label associé à la série 2
        utilisateursNAyantPasAime.setLabel("Utilisateurs n'ayant pas aimé");  
  
        //Mise en place des données pour la génération de la série 1 du graphique
        utilisateursNAyantPasAime.set("2", resultatNbNAimePas);
  
        //Ajout des 2 séries au graphique
        graphiqueAimeNAimePas.addSeries(utilisateursAyantAime);  
        graphiqueAimeNAimePas.addSeries(utilisateursNAyantPasAime);  
    }
	
	/** 
	 * Alimentation du carousel de recommendation de médias (suivant l'artiste du média visualisé)
	 * @return
	 */
	public String alimenterCarouselRecommendationMedias() {
		//System.out.println("alimenterCarouselRecommendationMedias");
		
		//Récupération des médias recommendés ayant le même artiste que le média visualisé (requête HQL)
		carouselRecommendationMedias = daoMedia.recommendationMediasSuivantMediaVisualise(daoMedia.getUn(2));
		
		return "alimenterCarouselRecommendationMedias";
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
		//Récupération de la valeur du checkbox (pour savoir s'il a été coché ou pas)
		check = (HtmlSelectBooleanCheckbox)e.getSource();
		Object checkvar = check.getValue();
		System.out.println("Value : " + checkvar); //false true TODO
		
		//Si ce n'est pas coché
	    if(! Boolean.parseBoolean(checkvar.toString()))	{
			codeIntegration = "<iframe width='" + largeur + "' height='" + hauteur + "' src='" + url + "' frameborder='0' allowfullscreen></iframe>";
		}
		else { //Si c'est coché (Utiliser l'ancien code d'intégration)
			codeIntegration = "<object width='" + largeur + "' height='" + hauteur + "'><param name='movie' value='" + url + "'></param><param name='allowFullScreen' value='true'></param><param name='allowscriptaccess' value='always'></param><embed src='http://www.youtube.com/v/ZQ2nCGawrSY?version=3&amp;hl=fr_FR' type='application/x-shockwave-flash' width='" + largeur + "' height='" + hauteur + "' allowscriptaccess='always' allowfullscreen='true'></embed></object>";	
		}
	}
	
	/** 
	 * Changement de la taille du lecteur (code d'intégration)
	 * @return
	 */
	public void changerTailleLecteur(AjaxBehaviorEvent e) {
		System.out.println("changerTailleLecteur");
		
		//Si choix d'un lecteur petit coché
		if(tailleLecteur.equals("petit")) { //TODO tailleLecteur
        	largeur = 320; //changement de la largeur (code d'intégration)
        	hauteur = 180; //changement de la hauteur (code d'intégration)
        }
		//Si choix d'un lecteur moyen coché
        else if(tailleLecteur.equals("moyen")) {
        	largeur = 480;
        	hauteur = 270;
        }
		//Si choix d'un lecteur grand coché
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
	 * Décrémentation du nombre de caractères restants (composition d'un commentaire)
	 * @return
	 */
	public String decrementerNbCaracteresRestants() {
		System.out.println("decrementerNbCaracteresRestants");
		
		//Décrémentation du nombre de caractères restants pour la saisie du commentaire
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
		
		//Création du commentaire
		Commentaire c = new Commentaire(commentaireSaisi,util);
		
		//Ajout du commentaire à la liste de commentaires du média
		daoMedia.getUn(2).getCommentaires().add(c); //mediaVisualise. TODO
		
		//Enregistrement de l'ajout
		daoMedia.sauvegarder(daoMedia.getUn(2));
		
		/*if(commentaires == null) {
			System.out.println("c'est la liste de commentaires qui pose problème (null)");
		}*/
		
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
		
		//Chargement de la liste des commentaires associé au média
		listeCommentaires = daoMedia.getCommentaires(daoMedia.getUn(2));
	}
	
	/** 
	 * Chargement de la liste des réponses associée à un commentaire
	 * @return
	 */
	public String chargerReponses() {
		//System.out.println("chargerReponses");
		
		//Récupération de la liste des commentaires réponse du média
		resultatReponses = daoMedia.getReponses(daoMedia.getUn(2));
		
		//Création de la HashMap avec en clé le commentaire père et en valeur la liste des commentaires fils
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
    		System.out.println("Contenu comm père : " + cle.getContenuCommentaire());
    		System.out.println("Contenu comms fils : " + valeur);
    	}*/
		
		return "chargerReponses";
	}
	
	/** 
	 * Récupération de la liste des commentaires fils associés au commentaire passé en paramètre
	 * @return
	 */
	public ArrayList<Commentaire> mapValue(Commentaire c) {
		return hmReponses.get(c);
	}
	
	/** 
	 * Nombre de commentaires fils associés au commentaire passé en paramètre
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
		
		// Si celui qui tente de supprimer un commentaire est l'utilisateur connecté
		//if (commSelectionne.getAuteur() == utilisateurConnecte) { //TODO
			//Suppression du commentaire traité de la liste des commentaires du média
			daoMedia.getUn(2).getCommentaires().remove(daoCommentaire.getUn(Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idCommentaire"))));
			
			//Enregistrement de la modification
			daoMedia.sauvegarder(daoMedia.getUn(2));
			
			//Affichage de la notification
			context.addMessage(null, new FacesMessage("Suppression du commentaire", "Le commentaire a été supprimé"));
		//}
		
		//Rafraîchissement des listes
		chargerCommentaires();
		chargerReponses();
		
		//update + 2 min TODO
		
		return "supprimerCommentaire";
	}

	/** 
	 * Réponse à un commentaire
	 * @return
	 */
	public String repondreCommentaire() {
		System.out.println("repondreCommentaire");
		
		// Création d'une nouvelle réponse
		Commentaire c = new Commentaire(reponseSaisie,util);
		
		// Ajout aux réponses du commentaire père
		daoCommentaire.getUn(2).getCommentairesFils().add(c); //TODO bon commentaire
		
		// Sauvegarde de l'ajout
		daoCommentaire.sauvegarder(daoCommentaire.getUn(2));
		
		// Rechargement de la liste de réponses
		chargerReponses();
		
		return "repondreCommentaire";
	}
	
	/** 
	 * Signalement d'un commentaire
	 * @return
	 */
	public String signalerCommentaire() {
		System.out.println("signalerCommentaire");
		
		//interdiction de signaler soi-même
		//if (commentaireSelectionne.getAuteur() != utilisateurConnecte) {
			//Création du signalement
			Signalement_Commentaire sc = new Signalement_Commentaire(raisonCommentaire, daoCommentaire.getUn(2), daoUtilisateur.getUn(1)); //TODOO bon commentaire signalé
		
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

	/** 
	 * Modification des catégories et tags d'un média (si propriétaire)
	 * @return
	 */
	public String modifierCategoriesTags() { //TODO
		System.out.println("méthode modifierCategoriesTags");
		
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
		//Récupération de la liste de tags associés au média
		tagMedia = mediaVisualise.getTags();
		
		//Récupération de tous les médias
		listeTousMedia = daoMedia.getTous();
		
		//Création d'une HashMap contenant le nb d'occurrences de tags correspondants dans les médias
		mapOccurrenceTags = new HashMap<Media, Integer>();
		
		iteratorMedia = tagMedia.iterator();
		//Parcours des tags du média visualisé
		while(iteratorMedia.hasNext()) { 
			//System.out.println("Set tagMedia : " + i.next());
			tagMediaCourant = iteratorMedia.next();
			
			//Parcours de tous les médias
			for(Media elMedia : listeTousMedia) {
				if(! elMedia.equals(mediaVisualise)) { //tout sauf le média actuellement visualisé
					setTagMediaCourant = daoMedia.getUn(elMedia.getIdMedia()).getTags();
					
					//Parcours des tags associés au média
					for(Tag tagMediaCourantAutreMedia : setTagMediaCourant) {
						if(tagMediaCourant.toString().equals(tagMediaCourantAutreMedia.toString())) {
							if(mapOccurrenceTags.containsKey(elMedia)) {
								//Incrémentation de l'occurrence de tags du média courant
								mapOccurrenceTags.put(elMedia, mapOccurrenceTags.get(elMedia) + 1); //tag correspond supplémentaire
							}
							else {
								//Association de 1 au média courant pour la HashMap (la clé n'existe pas encore)
								mapOccurrenceTags.put(elMedia, 1);
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
		
		//Création d'une liste de clés
		listeMediasSuggeres = new ArrayList<Media>(mapOccurrenceTags.keySet());
		//System.out.println("size listeMediasSuggeres : " + listeMediasSuggeres.size());
		
		//Limitation à 20 médias suggérés
		if(listeMediasSuggeres.size() < 20) {
			//Extraction des premiers éléments de la liste des médias suggérés
			listeMediasSuggeres = listeMediasSuggeres.subList(0, listeMediasSuggeres.size());
		}
		else {
			//Extraiter des 20 premiers médias suggérés
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
	
	public String getRaisonMedia() {
		return raisonMedia;
	}
	
	public void setRaisonMedia(String raisonMedia) {
		this.raisonMedia = raisonMedia;
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
	
	public CartesianChartModel getGraphiqueStatVues() {  
		return graphiqueStatVues;  
	}
    
    public long getMaxY() {
		return maxY;
	}
    
	public void setMaxY(long maxY) {
		this.maxY = maxY;
	}
	
	public CartesianChartModel getGraphiqueAimeNAimePas() {
		return graphiqueAimeNAimePas;
	}

	public void setGraphiqueAimeNAimePas(CartesianChartModel graphiqueAimeNAimePas) {
		this.graphiqueAimeNAimePas = graphiqueAimeNAimePas;
	}

	public List<?> getCarouselRecommendationMedias() {
		return carouselRecommendationMedias;
	}

	public void setCarouselRecommendationMedias(
			List<?> carouselRecommendationMedias) {
		this.carouselRecommendationMedias = carouselRecommendationMedias;
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

	public List<Media> getListeMediasDeAuteur() {
		return listeMediasDeAuteur;
	}

	public void setListeMediasDeAuteur(List<Media> listeMediasDeAuteur) {
		this.listeMediasDeAuteur = listeMediasDeAuteur;
	}


	public List<Playlist> getListePlaylistsAvecMedia() {
		return listePlaylistsAvecMedia;
	}
	
	public void setListePlaylistsAvecMedia(List<Playlist> listePlaylistsAvecMedia) {
		this.listePlaylistsAvecMedia = listePlaylistsAvecMedia;
	}
	
	public TagCloudModel getTagCloud() {  
        return tagCloud;  
    }
	
	public String getRaisonCommentaire() {
		return raisonCommentaire;
	}

	public void setRaisonCommentaire(String raisonCommentaire) {
		this.raisonCommentaire = raisonCommentaire;
	}

	public String getReponseSaisie() {
		return reponseSaisie;
	}

	public void setReponseSaisie(String reponseSaisie) {
		this.reponseSaisie = reponseSaisie;
	}

	public boolean isEstCommentairesAutorise() {
		return estCommentairesAutorise;
	}

	public void setEstAutoriseCommentaires(boolean estAutoriseCommentaires) {
		this.estCommentairesAutorise = estAutoriseCommentaires;
	}

	public StreamedContent getFile() {  
        return file;  
    }
}
