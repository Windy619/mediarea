import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import metier.media.FichierUpload;
import metier.utilisateur.Utilisateur;

import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

import dao.utilisateur.DaoUtilisateur;


public class BeanUpload {

	// DAO
	DaoUtilisateur daoUtilisateur = new DaoUtilisateur();
	
	// Fichier a uploader
    private ArrayList<FichierUpload> files = new ArrayList<FichierUpload>();
    private FichierUpload fichierSelectionne;
    
    // Formulaire
    private String typeMedia;
    private String nomMedia;
    private String categorieMedia;
    private String photoMedia;
    private String tagsMedia;
    private String porteeMedia;
    private String motdepasseMedia;
    private String descriptionMedia;
    
    // Boolean
    private boolean afficherFormulaire;
    
	// Utilisateur connecte actuellement
	private Utilisateur utilisateurConnecte;
	
	//Bean
	BeanConnexion beanConnexion;    
    
	public BeanUpload() {
		daoUtilisateur = new DaoUtilisateur();
		
		// Chargement de l'utilisateur connecte
		beanConnexion = (BeanConnexion) FacesContext.getCurrentInstance().getCurrentInstance().getExternalContext().getSessionMap().get("beanConnexion");
		
		afficherFormulaire = false;
		
		if (beanConnexion != null) {
			// ON charge l'utilisateur connecte
			utilisateurConnecte = beanConnexion.getUser();	
			// Si on trouve l'utilisateur, on charge
			if (utilisateurConnecte != null) {
				
			}
				
		}		
	}
 
    public void listenerMedia(FileUploadEvent event) throws Exception {
    	
    	// On récupère l'item uploadé
        UploadedFile item = event.getUploadedFile();
        
        // On crée un FichierUpload pour l'affichage des informations
        FichierUpload file = new FichierUpload();
        file.setTaille(item.getData().length);
        file.setNom(item.getName());
        file.setData(item.getData());
        
        // On ajoute le fichierUpload à la liste
        files.add(file);
        
    }
    
    public void listenerPhoto(FileUploadEvent event) throws Exception {
    	
    	// On récupère l'item uploadé
        UploadedFile item = event.getUploadedFile();
        
        // On crée un FichierUpload pour l'affichage des informations
        FichierUpload file = new FichierUpload();
        file.setTaille(item.getData().length);
        file.setNom(item.getName());
        file.setData(item.getData());
        file.setExtension(file.getNom().substring(-3));
        
        // On ajoute le fichierUpload à la liste
        files.add(file);
        
    }    
    
    public void ecrireFichierUploade(FichierUpload file) throws IOException {
    	
        // On crée un File physique sur le seveur d'application avec pour nom le timestamp
        File fileUploaded = new File("./upload/tmp/" + getTimeStamp() + ".mp3");
        fileUploaded.createNewFile();
        
        // On écrit le fichier
        ByteArrayInputStream sourceFile = new ByteArrayInputStream(file.getData());
        
        try {
        	FileOutputStream destinationFile = new FileOutputStream(fileUploaded);
        	
        	byte buffer[] = new byte[512*1024];
        	int nbLecture;
        	
        	while ( ( nbLecture = sourceFile.read(buffer)) != -1) {
        		System.out.println("-" + nbLecture);
        		destinationFile.write(buffer, 0, nbLecture);
        	}
        	
        	destinationFile.close();
        	sourceFile.close();
        	
		} catch (Exception e) {
			// TODO: handle exception
		}    	
    }
    
    public String afficherFormulaireUpload() {

    	System.out.println(fichierSelectionne);
        nomMedia = fichierSelectionne.getNom();
        
    	afficherFormulaire = true;
    
    	return null;
    }
 
    public String supprimerFichierUploade() {
    	
    	System.out.println(fichierSelectionne);
    	
    	files.remove(fichierSelectionne);
    	
    	return null;
    }
    
    public String supprimerFichiersUploades() {
        files.clear();
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

	public String getTypeMedia() {
		return typeMedia;
	}

	public void setTypeMedia(String typeMedia) {
		this.typeMedia = typeMedia;
	}

	public String getNomMedia() {
		return nomMedia;
	}

	public void setNomMedia(String nomMedia) {
		this.nomMedia = nomMedia;
	}

	public String getPhotoMedia() {
		return photoMedia;
	}

	public void setPhotoMedia(String photoMedia) {
		this.photoMedia = photoMedia;
	}

	public String getTagsMedia() {
		return tagsMedia;
	}

	public void setTagsMedia(String tagsMedia) {
		this.tagsMedia = tagsMedia;
	}

	public String getPorteeMedia() {
		return porteeMedia;
	}

	public void setPorteeMedia(String porteeMedia) {
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

	public String getCategorieMedia() {
		return categorieMedia;
	}

	public void setCategorieMedia(String categorieMedia) {
		this.categorieMedia = categorieMedia;
	}

	
	
}
