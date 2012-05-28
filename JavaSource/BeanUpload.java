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

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.validation.constraints.Size;

import metier.media.Categorie;
import metier.media.Categorie_Media;
import metier.media.Fichier;
import metier.media.FichierUpload;
import metier.media.Media;
import metier.media.Photo_Couverture;
import metier.media.Type_Media;
import metier.media.Visibilite;
import metier.utilisateur.Utilisateur;

import org.primefaces.event.FileUploadEvent;
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
    private ArrayList<FichierUpload> files = new ArrayList<FichierUpload>();
    private FichierUpload fichierSelectionne;
    
    // Formulaire
    private Long typeMedia;
    @Size(min=0, max=100, message="Le nom du média ne doit pas dépasser 50 caractères")
    private String nomMedia;
    private Long categorieMedia;
    private ArrayList<Long> categoriesMedia;
    private UploadedFile photoMedia;
    private String tagsMedia;
    private Long porteeMedia;
    private String motdepasseMedia;
    private boolean autoriserTelechargementMedia = true;
    private boolean autoriserCommentaireMedia = true;
    private boolean rechercheAutomatique = false;
    private String descriptionMedia;
    
    // Boolean
    private boolean afficherFormulaire;
    private boolean afficherListeUploades;
    private boolean afficherMotDePasse;

    
	// Utilisateur connecte actuellement
	private Utilisateur utilisateurConnecte;
	
	//Bean
	BeanConnexion beanConnexion;    
	
	// Autre/Metier
	private FacesMessage message;
	private ArrayList<Type_Media> lstTypesMedia;
	private ArrayList<Categorie> lstCategories;
	private ArrayList<Visibilite> lstVisibilites;
    
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
		
		// Chargement des types de média disponnibles
		lstTypesMedia = new ArrayList<Type_Media>(daoTypeMedia.getTous());
		
		// Chargement des catégories disponnibles
		lstCategories = new ArrayList<Categorie>(daoCategorie.getTous());
		categoriesMedia = new ArrayList<Long>();
		
		// Chargement des types de visibilité		
		lstVisibilites = new ArrayList<Visibilite>(daoVisibilite.getTous());
		
		afficherFormulaire = false;
		afficherMotDePasse = false;
		
		if (beanConnexion != null) {
			// ON charge l'utilisateur connecte
			utilisateurConnecte = beanConnexion.getUser();	
			// Si on trouve l'utilisateur, on charge
			if (utilisateurConnecte != null) {
				
			}
				
		}		
	}
	
	public String uploader() {
		
		// Création d'un nouvel objet Media
		Media media = new Media();
		

		Photo_Couverture pc = new Photo_Couverture();	
		
		// Recherche automatique activée
		if (rechercheAutomatique) {
			ArrayList<String> lstLien = GoogleImage.rechercheGoogle(nomMedia);
			
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
				
		    	if (isAudio(fichierSelectionne)) {
		    		nom = "audio128.png";
		    	} else if (isVideo(fichierSelectionne)) {
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
		} else {
        	
			String chemin = "/images/";
			String nom = "";
			
	    	if (isAudio(fichierSelectionne)) {
	    		nom = "audio128.png";
	    	} else if (isVideo(fichierSelectionne)) {
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
			media.setType(daoTypeMedia.getUn(typeMedia));
		}
		
		if (porteeMedia != null) {
			media.setVisibilite(daoVisibilite.getUn(porteeMedia));
		}
		
		if (categoriesMedia != null) {
			for (Long id : categoriesMedia) {
				media.getCategories().add(new Categorie_Media(2,id));	
			}
		}

		if (motdepasseMedia != null) {
			// Si un mot de passe a été renseigné, on le crypte et on l'enregistre dans la base
			media.setMdpMedia(Md5.getHash(motdepasseMedia));
		}
		
		media.setDescriptionMedia(descriptionMedia);
		media.setDatePublication(new Date());
		media.setaCommentairesOuverts(autoriserCommentaireMedia);
		media.setEstTelechargementAutorise(autoriserTelechargementMedia);
		media.setAuteurMedia(utilisateurConnecte);
		
		// Et enfin, on ajoute le fichier source du Media :
		try {
			File fichierCree = ecrireFichierUploade(fichierSelectionne);
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
		files.remove(fichierSelectionne);
		
		// On remet les valeurs par défaut
	    nomMedia = "";
	    photoMedia = null;
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

    	 
        File fileUploaded = new File(chemin + getTimeStamp() + file.getExtension());
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
    
    
    public void handleFileUpload(FileUploadEvent event) {  
    	
    	// On récupère l'item uploadé
        UploadedFile item = event.getFile();
        
        // On crée un FichierUpload pour l'affichage des informations
        FichierUpload file = new FichierUpload();
        file.setTaille(item.getSize());
        file.setNom(item.getFileName().substring(0,item.getFileName().length()-4));
        file.setExtension(item.getFileName().substring(item.getFileName().length()-4));
        file.setData(item.getContents());
        
        file.reCalculerTaille();
        
        // On ajoute le fichierUpload à la liste
        files.add(file);
        
        // On affiche un message à l'utilisateur
        FacesMessage msg = new FacesMessage("Le fichier : ", file.getNom() + " a été uploadé avec succès !");  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }     
    
    public void handlePictureUpload() {  
    	// On affiche un message à l'utilisateur
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
    	if (file.getExtension().matches("(.*)(mp3|wav|riff|ogg)(.*)")) {
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
    	if (file.getExtension().matches("(.*)(mp4|avi)(.*)")) {
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
    	if (file.getExtension().matches("(.*)(png|jpg|JPG|jpeg|JPEG)(.*)")) {
    		return true;
    	}
    	
    	return false;
    }    
    
    public String supprimerFichiersUploades() {
        
    	files.clear();
    
    	return null;
    }
    
    public String supprimerFichierUploade() {
        
    	files.remove(fichierSelectionne);
    
    	return null;
    }    
 
    public long getTimeStamp() {
        return System.currentTimeMillis();
    }
    
    public int getSize() {
        if (getFiles().size() > 0) {
            return getFiles().size();
        } else {
            return 0;
        }
    }   
    
    // GETTER SETTER
    
	public ArrayList<FichierUpload> getFiles() {
		return files;
	}


	public void setFiles(ArrayList<FichierUpload> files) {
		this.files = files;
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

	public FichierUpload getFichierSelectionne() {
		return fichierSelectionne;
	}

	public void setFichierSelectionne(FichierUpload fichierSelectionne) {
		this.fichierSelectionne = fichierSelectionne;
	}

	public Long getTypeMedia() {
		return typeMedia;
	}

	public void setTypeMedia(Long typeMedia) {
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

	public Long getPorteeMedia() {
		return porteeMedia;
	}

	public void setPorteeMedia(Long porteeMedia) {
		this.porteeMedia = porteeMedia;
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

	public Long getCategorieMedia() {
		return categorieMedia;
	}

	public void setCategorieMedia(Long categorieMedia) {
		this.categorieMedia = categorieMedia;
	}

	public boolean isAfficherListeUploades() {
		return afficherListeUploades;
	}

	public void setAfficherListeUploades(boolean afficherListeUploades) {
		this.afficherListeUploades = afficherListeUploades;
	}

	public ArrayList<Type_Media> getLstTypesMedia() {
		return lstTypesMedia;
	}

	public void setLstTypesMedia(ArrayList<Type_Media> lstTypesMedia) {
		this.lstTypesMedia = lstTypesMedia;
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

	public ArrayList<Long> getCategoriesMedia() {
		return categoriesMedia;
	}

	public void setCategoriesMedia(ArrayList<Long> categoriesMedia) {
		this.categoriesMedia = categoriesMedia;
	}


	public ArrayList<Visibilite> getLstVisibilites() {
		return lstVisibilites;
	}

	public void setLstVisibilites(ArrayList<Visibilite> lstVisibilites) {
		this.lstVisibilites = lstVisibilites;
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
