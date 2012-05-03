import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

//import org.richfaces.component.SortOrder;

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
	private int note;
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
	
	boolean estVisible = false; // TODO
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

	//*****

	public static DaoVisibilite daoVisibilite = new DaoVisibilite();
	
	//*****
	
	/*private SortOrder statesOrder = SortOrder.unsorted;
	public SortOrder getStatesOrder() {
		return statesOrder;
	}
	public void setStatesOrder(SortOrder statesOrder) {
		this.statesOrder = statesOrder;
	}*/
	
	//constructeur
	public BeanMedia() {
		//Media
		daoMedia = new DaoMedia();
		mediaVisualise = daoMedia.getUn(2);
		
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

		//***********************************************
		
		listeMediasSuggeres = new ArrayList<Media>(map.keySet());
		//System.out.println("size listeMediasSuggeres : " + listeMediasSuggeres.size());
		if(listeMediasSuggeres.size() < 20) {
			listeMediasSuggeres = listeMediasSuggeres.subList(0, listeMediasSuggeres.size());
		}
		else {
			listeMediasSuggeres = listeMediasSuggeres.subList(0, 20);
		}
		
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
		
	public int getNote() {
		return note;
	}

	public void setNote(int note) {
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
	
	public String vote() {
		//System.out.println("Méthode vote");
		//System.out.println("Note : "+note);
		
		util.getNoteMedias().add(new Note(note, mediaVisualise)); //media_idMedia + interdire de noter plusieurs fois TODO
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
	
	public String ajouterMediAPlaylist() {
		System.out.println("Ajouter une vidéo à une playlist");
		
		return "ajouterMediAPlaylist";
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
}
