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
 	
 	// Propri�t�s
	private String idMediaVisualise;
	private Media mediaVisualise;
	private String titreMedia; //n�cessaire de faire appel � l'objet car #{beanMedia.mediaVisualise.titreMedia} ne fonctionne pas
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
	@Size(min = 3, max = 12, message = "La taille du mot de passe doit �tre entre 3 et 12")
	private String motDePasseMedia;
	private long resultatTotalVuesMedia;
	private String motVues;
	private String motTelechargement;
	private long resultatTotalTelechargementMedia;
	//private List<Media> mediaDansPanier; //TODO � mettre en SESSION
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
 	
 	// Utilisateur connect� actuellement
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
		//commentaireSaisi = "R�agir � propos de ce m�dia.";
		
		//estNotifieJAime = false;
		if(utilisateurConnecte != null) {
			if(daoMedia.getUn(2).getVisibilite().equals(daoVisibilite.getUn(2))) { //si priv�
				 if(! utilisateurConnecte.getAmis().contains(daoMedia.getUn(2).getAuteurMedia())) {//et que l'utilisateur n'est pas un ami
					 System.out.println("demande d'un mot de passe"); //demande d'un mot de passe
				 }
				 else { //mais que l'utilisateur est ami
					 System.out.println("demande rien"); //demande rien
				 }
			}
			else {
				detailNotificationJAime = "Merci !";
				detailNotificationJeNAimePas = "Vous n'aimez pas ce m�dia. Merci de votre commentaire !";
			}
		}
		else {
			detailNotificationJAime = "Connectez-vous ou inscrivez-vous d�s maintenant !";
			detailNotificationJeNAimePas = "Connectez-vous ou inscrivez-vous d�s maintenant !";
		}
		
		InputStream stream = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/images/optimusprime.jpg");  
        file = new DefaultStreamedContent(stream, "image/jpg", "downloaded_optimus.jpg"); //TODO chemin vers m�dia � t�l�charger
	}
	

	/** 
	 * Fonction appel�e avant l'affichage de la page
	 * @return
	 */
	public void processRecherche() throws IOException {
		if(idMediaVisualise == null || idMediaVisualise == "") {
//			FacesContext.getCurrentInstance().getExternalContext().redirect("/MediArea/pages/erreur.jsf"); //redirection vers la page d'erreur
			//TODO redirection vers page media indisponible
		}
		else {
			mediaVisualise = daoMedia.getUn(Long.parseLong(idMediaVisualise));
			if(mediaVisualise == null) { //id de media pass� en param�tre GET n'existe pas
//				FacesContext.getCurrentInstance().getExternalContext().redirect("/MediArea/pages/erreur.jsf"); //redirection vers la page d'erreur
			}
		}
		
				//Media
				//mediaVisualise = daoMedia.getUn(2);
						
				titreMedia = mediaVisualise.getTitreMedia();
				
				//nbCommentaires = String.valueOf(mediaVisualise.getCommentaires().size()); //toujours renseign� une cha�ne de caract�res pour un outputText
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
				
			
				categoriesCompteur = setCategories.iterator(); // on cr�e un Iterator pour parcourir notre Set
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
				
				//Chargement des r�ponses
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
				Collections.reverse(listeNomVisibilite); //inversion pour mettre "Prive" au d�but
				
				//***********************************************
				
				//Note
				resultatTotalVotesMedia = daoMedia.totalVotes(mediaVisualise);
				if(resultatTotalVotesMedia > 0)	{
					motVotes = "s"; //"vues" au pluriel
				}
				
				//moyenne des notes
				note = daoMedia.moyenneVotes(mediaVisualise); //note initialement affich�e
				
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
		System.out.println("M�thode jAime");
		
		//Cr�ation de l'objet Aimer (true)
		Aimer a = new Aimer(true,mediaVisualise);
		
		//Ajout � la liste des m�dias aim�s
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
		System.out.println("M�thode jeNAimePas");
		
		//Cr�ation de l'objet Aimer (false)
		Aimer a = new Aimer(false,mediaVisualise);
		
		//Ajout � la liste des m�dias aim�s
		util.getAimeMedias().add(a);
		
		//Sauvegarde de l'ajout
		daoUtilisateur.sauvegarder(util);

		//Affichage de la notification
		context.addMessage(null, new FacesMessage("Je n'aime pas ce contenu", detailNotificationJeNAimePas));
		
		return "jeNAimePas";
	}
	
	/** 
	 * Envoi du rapport de signalement du m�dia
	 * @return
	 */
	public String signalerMedia() { //retourn� obligatoirement un String et non un void
		System.out.println("M�thode signalerMedia");
		
		//Cr�ation du signalement du m�dia
		Signalement_Media sm = new Signalement_Media(raisonMedia, daoMedia.getUn(2));
		
		//Ajout � la liste des signalements du m�dia
		util.getSignalementsMedias().add(sm);
		
		//Sauvegarde de l'ajout
		daoUtilisateur.sauvegarder(util);
		
		//Affichage de la notification
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Signalement du m�dia", "Votre rapport a �t� pris en compte.")); //TODO ne plus afficher ce message lorsque l'on revient sur la pop-up
		
		return "signalerMedia";
	}
	
	/** 
	 * T�l�chargement d'un m�dia
	 * @return
	 */
	public String telechargerMedia() { //TODO rentrer dedans
		System.out.println("m�thode telechargerMedia");
		
		//Cr�ation de l'objet Telechargement_Media
		Telechargement_Media tm = new Telechargement_Media(daoMedia.getUn(2));
		
		//Ajout du t�l�chargement � la liste de m�dias t�l�charg�s
		daoUtilisateur.getUn(1).getTelechargementsMedias().add(tm);
		
		//Sauvegarde de l'ajout
		daoUtilisateur.sauvegarder(daoUtilisateur.getUn(1));
		
		return "telechargerMedia";
	}
	
	/** 
	 * Notation par estimation d'�toiles du m�dia
	 * @return
	 */
	public void handleRate(RateEvent rateEvent) {
		//R�cup�ration de la note assign�e
		note = ((Double) rateEvent.getRating()).intValue();
		
        vote();        

        //Affichage de la notification
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Notation", "Votre note : " + note);  
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
	/** 
	 * Notation par �toile du m�dia (suite)
	 * @return
	 */
	public String vote() {
		System.out.println("M�thode vote");
		
		//Cr�ation de la note assign�e au m�dia
		Note n = new Note((int) note, daoMedia.getUn(2));
		
		//Ajout � la liste des notes
		util.getNoteMedias().add(n); //media_idMedia + interdire de noter plusieurs fois TODO
		
		//Sauvegarde de l'ajout
		daoUtilisateur.sauvegarder(util);
				
		return "vote";
	}
	
	/** 
	 * Incr�mentation du nombre de vues du m�dia
	 * @return
	 */
	public String incrNbVues() { //incr�menter seulement si clic sur Play et par utilisateur XXX
		System.out.println("Incr�mentation du nombre de vues");
						
		/*List<Regarder> listeRegarder = daoRegarder.getTous();
		boolean existeRegMedia = false;
		for(Regarder reg : listeRegarder) {
			if(reg.getMedia() == daoMedia.getUn(2))	{
				System.out.println("Incr�mentation (existe)");
				reg.setNbVues(reg.getNbVues() + 1);
				daoRegarder.sauvegarder(reg);
				
				existeRegMedia = true;
				break;
			}
		}
		
		if(!existeRegMedia) { //il cr�e un objet Regarder correspond au m�dia visualis�
		*/	//System.out.println("Incr�mentation (n'existe pas)");
		
			//Cr�ation de l'objet Regarder
			Regarder r = new Regarder(daoMedia.getUn(2));
			
			//Ajout � la liste des m�dias regard�s
			util.getRegardeMedias().add(r);
			
			//Enregistrement de l'ajout
			daoUtilisateur.sauvegarder(util);
		/*}*/
		
		return "incrNbVues";
	}
	
	/** 
	 * Ajout du m�dia � un favori
	 * @return
	 */
	public String ajouterAFavori() {
		//R�cup�ration des playlists appartenant � l'utilisateur connect�
		playlistsUtilisateur = daoUtilisateur.getUn(1).getPlaylists();
		
		//Ajout au favori
		if(txtFavori == "Favori")
		{
			//System.out.println("Ajout au favori");			

			plFavoris = new Playlist();
			
			boolean possedeFavori = false;
			//Parcours de la liste des playlists de l'utilisateur connect�
			for(Playlist pl : playlistsUtilisateur) {
				//System.out.println("nom playlist : " + pl.getNomPlaylist());
				//System.out.println(pl.getType() + " * " + daoTypePlaylist.getUn(2));

				//V�rification si l'utilisateur poss�de d�j� une playlist ayant pour type "Favoris"
				if(pl.getType().equals(daoTypePlaylist.getUn(2))) { //daoTypePlaylist.getUn(2) <=> favoris
					possedeFavori = true;
					plFavoris = pl;
					break;
				}
			}
			
			if(!possedeFavori) { //Si la playlist de type Favori n'existe pas
				//Cr�ation d'une playlist de type Favoris pour l'utilisateur connect�
				plFavoris = new Playlist("Mes favoris","Favoris","Description",daoTypePlaylist.typeFavoris(),daoVisibilite.typeVisible());

				//Ajout de la playlist de type Favoris � la liste des playlists de l'utilisateur connect�
				daoUtilisateur.getUn(1).getPlaylists().add(plFavoris);
				
				//Enregistrement de l'ajout
				daoUtilisateur.sauvegarder(daoUtilisateur.getUn(1));
			}
			
			//Ajout du m�dia visualis� � la playlist de type Favoris de l'utilisateur
			plFavoris.getMedias().add(daoMedia.getUn(2));
			
			//Sauvegarde de l'ajout
			daoPlaylist.sauvegarder(plFavoris);
			
			//Modification du texte affich� sur la vue
			txtFavori = "Retirer des favoris";

			//Affichage de la notification
			context.addMessage(null, new FacesMessage("Favori", "Ajout�e � Favoris"));			
		}
		else { //Retrait au favori
			//System.out.println("Retrait au favori");
			
			//Parcours de la liste des playlists de l'utilisateur connect�
			for(Playlist pl : playlistsUtilisateur) {
				//Si poss�de d�j� une playlist de type Favori
				if(pl.getType().equals(daoTypePlaylist.getUn(2))) {
					//Suppression du m�dia visualis� de la liste des favoris
					pl.getMedias().remove(daoMedia.getUn(2));
					
					//Enregistrement de la suppression
					daoPlaylist.sauvegarder(pl);
				}
			}
			
			//Affichage de la notification
			context.addMessage(null, new FacesMessage("Favori", "Retir�e � Favoris"));
		}
		
		return "ajouterAFavori";
	}
	
	/** 
	 * Ajout du m�dia � la playlist
	 * @return
	 */
	public String ajouterMediaAPlaylist() {
		System.out.println("ajouterMediaAPlaylist");
		
		//R�cup�ration de la liste des playlists de l'utilisateur connect�
		setPlaylistUt = util.getPlaylists();
		
		//Si la playlist ne contient pas le m�dia
		if(!estAjouteAPlaylist) {
			System.out.println("Ajouter un m�dia � une playlist");
			
			//Parcours de la liste des playlists de l'utilisateur
			for(Playlist playlistUt : setPlaylistUt) {
				//Si la playlist courante parcourue correspond � la playlist � traiter
				if(playlistUt.getIdPlaylist() == Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idPlaylist"))) {
					//Ajout du m�dia � la liste des playlists de l'utilisateur
					playlistUt.getMedias().add(daoMedia.getUn(2));
					
					//Enregistrement de l'ajout
					daoPlaylist.sauvegarder(playlistUt);
					break;
				}
			}
		}
		else { //Retrait d'un m�dia � une playlist
			System.out.println("Retirer un m�dia � une playlist");
			
			//Parcours de la liste des playlists de l'utilisateur
			for(Playlist playlistUt : setPlaylistUt) {
				//Si la playlist courante parcourue correspond � la playlist � traiter
				if(playlistUt.getIdPlaylist() == Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idPlaylist"))) {
					//Suppression du m�dia � la liste des playlists de l'utilisateur
					playlistUt.getMedias().remove(daoMedia.getUn(2));
					
					//Enregistrement de la suppression
					daoPlaylist.sauvegarder(playlistUt);
					break;
				}
			}

			//Changement de l'image affich�e sur la vue
			imgAjoutPlaylist = "accepter-check-ok-oui-icone-4851-16.png"; //TODO
		}
		
		return "ajouterMediaAPlaylist";
	}
	
	/** 
	 * Cr�ation d'une playlist
	 * @return
	 */
	public String creerPlaylist() { //TODO � tester
		System.out.println("Cr�ation d'une playlist");
		
		//Cr�ation de la nouvelle playlist
		nvlPlaylist = new Playlist(nomPlaylistACreer, descriptionPlaylistACreer, "", daoTypePlaylist.typeAutre(), daoVisibilite.typeVisible()); //visibilit� TODO
		
		//Si l'utilisateur ne poss�de pas d�j� cette nouvelle playlist
		if(!daoUtilisateur.getUn(1).getPlaylists().contains(nvlPlaylist)) {
			//Ajout de la nouvelle playlist � la liste des playlists de l'utilisateur
			daoUtilisateur.getUn(1).getPlaylists().add(nvlPlaylist);
			
			//Sauvegarde de l'ajout
			daoUtilisateur.sauvegarder(daoUtilisateur.getUn(1));
			
			//Affichage de la notification
			FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage("Mission accomplie !", "Ce m�dia a �t� ajout� � votre playlist: ..."));
		}
		else { //Si la playlist est d�j� existante
			
			//Affichage de la notification
			message = new FacesMessage("Attention ! La playlist existe d�j�");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(
					"...", message);
		}
		
		return "creerPlaylist";
	}

	/** 
	 * G�n�ration du graphique du nombre de vues du m�dia (ligne)
	 * @return
	 */
	private void creerGraphiqueStatVues() {
		//Cr�ation du graphique
		graphiqueStatVues = new CartesianChartModel();

		//Cr�ation de la s�rie du graphique
		graphiqueVues = new ChartSeries();
		//Affectation du label associ� � la s�rie
		graphiqueVues.setLabel("Vues totales");

		//R�cup�ration des donn�es n�cessaires � la g�n�ration du graphique (requ�te HQL)
        Query resultatStatVues = daoMedia.statVues(daoMedia.getUn(2));
        //suivant heure et mois XXX
        
        //Parcours du SELECT de la requ�te HQL
        for(Iterator<?> it = resultatStatVues.iterate();it.hasNext();) {
        	Object[] row = (Object[]) it.next();
        
        	//Mise en place des donn�es pour la g�n�ration du graphique (date en abscisse et nombre de vues en ordonn�e)
        	graphiqueVues.set(row[0], Integer.parseInt(row[1].toString()));
        }
        
        //Mise en place de l'�chelle des ordonn�es (requ�te HQL)
        maxY = daoMedia.totalVues(daoMedia.getUn(2));

        //Ajout de la s�rie cr��e au graphique
		graphiqueStatVues.addSeries(graphiqueVues);
	}
	
	/** 
	 * G�n�ration du graphique du nombre de J'aime / Je n'aime pas (histogramme horizontal)
	 * @return
	 */
	private void creerGraphiqueAimeNAimePas() {
		//Cr�ation du graphique
		graphiqueAimeNAimePas = new CartesianChartModel();  
  
		//Cr�ation de la s�rie 1 du graphique
        ChartSeries utilisateursAyantAime = new ChartSeries();  
        //Affectation du label associ� � la s�rie 1
        utilisateursAyantAime.setLabel("Utilisateurs ayant aim�");  
  
        //Mise en place des donn�es pour la g�n�ration de la s�rie 1 du graphique
    	utilisateursAyantAime.set("1", resultatNbAime);
  
    	//Cr�ation de la s�rie 2 du graphique
        ChartSeries utilisateursNAyantPasAime = new ChartSeries();
        //Affectation du label associ� � la s�rie 2
        utilisateursNAyantPasAime.setLabel("Utilisateurs n'ayant pas aim�");  
  
        //Mise en place des donn�es pour la g�n�ration de la s�rie 1 du graphique
        utilisateursNAyantPasAime.set("2", resultatNbNAimePas);
  
        //Ajout des 2 s�ries au graphique
        graphiqueAimeNAimePas.addSeries(utilisateursAyantAime);  
        graphiqueAimeNAimePas.addSeries(utilisateursNAyantPasAime);  
    }
	
	/** 
	 * Alimentation du carousel de recommendation de m�dias (suivant l'artiste du m�dia visualis�)
	 * @return
	 */
	public String alimenterCarouselRecommendationMedias() {
		//System.out.println("alimenterCarouselRecommendationMedias");
		
		//R�cup�ration des m�dias recommend�s ayant le m�me artiste que le m�dia visualis� (requ�te HQL)
		carouselRecommendationMedias = daoMedia.recommendationMediasSuivantMediaVisualise(daoMedia.getUn(2));
		
		return "alimenterCarouselRecommendationMedias";
	}
	
	/** 
	 * D�sactivation du lecteur iframe (code d'int�gration)
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
		//R�cup�ration de la valeur du checkbox (pour savoir s'il a �t� coch� ou pas)
		check = (HtmlSelectBooleanCheckbox)e.getSource();
		Object checkvar = check.getValue();
		System.out.println("Value : " + checkvar); //false true TODO
		
		//Si ce n'est pas coch�
	    if(! Boolean.parseBoolean(checkvar.toString()))	{
			codeIntegration = "<iframe width='" + largeur + "' height='" + hauteur + "' src='" + url + "' frameborder='0' allowfullscreen></iframe>";
		}
		else { //Si c'est coch� (Utiliser l'ancien code d'int�gration)
			codeIntegration = "<object width='" + largeur + "' height='" + hauteur + "'><param name='movie' value='" + url + "'></param><param name='allowFullScreen' value='true'></param><param name='allowscriptaccess' value='always'></param><embed src='http://www.youtube.com/v/ZQ2nCGawrSY?version=3&amp;hl=fr_FR' type='application/x-shockwave-flash' width='" + largeur + "' height='" + hauteur + "' allowscriptaccess='always' allowfullscreen='true'></embed></object>";	
		}
	}
	
	/** 
	 * Changement de la taille du lecteur (code d'int�gration)
	 * @return
	 */
	public void changerTailleLecteur(AjaxBehaviorEvent e) {
		System.out.println("changerTailleLecteur");
		
		//Si choix d'un lecteur petit coch�
		if(tailleLecteur.equals("petit")) { //TODO tailleLecteur
        	largeur = 320; //changement de la largeur (code d'int�gration)
        	hauteur = 180; //changement de la hauteur (code d'int�gration)
        }
		//Si choix d'un lecteur moyen coch�
        else if(tailleLecteur.equals("moyen")) {
        	largeur = 480;
        	hauteur = 270;
        }
		//Si choix d'un lecteur grand coch�
        else if(tailleLecteur.equals("grand")) {
        	largeur = 560;
        	hauteur = 315;
        }
		//taille personnalis�e TODO
		System.out.println("/" + tailleLecteur + " ==> " + largeur + "***" + hauteur);
		codeIntegration = "<object width='" + largeur + "' height='" + hauteur + "'><param name='movie' value='" + url + "'></param><param name='allowFullScreen' value='true'></param><param name='allowscriptaccess' value='always'></param><embed src='http://www.youtube.com/v/ZQ2nCGawrSY?version=3&amp;hl=fr_FR' type='application/x-shockwave-flash' width='" + largeur + "' height='" + hauteur + "' allowscriptaccess='always' allowfullscreen='true'></embed></object>";	
		//code object aussi TODO
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
		daoMedia.getUn(2).getCommentaires().add(c); //mediaVisualise. TODO
		
		//Enregistrement de l'ajout
		daoMedia.sauvegarder(daoMedia.getUn(2));
		
		/*if(commentaires == null) {
			System.out.println("c'est la liste de commentaires qui pose probl�me (null)");
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
		
		//Chargement de la liste des commentaires associ� au m�dia
		listeCommentaires = daoMedia.getCommentaires(daoMedia.getUn(2));
	}
	
	/** 
	 * Chargement de la liste des r�ponses associ�e � un commentaire
	 * @return
	 */
	public String chargerReponses() {
		//System.out.println("chargerReponses");
		
		//R�cup�ration de la liste des commentaires r�ponse du m�dia
		resultatReponses = daoMedia.getReponses(daoMedia.getUn(2));
		
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
			daoMedia.getUn(2).getCommentaires().remove(daoCommentaire.getUn(Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idCommentaire"))));
			
			//Enregistrement de la modification
			daoMedia.sauvegarder(daoMedia.getUn(2));
			
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

	/** 
	 * Modification des cat�gories et tags d'un m�dia (si propri�taire)
	 * @return
	 */
	public String modifierCategoriesTags() { //TODO
		System.out.println("m�thode modifierCategoriesTags");
		
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
	 * Algorithme des suggestions de m�dia
	 * @return
	 */
	public void algorithmeSuggestions() {
		//R�cup�ration de la liste de tags associ�s au m�dia
		tagMedia = mediaVisualise.getTags();
		
		//R�cup�ration de tous les m�dias
		listeTousMedia = daoMedia.getTous();
		
		//Cr�ation d'une HashMap contenant le nb d'occurrences de tags correspondants dans les m�dias
		mapOccurrenceTags = new HashMap<Media, Integer>();
		
		iteratorMedia = tagMedia.iterator();
		//Parcours des tags du m�dia visualis�
		while(iteratorMedia.hasNext()) { 
			//System.out.println("Set tagMedia : " + i.next());
			tagMediaCourant = iteratorMedia.next();
			
			//Parcours de tous les m�dias
			for(Media elMedia : listeTousMedia) {
				if(! elMedia.equals(mediaVisualise)) { //tout sauf le m�dia actuellement visualis�
					setTagMediaCourant = daoMedia.getUn(elMedia.getIdMedia()).getTags();
					
					//Parcours des tags associ�s au m�dia
					for(Tag tagMediaCourantAutreMedia : setTagMediaCourant) {
						if(tagMediaCourant.toString().equals(tagMediaCourantAutreMedia.toString())) {
							if(mapOccurrenceTags.containsKey(elMedia)) {
								//Incr�mentation de l'occurrence de tags du m�dia courant
								mapOccurrenceTags.put(elMedia, mapOccurrenceTags.get(elMedia) + 1); //tag correspond suppl�mentaire
							}
							else {
								//Association de 1 au m�dia courant pour la HashMap (la cl� n'existe pas encore)
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
		
		//Cr�ation d'une liste de cl�s
		listeMediasSuggeres = new ArrayList<Media>(mapOccurrenceTags.keySet());
		//System.out.println("size listeMediasSuggeres : " + listeMediasSuggeres.size());
		
		//Limitation � 20 m�dias sugg�r�s
		if(listeMediasSuggeres.size() < 20) {
			//Extraction des premiers �l�ments de la liste des m�dias sugg�r�s
			listeMediasSuggeres = listeMediasSuggeres.subList(0, listeMediasSuggeres.size());
		}
		else {
			//Extraiter des 20 premiers m�dias sugg�r�s
			listeMediasSuggeres = listeMediasSuggeres.subList(0, 20);
		}
		//suggestion en tenant compte du titre et cat�gories XXX
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
