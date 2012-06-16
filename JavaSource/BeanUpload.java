import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.validation.constraints.Size;

import metier.media.Categorie;
import metier.media.Fichier;
import metier.media.FichierUpload;
import metier.media.Media;
import metier.media.Photo_Couverture;
import metier.media.Type_Media;
import metier.utilisateur.Utilisateur;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.UploadedFile;

import outil.GoogleImage;
import outil.Md5;
import dao.media.DaoCategorie;
import dao.media.DaoMedia;
import dao.media.DaoTypeMedia;
import dao.media.DaoVisibilite;
import dao.utilisateur.DaoUtilisateur;


public class BeanUpload {

	// DAO
	DaoUtilisateur daoUtilisateur;
	DaoMedia daoMedia;
	DaoTypeMedia daoTypeMedia;
	DaoCategorie daoCategorie;
	DaoVisibilite daoVisibilite;
	
	// Fichier a uploader
    private FichierUpload fichierUploade = null;
    
    // Formulaire
    private Type_Media typeMedia;
    @Size(min=0, max=100, message="Le nom du média ne doit pas dépasser 50 caractères")
    private String nomMedia;
    private ArrayList<Categorie> categoriesMedia;
    private UploadedFile photoMedia = null;
    private String tagsMedia;
    private String motdepasseMedia;
    private boolean autoriserTelechargementMedia = true;
    private boolean autoriserCommentaireMedia = true;
    private boolean rechercheAutomatique = false;
    private String descriptionMedia;
    
    // Boolean
    private boolean afficherFormulaire;
    private boolean afficherFichierUploade;
    private boolean afficherMotDePasse;

    
	// Utilisateur connecte actuellement
	private Utilisateur utilisateurConnecte;
	
	//Bean
	BeanConnexion beanConnexion;    
	
	// Autre/Metier
	private FacesMessage message;
	private ArrayList<Categorie> lstCategories;
    
	/**
	 * Constructeur
	 */
	public BeanUpload() {
		// DAO
		daoUtilisateur = new DaoUtilisateur();
		daoMedia = new DaoMedia();
		daoTypeMedia = new DaoTypeMedia();
		daoCategorie = new DaoCategorie();
		daoVisibilite = new DaoVisibilite();
		
		// Chargement de l'utilisateur connecte
		beanConnexion = (BeanConnexion) FacesContext.getCurrentInstance().getCurrentInstance().getExternalContext().getSessionMap().get("beanConnexion");
		
		lstCategories = new ArrayList<Categorie>(daoCategorie.getTous());
		// Chargement des catégories disponnibles
		/*for (Categorie categorie : daoCategorie.getTous()) {
			lstCategories.put(categorie.getNomCategorie(), categorie);
		}*/
		
		
		categoriesMedia = new ArrayList<Categorie>();
		
		afficherFormulaire = false;
		afficherMotDePasse = false;
				
		if (beanConnexion != null) {
			// ON charge l'utilisateur connecte
			utilisateurConnecte = beanConnexion.getUser();					
		}		
	}
	
	/**
	 * Upload d'un média
	 * @return
	 */
	public String uploader() {
		
		// Création d'un nouvel objet Media
		Media media = new Media();
		
		Photo_Couverture pc = new Photo_Couverture();	
				
		// Recherche automatique activée
		if (rechercheAutomatique) {
			
			ArrayList<String> lstLien = new ArrayList<String>();
			
			try {
				lstLien = GoogleImage.rechercheGoogle(nomMedia);
			} catch (Exception e) {
				System.err.println("Impossible de joindre l'api google");
			}
			
			
			String chemin;
			String nom;
			
			// Si on trouve un resultat, on prend le premier
			if (lstLien.size() > 0) {
				
				System.out.println("Photo trouvée !");
				
				chemin = "";
				nom = lstLien.get(0);
				
	            // Création de l'objet Photo_Couverture
	        	File photoCree = ecrireFichierRecherche(nom);
	        	
	    		pc.setCheminPhotoCouverture(photoCree.getAbsolutePath());
	    		pc.setNomPhotoCouverture(photoCree.getName());
	    		
	    		
				// Maj du media
				media.setPhoto(pc);	
	
	    		System.out.println(photoCree.getAbsolutePath());				
				
				
			} else { // Sinon on prend une image par défaut

				System.out.println("Aucune photo trouvée !");
				
				chemin = "/images/";
				nom = "";
				
		    	if (isAudio(fichierUploade)) {
		    		nom = "audio128.png";
		    	} else if (isVideo(fichierUploade)) {
		    		nom = "video128.png";
		    	}
		    	
	    		pc.setCheminPhotoCouverture(chemin);
	    		pc.setNomPhotoCouverture(nom);
	    		    		
	    		// Maj du media
	    		media.setPhoto(pc);					
			}
					
		} else if (photoMedia != null) { // Pas de recherche mais une photo a été postée
	        try {
	        	// Récupération du fichier uploadé
	            FichierUpload photo = new FichierUpload();
	            photo.setTaille(photoMedia.getSize());
	            photo.setNom(photoMedia.getFileName().substring(0,photoMedia.getFileName().length()-4));
	            photo.setExtension(photoMedia.getFileName().substring(photoMedia.getFileName().length()-4));
	            photo.setData(photoMedia.getContents());
	            
	            photo.reCalculerTaille();	
	            
	            // Création de l'objet Photo_Couverture
	        	File photoCree = ecrireFichierUploade(photo);
	        	
	    		pc.setCheminPhotoCouverture(photoCree.getAbsolutePath());
	    		pc.setNomPhotoCouverture(photoCree.getName());
	    		
	    		System.out.println(photoCree.getAbsolutePath());
	    		
	    		// Maj du media
	    		media.setPhoto(pc);
			} catch (Exception e) {
				e.printStackTrace();
			}			
		} else { // Photo par défaut
        	
			String chemin = "/images/";
			String nom = "";
			
	    	if (isAudio(fichierUploade)) {
	    		nom = "audio128.png";
	    	} else if (isVideo(fichierUploade)) {
	    		nom = "video128.png";
	    	}
	    	
    		pc.setCheminPhotoCouverture(chemin);
    		pc.setNomPhotoCouverture(nom);
    		    		
    		// Maj du media
    		media.setPhoto(pc);			
		}
	

		// Modification de l'objet
		if (nomMedia != null) {
			media.setTitreMedia(nomMedia);
		} else {
			nomMedia = "";
		}
		
		if (typeMedia != null) {
			media.setType(typeMedia);
		} else {
			media.setType(daoTypeMedia.typeSon());
		}
				
		if (categoriesMedia != null) {
			HashSet categories = new HashSet<Categorie>(categoriesMedia);
			media.getCategories().addAll(categories);	
		}

		if (motdepasseMedia != null) {
			// Si un mot de passe a été renseigné, on le crypte et on l'enregistre dans la base
			media.setMdpMedia(Md5.getHash(motdepasseMedia));
			// Type non visible
			media.setVisibilite(daoVisibilite.typeNonVisible());
		} else {
			// Type visible
			media.setVisibilite(daoVisibilite.typeVisible());
		}
		
		media.setDescriptionMedia(descriptionMedia);
		media.setDatePublication(new Date());
		media.setaCommentairesOuverts(autoriserCommentaireMedia);
		media.setEstTelechargementAutorise(autoriserTelechargementMedia);
		media.setAuteurMedia(utilisateurConnecte);
		
		// Et enfin, on ajoute le fichier source du Media :
		try {
			File fichierCree = ecrireFichierUploade(fichierUploade);
			Fichier fichier = new Fichier(fichierCree.getAbsolutePath(),fichierCree.getName());
			media.setFichier(fichier);			
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		// Sauvegarde du Media
		daoMedia.sauvegarder(media);
		
		// On ajoute le média à la liste de média de l'utilisateur
		utilisateurConnecte.getMedias().add(media);
		
		// On sauvegarde l'utilisateur
		daoUtilisateur.sauvegarder(utilisateurConnecte);
		
		// On supprime le fichier de la liste des uploads
		fichierUploade = null;
		
		// On remet les valeurs par défaut
	    nomMedia = "";
	    tagsMedia = "";
	    motdepasseMedia = "";
	    autoriserTelechargementMedia = true;
	    autoriserCommentaireMedia = true;
	    rechercheAutomatique = false;
	    descriptionMedia = "";	
	    categoriesMedia.clear();
		
        // On affiche un message à l'utilisateur
        FacesMessage msg = new FacesMessage("Le media : ", media.getTitreMedia() + " a été crée avec succès !");  
        FacesContext.getCurrentInstance().addMessage(null, msg); 		
        
		return "upload";
	}
 
	/**
	 * Ecriture sur le serveur du fichier à rechercher
	 * @param urlFichier
	 * @return
	 */
    public File ecrireFichierRecherche(String urlFichier) {
    	
		String chemin = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("resources");
		String fichier="";
		
		try {	
			
		   	URL u = new URL(urlFichier);
			URLConnection uc = u.openConnection();

			int taille = uc.getContentLength();

			// Créer un flux d’entrée pour le fichier
			InputStream brut = uc.getInputStream();
			// Mettre ce flux d’entrée en cache (pour un meilleur transfert, plus sûr et plus régulier).
			InputStream entree = new BufferedInputStream(brut);
			byte[] donnees = new byte[taille];

			int octetsLus = 0;

			int deplacement = 0; 
			float alreadyRead = 0;

			// Boucle permettant de parcourir tous les octets du fichier à lire
			while(deplacement < taille) {
				octetsLus = entree.read(donnees, deplacement, donnees.length-deplacement);
				alreadyRead = alreadyRead + octetsLus;

				if(octetsLus == -1) break;

				deplacement += octetsLus;

			}
			

			// fermer le flux d’entrée.
			entree.close();

			// Récupérer le nom du fichier
			fichier = u.getFile();
			fichier = fichier.substring(fichier.lastIndexOf('/') + 1);

			FileOutputStream fichierSortie = new FileOutputStream(chemin + "/images/" + fichier);

			// copier…
			fichierSortie.write(donnees);

			// vider puis fermer le flux de sortie
			fichierSortie.flush(); 
			fichierSortie.close();	    				
				        
			System.out.println("Fichier : " + fichier);
		
		} catch (Exception e) {
			e.printStackTrace();
		}	
		

		return new File(chemin + "/images/" + fichier);
		
    }
    
    /**
     * Ecriture sur le serveur du fichier Uploade
     * @param file
     * @return
     * @throws IOException
     */
    public File ecrireFichierUploade(FichierUpload file) throws IOException {
    	
        // On crée un File physique sur le seveur d'application avec pour nom le timestamp
    	
    	String chemin = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("resources");
    	System.out.println("Chemin : " + chemin);
    	
    	if (isAudio(file)) {
    		chemin += "/audios/";
    	} else if (isVideo(file)) {
    		chemin += "/videos/";
    	} else if (isImage(file)) {
    		chemin += "/images/";
    	}

    	 
        File fileUploaded = new File(chemin + "/" + getTimeStamp() + file.getExtension());
        fileUploaded.createNewFile();
        
        // On écrit le fichier
        ByteArrayInputStream sourceFile = new ByteArrayInputStream(file.getData());
        
        try {
        	FileOutputStream destinationFile = new FileOutputStream(fileUploaded);
        	
        	byte buffer[] = new byte[512*1024];
        	int nbLecture;
        	
        	while ( ( nbLecture = sourceFile.read(buffer)) != -1) {
        		destinationFile.write(buffer, 0, nbLecture);
        	}
        	
        	destinationFile.close();
        	sourceFile.close();
        	
		} catch (Exception e) {
			// TODO: handle exception
		}    
        
		System.out.println(fileUploaded.getAbsolutePath());
		
        return fileUploaded;
    }
    
    
    /**
     * Listener de fichier Uploadés
     * @param event
     */
    public void handleFileUpload(FileUploadEvent event) {  
    	
    	// On récupère l'item uploadé
        UploadedFile item = event.getFile();
        
        // On crée un FichierUpload pour l'affichage des informations
        FichierUpload fichierUpload = new FichierUpload();
        fichierUpload.setTaille(item.getSize());
        fichierUpload.setNom(item.getFileName().substring(0,item.getFileName().length()-4));
        fichierUpload.setExtension(item.getFileName().substring(item.getFileName().length()-4));
        fichierUpload.setData(item.getContents());
        
        fichierUpload.reCalculerTaille();
        
        // On ajoute le fichierUpload à la liste
        fichierUploade = fichierUpload;
        
        // Par défaut on peut dire que le nom du média est le nom du fichier !
        nomMedia = fichierUpload.getNom();
        tagsMedia = fichierUpload.getNom();
        motdepasseMedia = "";
        descriptionMedia = fichierUpload.getNom();      
        
        if (isAudio(fichierUpload)) {
        	typeMedia = daoTypeMedia.typeSon();
        } else if (isVideo(fichierUpload)) {
        	typeMedia = daoTypeMedia.typeVideo();
        }
        
        // On affiche un message à l'utilisateur
        FacesMessage msg = new FacesMessage("Le fichier : ", fichierUploade.getNom() + " a été uploadé avec succès !");  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }     
    
    public void handlePictureUpload() {  
    	// On affiche un message à l'utilisateur
    	System.out.println("Handle de la photo");
    	if (photoMedia != null) {
            FacesMessage msg = new FacesMessage("Le fichier : ", photoMedia.getFileName() + " a été uploadé avec succès !");  
            FacesContext.getCurrentInstance().addMessage(null, msg);      		
    	}
    }     
    
    /**
     * Détermine si un fichierUpload est un fichier Audio ou non
     * @param file
     * @return
     */
    public boolean isAudio(FichierUpload file) {
    	if (file != null && file.getExtension().matches("(.*)(mp3|MP3|wav|WAV|riff|ogg)(.*)")) {
    		return true;
    	}
    	
    	return false;
    }
    
    /**
     * Détermine si un fichierUpload est un fichier Vidéo ou non
     * @param file
     * @return
     */
    public boolean isVideo(FichierUpload file) {
    	if (file != null && file.getExtension().matches("(.*)(mp4|MP4|AVI|avi|wmv|AVI)(.*)")) {
    		return true;
    	}
    	
    	return false;
    }  
    
    /**
     * Détermine si un fichierUpload est un fichier Image ou non
     * @param file
     * @return
     */
    public boolean isImage(FichierUpload file) {
    	if (file != null && file.getExtension().matches("(.*)(png|PNG|jpg|JPG|jpeg|JPEG)(.*)")) {
    		return true;
    	}
    	
    	return false;
    }    
    
    public String supprimerFichierUploade() {
        
    	fichierUploade = null;
    
    	return null;
    }    
 
    public long getTimeStamp() {
        return System.currentTimeMillis();
    }
  
    
    // GETTER SETTER
    
	public FichierUpload getFichierUploade() {
		return fichierUploade;
	}


	public void setFichierUploade(FichierUpload file) {
		this.fichierUploade = file;
	}

	public boolean isAfficherFormulaire() {
		return afficherFormulaire;
	}

	public void setAfficherFormulaire(boolean afficherFormulaire) {
		this.afficherFormulaire = afficherFormulaire;
	}

	public Utilisateur getUtilisateurConnecte() {
		utilisateurConnecte = beanConnexion.getUser();
		return utilisateurConnecte;
	}

	public void setUtilisateurConnecte(Utilisateur utilisateurConnecte) {
		this.utilisateurConnecte = utilisateurConnecte;
	}

	public Type_Media getTypeMedia() {
		return typeMedia;
	}

	public void setTypeMedia(Type_Media typeMedia) {
		this.typeMedia = typeMedia;
	}

	public String getNomMedia() {
		return nomMedia;
	}

	public void setNomMedia(String nomMedia) {
		this.nomMedia = nomMedia;
	}

	public UploadedFile getPhotoMedia() {
		return photoMedia;
	}

	public void setPhotoMedia(UploadedFile photoMedia) {
		this.photoMedia = photoMedia;
	}

	public String getTagsMedia() {
		return tagsMedia;
	}

	public void setTagsMedia(String tagsMedia) {
		this.tagsMedia = tagsMedia;
	}

	public String getMotdepasseMedia() {
		return motdepasseMedia;
	}

	public void setMotdepasseMedia(String motdepasseMedia) {
		this.motdepasseMedia = motdepasseMedia;
	}

	public String getDescriptionMedia() {
		return descriptionMedia;
	}

	public void setDescriptionMedia(String descriptionMedia) {
		this.descriptionMedia = descriptionMedia;
	}


	public boolean isAfficherFichierUploade() {
		return afficherFichierUploade;
	}

	public void setAfficherFichierUploade(boolean afficherFichierUploade) {
		this.afficherFichierUploade = afficherFichierUploade;
	}
	
	public ArrayList<Categorie> getLstCategories() {
		return lstCategories;
	}

	public void setLstCategories(ArrayList<Categorie> lstCategories) {
		this.lstCategories = lstCategories;
	}

	public FacesMessage getMessage() {
		return message;
	}

	public void setMessage(FacesMessage message) {
		this.message = message;
	}

	public boolean isAutoriserTelechargementMedia() {
		return autoriserTelechargementMedia;
	}

	public void setAutoriserTelechargementMedia(boolean autoriserTelechargementMedia) {
		this.autoriserTelechargementMedia = autoriserTelechargementMedia;
	}

	public boolean isAutoriserCommentairesMedia() {
		return autoriserCommentaireMedia;
	}

	public void setAutoriserCommentairesMedia(boolean ouvrirCommentaireMedia) {
		this.autoriserCommentaireMedia = ouvrirCommentaireMedia;
	}

	public ArrayList<Categorie> getCategoriesMedia() {
		return categoriesMedia;
	}

	public void setCategoriesMedia(ArrayList<Categorie> categoriesMedia) {
		this.categoriesMedia = categoriesMedia;
	}

	public boolean isAfficherMotDePasse() {
		return afficherMotDePasse;
	}

	public void setAfficherMotDePasse(boolean afficherMotDePasse) {
		this.afficherMotDePasse = afficherMotDePasse;
	}

	public boolean isRechercheAutomatique() {
		return rechercheAutomatique;
	}

	public void setRechercheAutomatique(boolean rechercheAutomatique) {
		this.rechercheAutomatique = rechercheAutomatique;
	}

	
}
