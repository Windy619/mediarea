import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import metier.media.*;
import dao.media.*;

import metier.utilisateur.*;
import dao.utilisateur.*;



public class BeanMedia {
	private DaoMedia daoMedia;
	private Media mediaVisualise;
	
	private String titreMedia; //nécessaire de faire appel à l'objet car #{beanMedia.mediaVisualise.titreMedia} ne fonctionne pas
	private int nbCommentaires;
	private String auteur;
	private String datePublication;
	private String description;
	private String categories = "";
	private String tags;
	private List<Commentaire> commentaires;
	private int note;
	private String nomTypeMedia;
	private String commentaireSaisi;
	
	//****

	private DaoRegarder daoRegarder;
	private Regarder regarder;
	
	private int nbVues;
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
	private int resultatNbAime;
	private int resultatNbAimeNAimePas;

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
		
		Set<Categorie> setCategories = mediaVisualise.getCategories();
		Iterator<Categorie> i=setCategories.iterator(); // on crée un Iterator pour parcourir notre Set
		while(i.hasNext()) // tant qu'on a un suivant
		{
			//System.out.println(i.next().getNomCategorie()); // on affiche le suivant
			categories = i.next().getNomCategorie();
		}
		//System.out.println("Categories : " + categories);
		
		Set<Tag> setTags = mediaVisualise.getTags();
		Iterator<Tag> tagCompteur=setTags.iterator();
		while(tagCompteur.hasNext())
		{
			//System.out.println(i.next().getNomCategorie());
			tags = tagCompteur.next().getNomTag();
		}
		//System.out.println("Tags : " + tags);
		
		commentaires = new ArrayList<Commentaire>(mediaVisualise.getCommentaires());
		
		nomTypeMedia = mediaVisualise.getType().getNomTypeMedia();

		
		//***********************************************
		
		//Regarder
		daoRegarder = new DaoRegarder();
		
		List<?> listeRegarder = daoRegarder.getTous();
		int nbVues = 0;
        for (Object object : listeRegarder) {
        	if(((Regarder) object).getMedia() == mediaVisualise) { //faire une requete HQL TODO
        		nbVues += ((Regarder) object).getNbVues();
        	}
		}
		if(nbVues > 0) {
			motVues = "s"; //"vues" au pluriel
		}
		
		//***********************************************
		
		//Utilisateur (connecté)
		daoUtilisateur = new DaoUtilisateur();
		util = daoUtilisateur.getUn(1);
		
		//***********************************************		
		
		daoAimer = new DaoAimer();
		resultatNbAime = daoMedia.nbAimeMedia(mediaVisualise.getIdMedia()).size();
		resultatNbAimeNAimePas = daoMedia.nbAimeMedia(mediaVisualise.getIdMedia()).size();
		
		//***********************************************
		
		nomAvatar = util.getAvatar().getNomAvatar();
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

	public int getNbCommentaires() {
		return nbCommentaires;
	}

	public void setNbCommentaires(int nbCommentaires) {
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

	public int getNbVues() {
		return nbVues;
	}

	public void setNbVues(int nbVues) {
		this.nbVues = nbVues;
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
	
	public String getCategories() {
		return categories;
	}
	
	public void setCategories(String categories) {
		this.categories = categories;
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

	public int getResultatNbAime() {
		return resultatNbAime;
	}

	public void setResultatNbAime(int resultatNbAime) {
		this.resultatNbAime = resultatNbAime;
	}

	public int getResultatNbAimeNAimePas() {
		return resultatNbAimeNAimePas;
	}

	public void setResultatNbAimeNAimePas(int resultatNbAimeNAimePas) {
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
	}
	
	public String chercherMediasSuggeres() {
		
		
		return "chercherMediasSuggeres";
	}
	
}
