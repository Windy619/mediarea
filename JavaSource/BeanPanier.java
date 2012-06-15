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

import metier.media.Media;
import metier.utilisateur.Utilisateur;


public class BeanPanier {
 	
 	// Propri�t�s
	private List<Media> mediaDansPanier;
 	private FacesMessage message;
 	
 	// Bean
 	private BeanMedia beanMedia;
	private BeanConnexion beanConnexion;
	
	// Utilisateur connect� actuellement
	private Utilisateur utilisateurConnecte;
	
	
	public BeanPanier() {
		// Chargement du m�dia visualis�
		beanMedia = (BeanMedia) FacesContext.getCurrentInstance().getCurrentInstance().getExternalContext().getSessionMap().get("beanMedia");
		beanConnexion = (BeanConnexion) FacesContext.getCurrentInstance().getCurrentInstance().getExternalContext().getSessionMap().get("beanConnexion");
		
		if (beanConnexion != null) {
			// R�cup�ration des informations de l'utilisateur connect�
			utilisateurConnecte = beanConnexion.getUser();
		}
				
		// Cr�ation de la liste qui contiendra tous les m�dias ajout�s au panier
		mediaDansPanier = new ArrayList<Media>();
	}
		
	/** 
	 * Ajout du m�dia au panier
	 * @return
	 */
	public String ajouterAuPanier() {
		System.out.println("ajouterAuPanier");
		
		if(utilisateurConnecte != null) {
			// Si le m�dia est d�j� ajout� au panier
			//if(mediaDansPanier.contains(beanMedia.getMediaVisualise())) {
			boolean existeMediaDansPanier = false;
			for (Media elMediaPanier : mediaDansPanier) {
				// Si le m�dia �tait d�j� contenu dans le panier
				if(elMediaPanier.equals(beanMedia.getMediaVisualise()))
				{
					System.out.println("Media �tait d�j� pr�sent dans panier");
					existeMediaDansPanier = true;				
	
					// Pr�paration du message de la notification
					message = new FacesMessage("Panier : " + "Le m�dia " + beanMedia.getMediaVisualise().getTitreMedia() + " a d�j� �t� ajout� au panier.");
					message.setSeverity(FacesMessage.SEVERITY_WARN);
					
					break;
				}
			}
			// Si le m�dia n'est pas encore ajout� au panier
			if(! existeMediaDansPanier) {
				//System.out.println("Media n'�tait pas pr�sent dans panier");
				mediaDansPanier.add(beanMedia.getMediaVisualise());
				
				// Pr�paration du message de la notification
				message = new FacesMessage("Panier : " + "Le m�dia " + beanMedia.getMediaVisualise().getTitreMedia() + " a �t� ajout� au panier");
			}
			
			/*System.out.println("Panier : ");
			for (Media mediaContenu : mediaDansPanier) {
				System.out.println(mediaContenu.getTitreMedia() + " - ");
			}*/
		}
		else {
			// Pr�paration du message de la notification
			message = new FacesMessage("Panier : Connectez-vous ou inscrivez-vous d�s maintenant !");
			message.setSeverity(FacesMessage.SEVERITY_WARN);
		}
		
		// Affichage de la notification
		FacesContext.getCurrentInstance().addMessage(null, message);
		
		return "ajouterAuPanier";
	}
	
	/** 
	 * T�l�chargement du panier
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
           		File f = new File(mediaDansPanier.get(i).getFichier().getCheminFichier()); // on r�cup�re le fichier
           		System.out.println("Le media : " + mediaDansPanier.get(i).getFichier().getCheminFichier());
                FileInputStream fis = new FileInputStream(f);
                BufferedInputStream bus = new BufferedInputStream(fis,BUFFER);

                ZipEntry entry = new ZipEntry(f.getName()); // nom du fichier dans l'archive
                zos.putNextEntry(entry);

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
