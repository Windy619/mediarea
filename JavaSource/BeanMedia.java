import java.text.SimpleDateFormat;

import metier.media.Media;
import dao.media.DaoMedia;

import metier.media.Regarder;
import dao.media.DaoRegarder;

import metier.media.Signalement_Media;
import dao.media.DaoSignalementMedia;

import metier.utilisateur.Utilisateur;
import dao.utilisateur.DaoUtilisateur;


public class BeanMedia {
	private DaoMedia daoMedia;
	private Media mediaVisualise;
	
	private String titreMedia; //nécessaire de faire appel à l'objet car #{beanMedia.mediaVisualise.titreMedia} ne fonctionne pas
	private String nbCommentaires;
	private String auteur;
	private String datePublication;
	private String description;
	
	//****
	
	private DaoRegarder daoRegarder;
	private Regarder regarder;
	
	private String nbVues;
	
	//****
	private DaoSignalementMedia daoSignalementMedia;
	private Signalement_Media signalementMedia;
	private String raison = ""; //récupéré de la vue
	
	//*****
	private DaoUtilisateur daoUtilisateur;
	
	
	//constructeur
	public BeanMedia() {
		//Media
		daoMedia = new DaoMedia();
		mediaVisualise = daoMedia.getUn(2);
		
		titreMedia = mediaVisualise.getTitreMedia();
		//System.out.println(mediaVisualise.getTitreMedia());
		
		nbCommentaires = String.valueOf(mediaVisualise.getCommentaires().size()); //toujours renseigné une chaîne de caractères pour un outputText
		
		auteur = mediaVisualise.getAuteurMedia().getNomUtilisateur();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy H:m");
		datePublication = dateFormat.format(mediaVisualise.getDatePublication());
		
		description = mediaVisualise.getDescriptionMedia();
		
		//***********************************************
		
		//Regarder
		daoRegarder = new DaoRegarder();
		regarder = new Regarder(mediaVisualise); //A MODIFIER TODO
		
		nbVues = String.valueOf(regarder.getNbVues());
		
		//***********************************************
		
		//Utilisateur (connecté)
		daoUtilisateur = new DaoUtilisateur();
		//...
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

	public String getNbCommentaires() {
		return nbCommentaires;
	}

	public void setNbCommentaires(String nbCommentaires) {
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

	public String getNbVues() {
		return nbVues;
	}

	public void setNbVues(String nbVues) {
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

	
	//méthodes
	public String envoyerRapport() { //retourné obligatoirement un String et non un void
		System.out.println("Méthode envoyerRapport");
		Utilisateur util = daoUtilisateur.getUn(1);
		util.getSignalementsMedias().add(new Signalement_Media(raison, mediaVisualise));
		daoUtilisateur.sauvegarder(util);
		
		return "envoyerRapport";
	}
}
