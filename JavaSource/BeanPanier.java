import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import dao.media.DaoMedia;
import dao.media.DaoTelechargementMedia;

import metier.media.Media;
import metier.media.Telechargement_Media;
import metier.utilisateur.Utilisateur;


public class BeanPanier {
 	
 	// Propriétés
	private List<Media> mediaDansPanier;
 	private FacesMessage message;
 	
 	// Bean
 	private BeanMedia beanMedia;
	private BeanConnexion beanConnexion;
	
	// Utilisateur connecté actuellement
	private Utilisateur utilisateurConnecte;
	
	// Dao
	private DaoTelechargementMedia daoTelechargement;
	
	
	public BeanPanier() {
		// Chargement du média visualisé
		beanMedia = (BeanMedia) FacesContext.getCurrentInstance().getCurrentInstance().getExternalContext().getSessionMap().get("beanMedia");
		beanConnexion = (BeanConnexion) FacesContext.getCurrentInstance().getCurrentInstance().getExternalContext().getSessionMap().get("beanConnexion");
		
		if (beanConnexion != null) {
			// Récupération des informations de l'utilisateur connecté
			utilisateurConnecte = beanConnexion.getUser();
		}
		
		// Initialisation DAO
		daoTelechargement = new DaoTelechargementMedia();
		
		// Création de la liste qui contiendra tous les médias ajoutés au panier
		mediaDansPanier = new ArrayList<Media>();
	}
		
	/** 
	 * Ajout du média au panier
	 * @return
	 */
	public String ajouterAuPanier() {
		System.out.println("ajouterAuPanier");
		
		if(utilisateurConnecte != null) {
			// Si le média est déjà ajouté au panier
			//if(mediaDansPanier.contains(beanMedia.getMediaVisualise())) {
			boolean existeMediaDansPanier = false;
			for (Media elMediaPanier : mediaDansPanier) {
				// Si le média était déjà contenu dans le panier
				if(elMediaPanier.equals(beanMedia.getMediaVisualise()))
				{
					System.out.println("Media était déjà présent dans panier");
					existeMediaDansPanier = true;				
	
					// Préparation du message de la notification
					message = new FacesMessage("Panier : " + "Le média " + beanMedia.getMediaVisualise().getTitreMedia() + " a déjà été ajouté au panier.");
					message.setSeverity(FacesMessage.SEVERITY_WARN);
					
					break;
				}
			}
			// Si le média n'est pas encore ajouté au panier
			if(! existeMediaDansPanier) {
				//System.out.println("Media n'était pas présent dans panier");
				mediaDansPanier.add(beanMedia.getMediaVisualise());
				
				// Préparation du message de la notification
				message = new FacesMessage("Panier : " + "Le média " + beanMedia.getMediaVisualise().getTitreMedia() + " a été ajouté au panier");
			}
			
			/*System.out.println("Panier : ");
			for (Media mediaContenu : mediaDansPanier) {
				System.out.println(mediaContenu.getTitreMedia() + " - ");
			}*/
		}
		else {
			// Préparation du message de la notification
			message = new FacesMessage("Panier : Connectez-vous ou inscrivez-vous dès maintenant !");
			message.setSeverity(FacesMessage.SEVERITY_WARN);
		}
		
		// Affichage de la notification
		FacesContext.getCurrentInstance().addMessage(null, message);
		
		return "ajouterAuPanier";
	}
	
	/** 
	 * Téléchargement du panier
	 * @return
	 */
	public String downloadPanier() {
		FileOutputStream fos;
        BufferedOutputStream bos;
        ZipOutputStream zos;

        try {

            final int BUFFER = 4096; // taille du buffer

            byte[] buffer = new byte[BUFFER];

            fos = new FileOutputStream("monPanier.zip"); // nom de l'archive rar
            
            bos = new BufferedOutputStream(fos);
            zos = new ZipOutputStream(bos);


            for (int i = 0; i < mediaDansPanier.size(); i++) { // On boucle pour ajouter tout les fichier au rar
           		// Stockage du fichier dans un File
            	File f = new File(mediaDansPanier.get(i).getFichier().getCheminFichier()); // on récupère le fichier
           		FileInputStream fis = new FileInputStream(f);
                BufferedInputStream bus = new BufferedInputStream(fis,BUFFER);
                // Renommage du fichier
                String nameMedia = Integer.toString(i+1) + "-" + mediaDansPanier.get(i).getTitreMedia() + "." + f.getName().split("\\.")[1];
                ZipEntry entry = new ZipEntry(nameMedia); // nom du fichier dans l'archive
                // Ajout dans le zip
                zos.putNextEntry(entry);
                
                
                // Gestion des stats de telechargement
                Telechargement_Media dl_media = daoTelechargement.getByIdmedia(mediaDansPanier.get(i).getIdMedia());
                if(dl_media != null) {
                	dl_media.setNbTelechargement(dl_media.getNbTelechargement()+1);
                }
                else {
                	dl_media = new Telechargement_Media(mediaDansPanier.get(i));
                }
                daoTelechargement.sauvegarder(dl_media);
        
                int count;
                while ((count = bus.read(buffer,0,BUFFER)) != -1) { // On ajoute le fichier au rar
                    zos.write(buffer, 0, count);
                }

                bus.close();
                fis.close();
            }

            zos.flush();
            zos.close();
            fos.close();
            bos.close();

        } catch (Exception ex) {
            System.out.println("Erreur pendant la compression");
        }
        
        File panierCompresse = new File("monPanier.zip");
        if (panierCompresse.isFile()) {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        
        ServletOutputStream os = null;
        try {
            int length = 0;
            os = response.getOutputStream();

            //String mimetype = null; // ???

            response.setContentType("application/zip");
            response.setContentLength((int) panierCompresse.length()); // taille du fichier
            response.setHeader("Content-Disposition", "attachment; filename=\"" + "monPanier.zip" + "\"");
            
            byte[] buffer = new byte[4096]; //taille du buffer

            DataInputStream in = new DataInputStream(new FileInputStream(panierCompresse));
            while ((in != null) && ((length = in.read(buffer)) != -1)) {
                os.write(buffer, 0, length);
            }

            in.close();
            os.flush();
            
        } catch (IOException ex) {
            System.out.println("erreur");
        }
        finally {
            try {
                if (os !=null){
                    os.close();
                }
                FacesContext.getCurrentInstance().responseComplete();
            } catch (IOException e) {
                System.out.println("test");
            }

        }
        }
        return null;
	}
	
	/** 
	 * Téléchargement d'un media
	 * @return
	 */
	public String downloadMedia(Media media) {
		FileOutputStream fos;
        BufferedOutputStream bos;
        ZipOutputStream zos;

        try {

            final int BUFFER = 4096; // taille du buffer

            byte[] buffer = new byte[BUFFER];

            fos = new FileOutputStream("mediarea.zip"); // nom de l'archive rar
            
            bos = new BufferedOutputStream(fos);
            zos = new ZipOutputStream(bos);
            	
        	// Stockage du fichier dans un File
        	File f = new File(media.getFichier().getCheminFichier()); // on récupère le fichier
       		FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bus = new BufferedInputStream(fis,BUFFER);
            // Renommage du fichier
            String nameMedia = media.getTitreMedia() + "." + f.getName().split("\\.")[1];
            ZipEntry entry = new ZipEntry(nameMedia); // nom du fichier dans l'archive
            // Ajout dans le zip
            zos.putNextEntry(entry);
            
            
            // Gestion des stats de telechargement
            Telechargement_Media dl_media = daoTelechargement.getByIdmedia(media.getIdMedia());
            if(dl_media != null) {
            	dl_media.setNbTelechargement(dl_media.getNbTelechargement()+1);
            }
            else {
            	dl_media = new Telechargement_Media(media);
            }
            daoTelechargement.sauvegarder(dl_media);
    
            int count;
            while ((count = bus.read(buffer,0,BUFFER)) != -1) { // On ajoute le fichier au rar
                zos.write(buffer, 0, count);
            }

            bus.close();
            fis.close();
            

            zos.flush();
            zos.close();
            fos.close();
            bos.close();

        } catch (Exception ex) {
            System.out.println("Erreur pendant la compression du media");
        }
        
        File panierCompresse = new File("mediarea.zip");
        if (panierCompresse.isFile()) {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        
        ServletOutputStream os = null;
        try {
            int length = 0;
            os = response.getOutputStream();

            //String mimetype = null; // ???

            response.setContentType("application/zip");
            response.setContentLength((int) panierCompresse.length()); // taille du fichier
            response.setHeader("Content-Disposition", "attachment; filename=\"" + "mediarea.zip" + "\"");
            
            byte[] buffer = new byte[4096]; //taille du buffer

            DataInputStream in = new DataInputStream(new FileInputStream(panierCompresse));
            while ((in != null) && ((length = in.read(buffer)) != -1)) {
                os.write(buffer, 0, length);
            }

            in.close();
            os.flush();
            
        } catch (IOException ex) {
            System.out.println("erreur");
        }
        finally {
            try {
                if (os !=null){
                    os.close();
                }
                FacesContext.getCurrentInstance().responseComplete();
            } catch (IOException e) {
                System.out.println("test");
            }

        }
        }
        return null;
	}
	
	public String vider() {
		mediaDansPanier.clear();
		return "panier.jsf";
	}
	
	public String supprimer(Media media) {
		mediaDansPanier.remove(media);
		return "panier.jsf";
	}

	public List<Media> getMediaDansPanier() {
		return mediaDansPanier;
	}

	public void setMediaDansPanier(List<Media> mediaDansPanier) {
		this.mediaDansPanier = mediaDansPanier;
	}
}
