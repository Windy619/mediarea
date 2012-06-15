import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.io.InputStream;

import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Size;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;

import org.hibernate.Query;
import org.primefaces.event.*;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
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
	public static DaoTelechargementMedia daoTelechargementMedia;
	private static DaoUtilisateur daoUtilisateur;
	public static DaoTypePlaylist daoTypePlaylist;
	public static DaoPlaylist daoPlaylist;
	public static DaoVisibilite daoVisibilite;
 	private static DaoCategorie daoCategorie;
 	private static DaoAimer daoAimer;
 	private static DaoRegarder daoRegarder;
 	
 	// Propri�t�s
	private String idMediaVisualise;
	private Media mediaVisualise;
	private String titreMedia; //n�cessaire de faire appel � l'objet car #{beanMedia.mediaVisualise.titreMedia} ne fonctionne pas
	private String auteur;
	private String datePublication;
	private String description;
	private List<String> listeNomCategories;
	private List<String> listeNomTags;
	//private DataModel dataModel = new ListDataModel();
	private double note;
	private String nomTypeMedia;
	private SimpleDateFormat dateFormat;
	@Size(min = 3, max = 12, message = "La taille du mot de passe doit �tre entre 3 et 12")
	private String motDePasseMedia;
	private long resultatTotalVuesMedia;
	private String motVues;
	private String motTelechargement;
	private long resultatTotalTelechargementMedia;
	private String raisonMedia;
	private long resultatNbAime;
	private long resultatNbNAimePas;
	private String txtFavori;
	//private String imgFavori;
	private List<Playlist> listePlaylistUt;
	//private String imgAjoutPlaylist;
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
    private String tailleLecteur;
    private int largeur;
    private int hauteur;
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
 	private StreamedContent file;
 	private TagCloudModel tagCloud;
 	private Playlist playlistSelectionnee;
 	private boolean jAimeDisabled;
 	private boolean jeNAimePasDisabled;
 	private String paramUrl;
 	
 	// Compteur
 	
    // Bean
 	private BeanConnexion beanConnexion;
 	
 	// Utilisateur connect� actuellement
 	private Utilisateur utilisateurConnecte;
 		
 	
 	/**
	 * Constructeur du Bean
	 */
	public BeanMedia() {
		// Instantiation des Dao
		daoMedia = new DaoMedia();
		daoCategorie = new DaoCategorie();
		daoUtilisateur = new DaoUtilisateur();
		daoRegarder = new DaoRegarder();
		daoUtilisateur = new DaoUtilisateur();
		daoAimer = new DaoAimer();
		daoTypePlaylist = new DaoTypePlaylist();
		daoPlaylist = new DaoPlaylist();
		daoVisibilite = new DaoVisibilite();
		daoTelechargementMedia = new DaoTelechargementMedia();
		
		codeIntegration = "";
		tailleLecteur = "";
		
		if(utilisateurConnecte != null)
			mettreEnPlaceFavoris();
		else
			txtFavori = "Favori";

		if(utilisateurConnecte != null)
			mettreEnPlacePlaylists();
	}
		
	/** 
	 * Fonction appel�e avant l'affichage de la page
	 * @return
	 */
	public void processMediaPre() throws IOException {
		// Si l'identifiant du m�dia visualis� pass� en param�tre dans l'URL est nul
		if(idMediaVisualise == null) {
			System.out.println("redirection idMediaVisualise null");
			FacesContext.getCurrentInstance().getExternalContext().redirect("/MediArea/pages/erreur.jsf"); //redirection vers la page d'erreur
			redirigerErreur(); //TODO
			//redirection vers page media indisponible XXX
		}
		else {
			mediaVisualise = daoMedia.getUn(Long.parseLong(idMediaVisualise));
			// Si l'identifiant du m�dia visualis� pass� en param�tre dans l'URL ne correspond � aucun m�dia
			if (mediaVisualise == null) {
				System.out.println("redirection mediaVisualise null");
				// Redirection vers la page d'erreur
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect("/MediArea/pages/erreur.jsf");
			}
		}
		
		// Chargement de l'utilisateur connecte
		beanConnexion = (BeanConnexion) FacesContext.getCurrentInstance().getCurrentInstance().getExternalContext().getSessionMap().get("beanConnexion");
		//System.out.println("Bean connexion : " + beanConnexion);
			
		if (beanConnexion != null) {
			//R�cup�ration des informations de l'utilisateur connect�
			utilisateurConnecte = beanConnexion.getUser();
			//if (utilisateurConnecte != null) {}
				
			//System.out.println("Utilisateur connect� : " + utilisateurConnecte);
		}
		

		// --- Media ---

		titreMedia = mediaVisualise.getTitreMedia();

		auteur = mediaVisualise.getAuteurMedia().getNomUtilisateur();

		dateFormat = new SimpleDateFormat("dd MMMM yyyy H:m");
		datePublication = dateFormat.format(mediaVisualise.getDatePublication());

		description = mediaVisualise.getDescriptionMedia();

		mettreEnPlaceCategories();

		mettreEnPlaceTags();

		nomTypeMedia = mediaVisualise.getType().getNomTypeMedia();

		if (utilisateurConnecte != null)
			listeMediasDeAuteur = new ArrayList<Media>(utilisateurConnecte.getMedias());

		listeTousPlaylist = daoPlaylist.getTous();
		listePlaylistsAvecMedia = new ArrayList<Playlist>();
		for (Playlist elPlaylist : listeTousPlaylist) {
			if (elPlaylist.getMedias().contains(mediaVisualise))
				listePlaylistsAvecMedia.add(elPlaylist);
		}

		// Incr�mentation du nombre de vues lorsque l'on acc�de � la page
		incrNbVues();

		InputStream stream = ((ServletContext) FacesContext
				.getCurrentInstance().getExternalContext().getContext())
				.getResourceAsStream("C:/Users/Public/Pictures/Sample Pictures/Hortensias.jpg");
		/*System.out.println("Nom fichier : "
				+ mediaVisualise.getFichier().getNomFichier());
		file = new DefaultStreamedContent(stream, "images/jpg",
				"downloaded_file.jpg"); // TODO chemin vers m�dia � t�l�charger
		*/
		
		resultatTotalTelechargementMedia = daoMedia.totalTelechargement(mediaVisualise);
		if (resultatTotalTelechargementMedia > 1) {
			System.out.println("Nb de t�l�chargements : " + resultatTotalTelechargementMedia);
			motTelechargement = "s"; // "telechargements" au pluriel
		}
		
		
		// --- Regarder ---

		resultatTotalVuesMedia = daoMedia.totalVues(mediaVisualise);
		if (resultatTotalVuesMedia > 1) {
			motVues = "s"; // "vues" au pluriel
		}
		
		// --- Aimer ---

		if(utilisateurConnecte != null)
			mettreEnPlaceAimeAimePas();

		// --- Playlists ---

		listeVisibilite = daoVisibilite.getTous();
		listeNomVisibilite = new ArrayList<SelectItem>();
		for (Visibilite visible : listeVisibilite) {
			optionVisibilite = new SelectItem(visible.getIdVisibilite(),
					visible.getNomVisibilite(), visible.getNomVisibilite(),
					false);
			// listeNomVisibilite.add(visible.getNomVisibilite());
			listeNomVisibilite.add(optionVisibilite);
		}
		Collections.reverse(listeNomVisibilite); // inversion pour mettre "Prive" au d�but

		
		// --- Note ---
		
		resultatTotalVotesMedia = daoMedia.totalVotes(mediaVisualise);
		if (resultatTotalVotesMedia > 1) {
			motVotes = "s"; // "vues" au pluriel
		}

		// Moyenne des notes
		note = daoMedia.moyenneVotes(mediaVisualise); // note initialement affich�e

		
		// --- Autre ---
		creerGraphiqueStatVues();
		creerGraphiqueAimeNAimePas();
		alimenterCarouselRecommendationMedias();

		req = (HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest();
		url = req.getRequestURL().toString();

		if(codeIntegration.equals("")) {
			codeIntegration = "<iframe width='320' height='180' src='" + url
				+ "' frameborder='0' allowfullscreen></iframe>";
		}
		
		if(tailleLecteur.equals("")) {
			System.out.println("Taille lecteur vide => petit");
			tailleLecteur = "petit";
		}
		
		/*if(largeur == 0) {
			largeur = 320;
		}
		
		if(hauteur == 0) {
			hauteur = 180;
		}*/
		
		paramUrl = ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameter("v"); 
		
		processMedia();
	}
	
	/** 
	 * Redirection vers la page erreur
	 * @return
	 */
	public String redirigerErreur() {
		return "/pages/erreur?faces-redirect=true&amp;includeViewParams=true";
	}
	
	/** 
	 * M�thode permettant de garder le param�tre ?v=...
	 * @return
	 */
	public String processMedia() {
		return "/pages/detailMedia?faces-redirect=true&amp;includeViewParams=true";
	}
	
	/** 
	 * Mise en place des informations relatives � la cat�gorie
	 * @return
	 */
	public String mettreEnPlaceCategories() {
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
		
		return "mettreEnPlaceCategories";
	}
	
	/** 
	 * Mise en place des informations relatives aux tags
	 * @return
	 */
	public String mettreEnPlaceTags() {
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
		
		return "mettreEnPlaceTags";
	}
	
	/** 
	 * Mise en place des informations relatives � l'appr�ciation du m�dia visionn�
	 * @return
	 */
	public String mettreEnPlaceAimeAimePas() {
		resultatNbAime = daoMedia.nbAimeMedia(mediaVisualise.getIdMedia()).size();
		resultatNbNAimePas = daoMedia.nbAimeNAimePas(mediaVisualise.getIdMedia()).size();
		
		// D�sactivation de "J'aime" si l'utilisateur aime d�j� ce m�dia
		if (utilisateurConnecte.getAimeMedias().toString().equals("[]")) {
			jAimeDisabled = false;
			jeNAimePasDisabled = true;
		}
		else
		{
			for (Aimer aimerUt : utilisateurConnecte.getAimeMedias()) {
				if(aimerUt.isaAime() && aimerUt.getMedia().equals(mediaVisualise)) {
					//System.out.println("activer J'aime");
					jAimeDisabled = true;
					jeNAimePasDisabled = false;
					break;
				}
				else  {
					//System.out.println("activer Je n'aime pas");
					jAimeDisabled = false;
					jeNAimePasDisabled = true;
					break;
				}
			}
		}
		
		return "mettreEnPlaceAimeAimePas";
	}
	
	/** 
	 * Mise en place des favoris
	 * @return
	 */
	public String mettreEnPlaceFavoris() {
		//imgFavori = "add-star-award-icone-8518-16.png";
		//System.out.println("Playlist utilisateur : " + playlistsUtilisateur);
		playlistsUtilisateur = utilisateurConnecte.getPlaylists();
		if(playlistsUtilisateur.toString().equals("[]")) {
			txtFavori = "Favori";
		}
		else {
			boolean existeFavori = false;
			for(Playlist pl : playlistsUtilisateur) {
				if(pl.getType().equals(daoTypePlaylist.getUn(2))) {
					for (Media mediaPl : pl.getMedias()) {
						if(mediaPl.equals(mediaVisualise)) {
							System.out.println("Retrait des favoris (pre)");
							txtFavori = "Retirer des favoris";
							existeFavori = true;
							break;
						}
					}
				}
			}
			
			if(! existeFavori) {
				System.out.println("Ajout aux favoris (pre)");
				txtFavori = "Favori";
			}
		}
		
		return "mettreEnPlaceFavoris";
	}
	
	/** 
	 * Mise en place des playlists
	 * @return
	 */
	public String mettreEnPlacePlaylists() {
		System.out.println("m�thode mettreEnPlacePlaylists"); 
		listePlaylistUt = new ArrayList<Playlist>(utilisateurConnecte.getPlaylists());
		
		setPlaylistUt = utilisateurConnecte.getPlaylists();
		
		for(Playlist playlistUt : setPlaylistUt) {
				if(playlistUt.getMedias().contains(mediaVisualise)) {
					//imgAjoutPlaylist = "fermer-croix-supprimer-erreurs-sortie-icone-4368-16.png";
					estAjouteAPlaylist = true;
					break;
				}
		}

		if(! estAjouteAPlaylist) {
			//imgAjoutPlaylist = "accepter-check-ok-oui-icone-4851-16.png";
		}
		
		return "mettreEnPlacePlaylists";
	}
	
	/** 
	 * J'aime
	 * @return
	 */
	public String jAime() {
		System.out.println("M�thode jAime");
		
		if(utilisateurConnecte != null) {
			// Si l'utilisateur a d�j� aim� ou pas aim�
			boolean existeAimer = false;
			for (Aimer aimerUt : utilisateurConnecte.getAimeMedias()) {
				if(aimerUt.getMedia().equals(mediaVisualise)) { //Aimer de l'utilisateur du m�dia visualis�
					// On met � true aAime de l'objet Aimer d�j� existant
					aimerUt.setaAime(true);
					existeAimer = true;
					
					// Sauvegarde de la modification
					daoAimer.sauvegarder(aimerUt);
					break;
				}
			}
			
			if(! existeAimer) {
				// Cr�ation de l'objet Aimer (true)
				Aimer a = new Aimer(true, mediaVisualise);
						
				// Ajout � la liste des m�dias aim�s
				utilisateurConnecte.getAimeMedias().add(a);
				
				// Sauvegarde de l'ajout
				daoUtilisateur.sauvegarder(utilisateurConnecte);
			}
			
			// Pr�paration du message de la notification
			message = new FacesMessage("J'aime ce contenu : Merci !");
			
			// D�sactivation de "J'aime"
			jAimeDisabled = true;
			
			// Activation de "Je n'aime pas"
			jeNAimePasDisabled = false;
		}
		else {
			// Pr�paration du message de la notification
			message = new FacesMessage("J'aime ce contenu : Connectez-vous ou inscrivez-vous d�s maintenant !");
			message.setSeverity(FacesMessage.SEVERITY_WARN);
		}

		// Affichage de la notification
		FacesContext.getCurrentInstance().addMessage(null, message);
		
		return "jAime";
	}
	
	/** 
	 * Je n'aime pas
	 * @return
	 */
	public String jeNAimePas() {
		System.out.println("M�thode jeNAimePas");
		
		message = null;
		
		if(utilisateurConnecte != null) {
			//Si l'utilisateur a d�j� aim� ou pas aim�
			boolean existeAimer = false;
			for (Aimer aimerUt : utilisateurConnecte.getAimeMedias()) {
				if(aimerUt.getMedia().equals(mediaVisualise)) { //Aimer de l'utilisateur du m�dia visualis�
					// On met � false aAime de l'objet Aimer d�j� existant
					aimerUt.setaAime(false);
					existeAimer = true;
						
					// Sauvegarde de la modification
					daoAimer.sauvegarder(aimerUt);
					break;
				}
			}
			
			if(! existeAimer) {
				// Cr�ation de l'objet Aimer (false)
				Aimer a = new Aimer(false,mediaVisualise);
				
				// Ajout � la liste des m�dias aim�s
				utilisateurConnecte.getAimeMedias().add(a);
				
				// Sauvegarde de l'ajout
				daoUtilisateur.sauvegarder(utilisateurConnecte);
			}
			
			// Pr�paration du message de la notification
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Je n'aime pas ce contenu : Vous n'aimez pas ce m�dia. Merci de votre commentaire !"));
			
			// D�sactivation de "Je n'aime pas"
			jeNAimePasDisabled = true;
			
			// Activation de "J'aime"
			jAimeDisabled = false;
		}
		else {
			// Pr�paration du message de la notification
			message = new FacesMessage("Je n'aime ce contenu : Connectez-vous ou inscrivez-vous d�s maintenant !");
			message.setSeverity(FacesMessage.SEVERITY_WARN);
		}		

		// Affichage de la notification
		FacesContext.getCurrentInstance().addMessage(null, message);
			
		return "jeNAimePas";
	}
	
	/** 
	 * Envoi du rapport de signalement du m�dia
	 * @return
	 */
	public String signalerMedia() { //retourn� obligatoirement un String et non un void
		System.out.println("M�thode signalerMedia");

		// Cr�ation du signalement du m�dia
		Signalement_Media sm = new Signalement_Media(raisonMedia, mediaVisualise);
			
		// Ajout � la liste des signalements du m�dia
		utilisateurConnecte.getSignalementsMedias().add(sm);
			
		// Sauvegarde de l'ajout
		daoUtilisateur.sauvegarder(utilisateurConnecte);
			
		// Affichage de la notification
		message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Signalement du m�dia :" , "Votre rapport a �t� pris en compte.");
			
		// Cr�ation de la notification
		Notification notification = new Notification("Votre m�dia : \"" + mediaVisualise.getTitreMedia() + "\" a fait l'objet d'un signalement !", mediaVisualise.getAuteurMedia());
		notification.setDateEnvoiNotification(new Date());
		// On l'ajoute � l'utilisateur concern� et on le sauvegarde
		mediaVisualise.getAuteurMedia().getNotifications().add(notification);
		daoUtilisateur.sauvegarder(mediaVisualise.getAuteurMedia());
		
		return "signalerMedia";
	}
	
	/** 
	 * T�l�chargement d'un m�dia
	 * @return
	 */
	public String telechargerMedia() {
		System.out.println("m�thode telechargerMedia");
		
		// Cr�ation de l'objet Telechargement_Media
		Telechargement_Media tm = new Telechargement_Media(mediaVisualise);
		
		// Ajout du t�l�chargement � la liste de m�dias t�l�charg�s
		utilisateurConnecte.getTelechargementsMedias().add(tm);
		
		// Sauvegarde de l'ajout
		daoUtilisateur.sauvegarder(utilisateurConnecte);
		
		return "telechargerMedia";
	}
	
	/** 
	 * Notation par estimation d'�toiles du m�dia
	 * @return
	 */
	public void handleRate(RateEvent rateEvent) {
		if(utilisateurConnecte != null) {
			// R�cup�ration de la note assign�e
			note = ((Double) rateEvent.getRating()).intValue();
			
	        vote();
	
	        // Pr�paration du message de la notification
	        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Notation :", "Votre note : " + note); 
		}
		else {
			// Pr�paration du message de la notification
			message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Notation :", "Connectez-vous ou inscrivez-vous d�s maintenant !");
		}

        // Affichage de la notification
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
	/** 
	 * Notation par �toile du m�dia (suite)
	 * @return
	 */
	public String vote() {
		System.out.println("M�thode vote");
		
		// Cr�ation de la note assign�e au m�dia
		Note n = new Note((int) note, mediaVisualise);
		
		// Ajout � la liste des notes
		utilisateurConnecte.getNoteMedias().add(n); //media_idMedia + interdire de noter plusieurs fois TODO
		
		// Sauvegarde de l'ajout
		daoUtilisateur.sauvegarder(utilisateurConnecte);
		
		return "vote";
	}
	
	/** 
	 * Incr�mentation du nombre de vues du m�dia
	 * @return
	 */
	public String incrNbVues() { //incr�menter seulement si clic sur Play et par utilisateur XXX
		//System.out.println("Incr�mentation du nombre de vues");
						
		/*List<Regarder> listeRegarder = daoRegarder.getTous();
		boolean existeRegMedia = false;
		for(Regarder reg : listeRegarder) {
			if(reg.getMedia() == mediaVisualise)	{
				System.out.println("Incr�mentation (existe)");
				reg.setNbVues(reg.getNbVues() + 1);
				daoRegarder.sauvegarder(reg);
				
				existeRegMedia = true;
				break;
			}
		}
		
		if(!existeRegMedia) { //il cr�e un objet Regarder correspond au m�dia visualis�
		*/	//System.out.println("Incr�mentation (n'existe pas)");
		
			// Cr�ation de l'objet Regarder
			Regarder r = new Regarder(mediaVisualise);
			daoRegarder.sauvegarder(r);
			
			if(utilisateurConnecte != null)	{
				// Ajout � la liste des m�dias regard�s
				utilisateurConnecte.getRegardeMedias().add(r);
				
				// Enregistrement de l'ajout
				daoUtilisateur.sauvegarder(utilisateurConnecte);
			}
		/*}*/
		
		return "incrNbVues";
	}
	
	/** 
	 * Ajout du m�dia � un favori
	 * @return
	 */
	public String ajouterAFavori() {
		// R�cup�ration des playlists appartenant � l'utilisateur connect�
		playlistsUtilisateur = utilisateurConnecte.getPlaylists();
			
		// Ajout au favori
		if(txtFavori.equals("Favori"))
		{
			System.out.println("Ajout au favori");			
	
			plFavoris = new Playlist();
				
			boolean possedeFavori = false;
			// Parcours de la liste des playlists de l'utilisateur connect�
			for(Playlist pl : playlistsUtilisateur) {
				//System.out.println("nom playlist : " + pl.getNomPlaylist());
				//System.out.println(pl.getType() + " * " + daoTypePlaylist.getUn(2));
	
				// V�rification si l'utilisateur poss�de d�j� une playlist ayant pour type "Favoris"
				if(pl.getType().equals(daoTypePlaylist.getUn(2))) { //daoTypePlaylist.getUn(2) <=> favoris
					possedeFavori = true;
					plFavoris = pl;
					break;
				}
			}
				
			if(!possedeFavori) { // Si la playlist de type Favori n'existe pas
				// Cr�ation d'une playlist de type Favoris pour l'utilisateur connect�
				plFavoris = new Playlist("Mes favoris","Favoris","Description",daoTypePlaylist.typeFavoris(),daoVisibilite.typeVisible());
	
				// Ajout de la playlist de type Favoris � la liste des playlists de l'utilisateur connect�
				utilisateurConnecte.getPlaylists().add(plFavoris);
				
				// Enregistrement de l'ajout
				daoUtilisateur.sauvegarder(utilisateurConnecte);
			}
				
			// Ajout du m�dia visualis� � la playlist de type Favoris de l'utilisateur
			plFavoris.getMedias().add(mediaVisualise);
				
			// Sauvegarde de l'ajout
			daoPlaylist.sauvegarder(daoPlaylist.getUn(plFavoris.getIdPlaylist()));			
			
			// Modification du texte affich� sur la vue
			txtFavori = "Retirer des favoris";
	
			// Pr�paration du message de la notification
			message = new FacesMessage("Favori : Ajout�e � Favoris");			
		}
		else { // Retrait au favori
			System.out.println("Retrait au favori");
				
			// Parcours de la liste des playlists de l'utilisateur connect�
			for(Playlist pl : playlistsUtilisateur) {
				// Si poss�de d�j� une playlist de type Favori
				if(pl.getType().equals(daoTypePlaylist.getUn(2))) {
					// Suppression du m�dia visualis� de la liste des favoris
					pl.getMedias().remove(mediaVisualise);
						
					// Enregistrement de la suppression
					daoPlaylist.sauvegarder(pl);
				}
			}
				
			// Modification du texte affich� sur la vue
			txtFavori = "Favori";
			
			// Pr�paration du message de la notification
			message = new FacesMessage("Favori : Retir�e � Favoris");
		}		

		// Affichage de la notification
		FacesContext.getCurrentInstance().addMessage(null, message);
		
		return "ajouterAFavori";
	}
	
	/** 
	 * M�thode permettant de savoir si le m�dia visualis� est ajout� ou non � la playlist s�lectionn�e
	 * @return
	 */	
	public boolean getEstAjouteAPlaylist(Playlist play) {
		boolean estAjouterAPlaylistSelectionnee = false;
		
		for(Playlist playlistUt : setPlaylistUt) {
			if(playlistUt.equals(play) && playlistUt.getMedias().contains(mediaVisualise)) {
				// Le m�dia visualis� appartient � la playlist s�lectionn�e
				estAjouterAPlaylistSelectionnee = true;
				break;
			}
		}
		
		return estAjouterAPlaylistSelectionnee;
	}
	
	/** 
	 * Ajout du m�dia � la playlist
	 * @return
	 */
	public String ajouterMediaAPlaylist() {
		//System.out.println("ajouterMediaAPlaylist");	

		//System.out.println("Playlist s�lectionn�e : " + playlistSelectionnee);
		
		// R�cup�ration de la liste des playlists de l'utilisateur connect�
		setPlaylistUt = utilisateurConnecte.getPlaylists();
		
		// Si la playlist ne contient pas le m�dia
		if(! estAjouteAPlaylist) {
			System.out.println("Ajouter un m�dia � une playlist");
			
			// Parcours de la liste des playlists de l'utilisateur
			for(Playlist playlistUt : setPlaylistUt) {
				// Si la playlist courante parcourue correspond � la playlist � traiter
				//if(playlistUt.getIdPlaylist() == Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idPlaylist"))) {
				if(playlistUt.getIdPlaylist() == playlistSelectionnee.getIdPlaylist()) {
					// Ajout du m�dia � la liste des playlists de l'utilisateur
					playlistUt.getMedias().add(mediaVisualise);
					
					// Enregistrement de l'ajout
					daoPlaylist.sauvegarder(playlistUt);
					break;
				}
			}
		
			// Notification du retrait du m�dia � la playlist s�lectionn�e
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Ajout�e � " + playlistSelectionnee.getNomPlaylist()));
			
			// Actualisation du bool�en
			estAjouteAPlaylist = true;
		}
		else { // Retrait d'un m�dia � une playlist
			System.out.println("Retirer un m�dia � une playlist");
			
			// Parcours de la liste des playlists de l'utilisateur
			for(Playlist playlistUt : setPlaylistUt) {
				// Si la playlist courante parcourue correspond � la playlist � traiter
				//if(playlistUt.getIdPlaylist() == Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idPlaylist"))) {
				if(playlistUt.getIdPlaylist() == playlistSelectionnee.getIdPlaylist()) {
					// Suppression du m�dia � la liste des playlists de l'utilisateur
					playlistUt.getMedias().remove(mediaVisualise);
					
					// Enregistrement de la suppression
					daoPlaylist.sauvegarder(playlistUt);
					break;
				}
			}
		
			// Notification du retrait du m�dia � la playlist s�lectionn�e
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Retir�e � " + playlistSelectionnee.getNomPlaylist()));
			
			// Actualisation du bool�en
			estAjouteAPlaylist = false;
		}
		
		return "ajouterMediaAPlaylist";
	}
	
	/** 
	 * Cr�ation d'une playlist
	 * @return
	 */
	public String creerPlaylist() {
		System.out.println("Cr�ation d'une playlist");
		
		// Cr�ation de la nouvelle playlist
		nvlPlaylist = new Playlist(nomPlaylistACreer, descriptionPlaylistACreer, "", daoTypePlaylist.typeAutre(), daoVisibilite.typeVisible()); //visibilit� TODO
		
		boolean existePlaylistUt = false;
		for(Playlist playlistUt : utilisateurConnecte.getPlaylists()) {
			if(playlistUt.getNomPlaylist().equals(nomPlaylistACreer)) {
				existePlaylistUt = true;
				break;
			}
		}
		
		// Si l'utilisateur ne poss�de pas d�j� cette nouvelle playlist
		//if(! utilisateurConnecte.getPlaylists().contains(nvlPlaylist)) {
		if(! existePlaylistUt) {
			//Ajout de la nouvelle playlist � la liste des playlists de l'utilisateur
			utilisateurConnecte.getPlaylists().add(nvlPlaylist);
			
			// Sauvegarde de l'ajout
			daoUtilisateur.sauvegarder(utilisateurConnecte);
			
			// Affichage de la notification
			FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage("Mission accomplie ! : Ce m�dia a �t� ajout� � votre playlist : " + nomPlaylistACreer));
		}
		else { // Si la playlist est d�j� existante
			
			// Affichage de la notification
			message = new FacesMessage("Attention ! La playlist existe d�j�");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(
					null, message);
		}
		
		return "creerPlaylist";
	}

	/** 
	 * G�n�ration du graphique du nombre de vues du m�dia (ligne)
	 * @return
	 */
	private void creerGraphiqueStatVues() {
		// Cr�ation du graphique
		graphiqueStatVues = new CartesianChartModel();

		// Cr�ation de la s�rie du graphique
		graphiqueVues = new ChartSeries();
		// Affectation du label associ� � la s�rie
		graphiqueVues.setLabel("Vues totales");

		// R�cup�ration des donn�es n�cessaires � la g�n�ration du graphique (requ�te HQL)
        Query resultatStatVues = daoMedia.statVues(mediaVisualise);
        //suivant heure et mois XXX
        
        // Parcours du SELECT de la requ�te HQL
        for(Iterator<?> it = resultatStatVues.iterate();it.hasNext();) {
        	Object[] row = (Object[]) it.next();
        
        	//Mise en place des donn�es pour la g�n�ration du graphique (date en abscisse et nombre de vues en ordonn�e)
        	graphiqueVues.set(row[0], Integer.parseInt(row[1].toString()));
        }
        
        // Mise en place de l'�chelle des ordonn�es (requ�te HQL)
        maxY = daoMedia.totalVues(mediaVisualise);

        // Ajout de la s�rie cr��e au graphique
		graphiqueStatVues.addSeries(graphiqueVues);
	}
	
	/** 
	 * G�n�ration du graphique du nombre de J'aime / Je n'aime pas (histogramme horizontal)
	 * @return
	 */
	private void creerGraphiqueAimeNAimePas() {
		// Cr�ation du graphique
		graphiqueAimeNAimePas = new CartesianChartModel();  
  
		// Cr�ation de la s�rie 1 du graphique
        ChartSeries utilisateursAyantAime = new ChartSeries();  
        // Affectation du label associ� � la s�rie 1
        utilisateursAyantAime.setLabel("Utilisateurs ayant aim�");  
  
        // Mise en place des donn�es pour la g�n�ration de la s�rie 1 du graphique
    	utilisateursAyantAime.set("1", resultatNbAime);
  
    	// Cr�ation de la s�rie 2 du graphique
        ChartSeries utilisateursNAyantPasAime = new ChartSeries();
        // Affectation du label associ� � la s�rie 2
        utilisateursNAyantPasAime.setLabel("Utilisateurs n'ayant pas aim�");  
  
        // Mise en place des donn�es pour la g�n�ration de la s�rie 1 du graphique
        utilisateursNAyantPasAime.set("2", resultatNbNAimePas);
  
        // Ajout des 2 s�ries au graphique
        graphiqueAimeNAimePas.addSeries(utilisateursAyantAime);  
        graphiqueAimeNAimePas.addSeries(utilisateursNAyantPasAime);  
    }
	
	/** 
	 * Alimentation du carousel de recommendation de m�dias (suivant l'artiste du m�dia visualis�)
	 * @return
	 */
	public String alimenterCarouselRecommendationMedias() {
		//System.out.println("alimenterCarouselRecommendationMedias");
		
		// R�cup�ration des m�dias recommend�s ayant le m�me artiste que le m�dia visualis� (requ�te HQL)
		carouselRecommendationMedias = daoMedia.recommendationMediasSuivantMediaVisualise(mediaVisualise);
		
		return "alimenterCarouselRecommendationMedias";
	}
	
	/** 
	 * D�sactivation du lecteur iframe (code d'int�gration)
	 * @return
	 */
	public void desactiverLecteurIframe(AjaxBehaviorEvent e) {
		//System.out.println("desactiverLecteurIframe");
		
		// R�cup�ration de la valeur du checkbox (pour savoir s'il a �t� coch� ou pas)
		check = (HtmlSelectBooleanCheckbox)e.getSource();
		Object checkvar = check.getValue();
		//System.out.println("Check� : " + checkvar);
		
		// Si ce n'est pas coch�
	    if(! Boolean.parseBoolean(checkvar.toString()))	{
			//System.out.println("Code d'int�gration par d�faut");
	    	codeIntegration = "<iframe width='" + largeur + "' height='" + hauteur + "' src='" + url + "' frameborder='0' allowfullscreen></iframe>";
		}
		else { // Si c'est coch� (Utiliser l'ancien code d'int�gration)
			//System.out.println("Code d'int�gration ancien");
			codeIntegration = "<object width='" + largeur + "' height='" + hauteur + "'><param name='movie' value='" + url + "'></param><param name='allowFullScreen' value='true'></param><param name='allowscriptaccess' value='always'></param><embed src='http://www.youtube.com/v/ZQ2nCGawrSY?version=3&amp;hl=fr_FR' type='application/x-shockwave-flash' width='" + largeur + "' height='" + hauteur + "' allowscriptaccess='always' allowfullscreen='true'></embed></object>";	
		}
	}
	
	/** 
	 * Changement de la taille du lecteur (code d'int�gration)
	 * @return
	 */
	public void changerTailleLecteur(AjaxBehaviorEvent e) {
		System.out.println("changerTailleLecteur");
		
		// Si choix d'un lecteur petit coch�
		if(tailleLecteur.equals("petit")) { //TODO tailleLecteur
        	largeur = 320; //changement de la largeur (code d'int�gration)
        	hauteur = 180; //changement de la hauteur (code d'int�gration)
        }
		// Si choix d'un lecteur moyen coch�
        else if(tailleLecteur.equals("moyen")) {
        	largeur = 480;
        	hauteur = 270;
        }
		// Si choix d'un lecteur grand coch�
        else if(tailleLecteur.equals("grand")) {
        	largeur = 560;
        	hauteur = 315;
        }
		System.out.println(tailleLecteur + " ==> " + largeur + "***" + hauteur);
		
		// R�cup�ration de la valeur du checkbox (pour savoir s'il a �t� coch� ou pas)
		check = (HtmlSelectBooleanCheckbox)e.getSource();
		Object checkvar = check.getValue();
				
		// Si ce n'est pas coch�
		if(! Boolean.parseBoolean(checkvar.toString()))	{
			codeIntegration = "<iframe width='" + largeur + "' height='" + hauteur + "' src='" + url + "' frameborder='0' allowfullscreen></iframe>";
		}
		else {
			codeIntegration = "<object width='" + largeur + "' height='" + hauteur + "'><param name='movie' value='" + url + "'></param><param name='allowFullScreen' value='true'></param><param name='allowscriptaccess' value='always'></param><embed src='http://www.youtube.com/v/ZQ2nCGawrSY?version=3&amp;hl=fr_FR' type='application/x-shockwave-flash' width='" + largeur + "' height='" + hauteur + "' allowscriptaccess='always' allowfullscreen='true'></embed></object>";	
		}
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
		mediaVisualise.setCategories(setCategorie);
		*/
		//sauvegarder
		
		return "modifierCategoriesTags";
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
	
	/*public String getImgFavori() {
		return imgFavori;
	}
	
	public void setImgFavori(String imgFavori) {
		this.imgFavori = imgFavori;
	}*/
	
	public String getMotDePasseMedia() {
		return motDePasseMedia;
	}
	
	public void setMotDePasseMedia(String motDePasseMedia) {
		this.motDePasseMedia = motDePasseMedia;
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
	
	public Playlist getPlaylistSelectionnee() {
		return playlistSelectionnee;
	}
	
	public void setPlaylistSelectionnee(Playlist playlistSelectionnee) {
		this.playlistSelectionnee = playlistSelectionnee;
	}
	
	public boolean isjAimeDisabled() {
		return jAimeDisabled;
	}

	public void setjAimeDisabled(boolean jAimeDisabled) {
		this.jAimeDisabled = jAimeDisabled;
	}

	public boolean isJeNAimePasDisabled() {
		return jeNAimePasDisabled;
	}

	public void setJeNAimePasDisabled(boolean jeNAimePasDisabled) {
		this.jeNAimePasDisabled = jeNAimePasDisabled;
	}
	
	public StreamedContent getFile() {  
		System.out.println("File : " + file);
        return file;  
    }

	public boolean isEstAjouteAPlaylist() {
		return estAjouteAPlaylist;
	}

	public void setEstAjouteAPlaylist(boolean estAjouteAPlaylist) {
		this.estAjouteAPlaylist = estAjouteAPlaylist;
	}
	
	public String getParamUrl() {
		return paramUrl;
	}

	public void setParamUrl(String paramUrl) {
		this.paramUrl = paramUrl;
	}
}
