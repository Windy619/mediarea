import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
//import org.richfaces.component.SortOrder;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.*;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.LineChartSeries;

import metier.media.*;
import dao.media.*;

import metier.utilisateur.*;
import dao.utilisateur.*;


/**
 * @author Florence
 *
 */
public class BeanMedia {
	private DaoMedia daoMedia;
	private String idMediaVisualise = "";
	private Media mediaVisualise;
	
	private String titreMedia; //nécessaire de faire appel à l'objet car #{beanMedia.mediaVisualise.titreMedia} ne fonctionne pas
	private long nbCommentaires;
	private String auteur;
	private String datePublication;
	private String description;
	private List<Categorie> listeCategories;
	private String tags;
	private List<Tag> listeTags;
	private List<Commentaire> commentaires;
	private double note;
	private String nomTypeMedia;
	private String commentaireSaisi;
	List<Media> listeMediasSuggeres = new ArrayList<Media>();
	
	//****

	private DaoRegarder daoRegarder;
	private Regarder regarder;
	
	//private int nbVues;
	private long resultatTotalVuesMedia;
	
	private String motVues = "";
	
	//****

	private DaoSignalementMedia daoSignalementMedia;
	private Signalement_Media signalementMedia;
	private String raison = ""; //récupéré de la vue
	
	//*****
	
	private DaoUtilisateur daoUtilisateur;
	private Utilisateur util;
	
	//*****
	
	private DaoAimer daoAimer;
	private Aimer aimer;
	private long resultatNbAime;
	private long resultatNbAimeNAimePas;

	//*****
	
	private String nomAvatar;
	
	//*****
	
	boolean estVisible = false; //TODO
	public boolean isEstVisible() {
		return estVisible;
	}
	public void setEstVisible(boolean estVisible) {
		this.estVisible = estVisible;
	}
	
	//*****
	
	public static DaoTypePlaylist daoTypePlaylist = new DaoTypePlaylist();
	public static DaoPlaylist daoPlaylist = new DaoPlaylist();
	private String txtFavori;
	
	private List<Playlist> listePlaylistUt;
	private String imgAjoutPlaylist;
	private boolean estAjouteAPlaylist = false;
	
	@Size(min = 0, message = "Ce champ est requis.")
    private String nomPlaylistACreer;
	private String descriptionPlaylistACreer;
	private String visibilitePlaylistACreer;
	private FacesMessage message;

	//*****

	public static DaoVisibilite daoVisibilite = new DaoVisibilite();
	private List<Visibilite> listeVisibilite;
	private List<String> listeNomVisibilite;
	//private Map<Visibilite, String> items; // +getter
	
	//*****
	
	private long resultatTotalVotesMedia;
	
	private String motVotes;
	
	//*****
	
	/*private SortOrder statesOrder = SortOrder.unsorted;
	public SortOrder getStatesOrder() {
		return statesOrder;
	}
	public void setStatesOrder(SortOrder statesOrder) {
		this.statesOrder = statesOrder;
	}*/
	
	//*****
	  
    private CartesianChartModel linearModel;
    
    private String codeIntegration;
    private String lien;
    private String url;
    private String tailleLecteur = "";
    int largeur = 320;
    int hauteur = 180;
    
    
	
	
	//constructeur
	public BeanMedia() {
		//Media
		daoMedia = new DaoMedia();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		idMediaVisualise = request.getParameter("v");
		if(idMediaVisualise == null) {
			
		}
		else {			
			mediaVisualise = daoMedia.getUn(Long.parseLong(idMediaVisualise));
		}
        
		
		titreMedia = mediaVisualise.getTitreMedia();
		//System.out.println(mediaVisualise.getTitreMedia());
		
		//nbCommentaires = String.valueOf(mediaVisualise.getCommentaires().size()); //toujours renseigné une chaîne de caractères pour un outputText
		nbCommentaires = mediaVisualise.getCommentaires().size(); //Converter en JSF
		
		auteur = mediaVisualise.getAuteurMedia().getNomUtilisateur();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy H:m");
		datePublication = dateFormat.format(mediaVisualise.getDatePublication());
		
		description = mediaVisualise.getDescriptionMedia();
		
		listeCategories = new ArrayList<Categorie>();
		Set<Categorie> setCategories = mediaVisualise.getCategories();
		Iterator<Categorie> i=setCategories.iterator(); // on crée un Iterator pour parcourir notre Set
		while(i.hasNext()) // tant qu'on a un suivant
		{
			//System.out.println(i.next().getNomCategorie()); // on affiche le suivant
			listeCategories.add(i.next());
		}
		//System.out.println("Categories : " + categories);
		
		listeTags = new ArrayList<Tag>();
		Set<Tag> setTags = mediaVisualise.getTags();
		Iterator<Tag> tagCompteur=setTags.iterator();
		while(tagCompteur.hasNext())
		{
			//System.out.println(i.next().getNomCategorie());
			listeTags.add(tagCompteur.next());
		}
		
		commentaires = new ArrayList<Commentaire>(mediaVisualise.getCommentaires());
		
		nomTypeMedia = mediaVisualise.getType().getNomTypeMedia();

		
		//***********************************************
		
		//Regarder
		/*daoRegarder = new DaoRegarder();
		
		List<Regarder> listeRegarder = daoRegarder.getTous();
		int nbVues = 0;
        for (Regarder object : listeRegarder) {
        	if(object.getMedia() == mediaVisualise) {
        		nbVues += ((Regarder) object).getNbVues();
        	}
		}
		if(nbVues > 0) {
			motVues = "s"; //"vues" au pluriel
		}*/
		
		resultatTotalVuesMedia = daoMedia.totalVues(mediaVisualise);
		if(resultatTotalVuesMedia > 0)
		{
			motVues = "s"; //"vues" au pluriel
		}
		
		
		//***********************************************
		
		//Utilisateur (connecté)
		daoUtilisateur = new DaoUtilisateur();
		util = daoUtilisateur.getUn(1);
		
		//***********************************************		
		
		//Aimer
		daoAimer = new DaoAimer();
		resultatNbAime = daoMedia.nbAimeMedia(mediaVisualise.getIdMedia()).size();
		resultatNbAimeNAimePas = daoMedia.nbAimeNAimePas(mediaVisualise.getIdMedia()).size();
		
		//***********************************************
		
		//Avatar
		nomAvatar = util.getAvatar().getNomAvatar();
		
		//***********************************************
		
		//Tag
		Set<Tag> tagMedia = daoMedia.getUn(2).getTags(); //tags du média visualisé
		
		List<Media> listeMedia = daoMedia.getTous();
		
		HashMap<Media, Integer> map = new HashMap<Media, Integer>(); //HashMap contenant le nb d'occurrences de tags correspondants dans les médias
		
		Iterator<Tag> iteratorMedia = tagMedia.iterator();
		while(iteratorMedia.hasNext()) { //parcours des tags du média visualisé
			//System.out.println("Set tagMedia : " + i.next());
			Tag svg = iteratorMedia.next();
			for(Media elMedia : listeMedia) { //parcours de tous les médias
				if(! elMedia.equals(daoMedia.getUn(2))) {//tout sauf le média actuellement visualisé
					Set<Tag> setTagMediaCourant = daoMedia.getUn(elMedia.getIdMedia()).getTags();
					for(Tag tagMediaCourant : setTagMediaCourant) {
						//System.out.println(svg + "***" + tagMediaCourant + " (" + elMedia.getIdMedia() + ")");
						if(svg.toString().equals(tagMediaCourant.toString())) {
							if(map.containsKey(elMedia)) {
								map.put(elMedia, map.get(elMedia) + 1); //tag correspond supplémentaire
							}
							else
							{
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
		//suggestion en tenant compte du titre et catégories TODO
		
		//***********************************************
		
		//Playlist
		Set<Playlist> playlistsUtilisateur = daoUtilisateur.getUn(1).getPlaylists();
		for(Playlist pl : playlistsUtilisateur) {
			if(pl.getType().equals(daoTypePlaylist.getUn(2))) {
				if(pl.getMedias().contains(daoMedia.getUn(2))) {
					txtFavori = "Retirer des favoris";
				}
				else
				{
					txtFavori = "Favori";
				}
			}
		}
		
		listePlaylistUt = new ArrayList<Playlist>(daoUtilisateur.getUn(1).getPlaylists());
	
		
		Set<Playlist> setPlaylistUt = util.getPlaylists();
		for(Playlist playlistUt : setPlaylistUt)
		{
			if(playlistUt.getMedias().contains(daoMedia.getUn(2))) {
				imgAjoutPlaylist = "fermer-croix-supprimer-erreurs-sortie-icone-4368-16.png"; //mime TODO
				estAjouteAPlaylist = true;
				break;
			}
		}
		
		listeVisibilite = daoVisibilite.getTous();
		listeNomVisibilite = new ArrayList<String>();
		//items = new LinkedHashMap<Visibilite, String>();
		//SelectItem optionVisibilite = new SelectItem("ch1", "choice1", "This bean is for selectItems tag", true);
		for(Visibilite visible : listeVisibilite)
		{
			listeNomVisibilite.add(visible.getNomVisibilite());
			//items.put(visible, visible.getNomVisibilite());
		}
		
		//***********************************************
		
		//Note
		resultatTotalVotesMedia = daoMedia.totalVotes(mediaVisualise);
		if(resultatTotalVotesMedia > 0)
		{
			motVotes = "s"; //"vues" au pluriel
		}
		
		//***********************************************
		
		//Autre
        createLinearModel();
        
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        url = req.getRequestURL().toString();
        
        codeIntegration = "<iframe width='320' height='180' src='" + url + "' frameborder='0' allowfullscreen></iframe>";
    }

	//getter et setter
	public Media getMediaVisualise() {
		//System.out.println("Getter mediavisualise");		
		return mediaVisualise;
	}
	
	public void setMediaVisualise(Media mediaVisualise) {
		//System.out.println("Setter mediavisualise");
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
	
	public List<Categorie> getListeCategories() {
		return listeCategories;
	}
	public void setListeCategories(List<Categorie> listeCategories) {
		this.listeCategories = listeCategories;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}
	
	public List<Commentaire> getCommentaires() {
		return commentaires;
	}
	
	public void setCommentaires(List<Commentaire> commentaires) {
		this.commentaires = commentaires;
	}

	public long getResultatNbAime() {
		return resultatNbAime;
	}

	public void setResultatNbAime(long resultatNbAime) {
		this.resultatNbAime = resultatNbAime;
	}

	public long getResultatNbAimeNAimePas() {
		return resultatNbAimeNAimePas;
	}

	public void setResultatNbAimeNAimePas(long resultatNbAimeNAimePas) {
		this.resultatNbAimeNAimePas = resultatNbAimeNAimePas;
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
	
	public List<Tag> getListeTags() {
		return listeTags;
	}
	
	public void setListeTags(List<Tag> listeTags) {
		this.listeTags = listeTags;
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
	
	public List<String> getListeNomVisibilite() {
		return listeNomVisibilite;
	}
	
	public void setListeNomVisibilite(List<String> listeNomVisibilite) {
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
	
	public CartesianChartModel getLinearModel() {  
        return linearModel;  
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
	
	
	
	
	
	//méthodes
	public String jAime() {
		//System.out.println("Méthode jAime");
		
		util.getAimeMedias().add(new Aimer(true,mediaVisualise));
		daoUtilisateur.sauvegarder(util);
		
		return "jAime";
	}
	
	public String jeNAimePas() {
		//System.out.println("Méthode jeNAimePas");
		
		util.getAimeMedias().add(new Aimer(false,mediaVisualise));
		daoUtilisateur.sauvegarder(util);
		
		return "jeNAimePas";
	}
	
	public String envoyerRapport() { //retourné obligatoirement un String et non un void
		//System.out.println("Méthode envoyerRapport");
		
		util.getSignalementsMedias().add(new Signalement_Media(raison, mediaVisualise)); //media_idMedia pas tjrs à null TODO
		daoUtilisateur.sauvegarder(util);
		
		return "envoyerRapport";
	}
	
	public void handleRate(RateEvent rateEvent) {  
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Notation", "Votre note : " + ((Double) rateEvent.getRating()).intValue());  
  
        FacesContext.getCurrentInstance().addMessage(null, message);
        
        vote();
    }
	
	public String vote() {
		System.out.println("Méthode vote");
		//System.out.println("Note : "+note);
		
		util.getNoteMedias().add(new Note((int) note, mediaVisualise)); //media_idMedia + interdire de noter plusieurs fois TODO
		daoUtilisateur.sauvegarder(util);
		
		return "vote";
	}
	
	public String publierCommentaire() {
		//System.out.println("publier commentaire");
		//System.out.println("commentaire saisi : " + commentaireSaisi);
		
		daoMedia.getUn(2).getCommentaires().add(new Commentaire(commentaireSaisi,util)); //mediaVisualise. TODO
		//daoMedia.sauvegarder(daoMedia.getUn(2));
		
		return "publierCommentaire";
	}
	
	public String rendreVisible() {
		estVisible = true;
		
		System.out.println("est visible : " + estVisible);
		
		return "rendreVisible";
	} //TODO
	
	public String incrNbVues() { //incrémenter seulement si clic sur Play et par utilisateur TODO
		//System.out.println("Incrémentation du nombre de vues");
						
		List<Regarder> listeRegarder = daoRegarder.getTous();
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
			//System.out.println("Incrémentation (n'existe pas)");
			util.getRegardeMedias().add(new Regarder(daoMedia.getUn(2)));
			daoUtilisateur.sauvegarder(util);
		}
		
		return "incrNbVues";
	}
	
	public String ajouterAFavori() {
		Set<Playlist> playlistsUtilisateur = daoUtilisateur.getUn(1).getPlaylists();
		
		if(txtFavori == "Favori")
		{
			//System.out.println("Ajout au favori");
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
		else
		{
			//System.out.println("Retrait au favori");
			for(Playlist pl : playlistsUtilisateur) {
				if(pl.getType().equals(daoTypePlaylist.getUn(2))) { //possède déjà une playlist de type Favori
					pl.getMedias().remove(daoMedia.getUn(2)); //suppression du média visualisé de la liste des favoris
					daoPlaylist.sauvegarder(pl);
				}
			}
		}
		
		return "ajouterAFavori";
	}
	
	public void sortByNom() {
		System.out.println("SORT BY");
		
        /*statesOrder = SortOrder.unsorted; //SortOrder.unsorted;
        //timeZonesOrder = SortOrder.unsorted;
        if (statesOrder.equals(SortOrder.ascending)) {
            setStatesOrder(SortOrder.descending);
        } else {
            setStatesOrder(SortOrder.ascending);
        }*/
    }
	
	public String ajouterMediAPlaylist() {
		Set<Playlist> setPlaylistUt = util.getPlaylists();
		if(!estAjouteAPlaylist) {
			System.out.println("Ajouter une vidéo à une playlist");
			for(Playlist playlistUt : setPlaylistUt)
			{
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
			for(Playlist playlistUt : setPlaylistUt)
			{
				if(playlistUt.getIdPlaylist() == Long.parseLong(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idPlaylist"))) {
					playlistUt.getMedias().remove(daoMedia.getUn(2));
					daoPlaylist.sauvegarder(playlistUt);
					break;
				}
			}

			imgAjoutPlaylist = "accepter-check-ok-oui-icone-4851-16.png"; //TODO
		}
		
		return "ajouterMediAPlaylist";
	}
	
	public String creerPlaylist() { //TODO à tester
		System.out.println("Création d'une playlist");
		
		Playlist nvlPlaylist = new Playlist(nomPlaylistACreer, descriptionPlaylistACreer, "", daoTypePlaylist.typeAutre(), daoVisibilite.typeVisible()); //visibilité TODO
		
		if(!daoUtilisateur.getUn(1).getPlaylists().contains(nvlPlaylist))
		{
			daoUtilisateur.getUn(1).getPlaylists().add(nvlPlaylist);
			daoUtilisateur.sauvegarder(daoUtilisateur.getUn(1));
			FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_INFO, "Mission accomplie !", "Ce média a été ajouté à votre playlist: ...")); //XXX en pop-up ?
		}
		else //playlist déjà existante
		{
			message = new FacesMessage("La playlist existe déjà");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(
					"...", message);
		}
		
		return "creerPlaylist";
	}
	
	private void createLinearModel() {  
        linearModel = new CartesianChartModel();  
  
        LineChartSeries series1 = new LineChartSeries();  
        series1.setLabel("Series 1");  
  
        series1.set(1, 2);  
        series1.set(2, 1);  
        series1.set(3, 3);  
        series1.set(4, 6);  
        series1.set(5, 8);  
  
        LineChartSeries series2 = new LineChartSeries();  
        series2.setLabel("Series 2");  
        series2.setMarkerStyle("diamond");  
  
        series2.set(1, 6);  
        series2.set(2, 3);  
        series2.set(3, 2);  
        series2.set(4, 7);  
        series2.set(5, 9);  
  
        linearModel.addSeries(series1);  
        linearModel.addSeries(series2);  
    }
	
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
		
	    if(! Boolean.parseBoolean(checkvar.toString()))
		{
			codeIntegration = "<object width='" + largeur + "' height='" + hauteur + "'><param name='movie' value='" + url + "'></param><param name='allowFullScreen' value='true'></param><param name='allowscriptaccess' value='always'></param><embed src='http://www.youtube.com/v/ZQ2nCGawrSY?version=3&amp;hl=fr_FR' type='application/x-shockwave-flash' width='" + largeur + "' height='" + hauteur + "' allowscriptaccess='always' allowfullscreen='true'></embed></object>";	
		}
		else //!
		{
			codeIntegration = "<iframe width='" + largeur + "' height='" + hauteur + "' src='" + url + "' frameborder='0' allowfullscreen></iframe>";
		}
	}
	
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
}
