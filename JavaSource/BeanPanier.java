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
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import dao.media.DaoMedia;

import metier.media.Media;


public class BeanPanier {
	
	// DAO
 	private static DaoMedia daoMedia;
 	
 	// Propriétés
	private List<Media> mediaDansPanier;
 	private String detailNotifyAjoutAuPanier;
 	
 	// Bean
 	private BeanMedia beanMedia;

	public BeanPanier() {
		// Chargement du média visualisé
		beanMedia = (BeanMedia) FacesContext.getCurrentInstance().getCurrentInstance().getExternalContext().getSessionMap().get("beanMedia");
		
		//Instantiation des Dao
		DaoMedia daoMedia = new DaoMedia();
		
		mediaDansPanier = new ArrayList<Media>();
		//mediaDansPanier.add(daoMedia.getUn(29));
	}
		
	/** 
	 * Ajout du média au panier
	 * @return
	 */
	public String ajouterAuPanier() {
		System.out.println("ajouterAuPanier");
		
		//Si le média est déjà ajouté au panier
		//if(mediaDansPanier.contains(daoMedia.getUn(2))) {
		boolean existeMediaDansPanier = false;
		for (Media elMediaPanier : mediaDansPanier) {
			if(elMediaPanier.equals(beanMedia.getMediaVisualise()))
			{
				System.out.println("Media était déjà présent dans panier");
				detailNotifyAjoutAuPanier = "Le média " + beanMedia.getMediaVisualise().getTitreMedia() + " a déjà été ajouté au panier.";
				existeMediaDansPanier = true;
				break;
			}
		}
		//Si le média n'est pas encore ajouté au panier
		//else {
		if(! existeMediaDansPanier) {
			//System.out.println("Media n'était pas présent dans panier");
			mediaDansPanier.add(daoMedia.getUn(2));
			detailNotifyAjoutAuPanier = "Le média " + beanMedia.getMediaVisualise().getTitreMedia() + " a été ajouté au panier";
		}

		//Affichage de la notification
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Panier", detailNotifyAjoutAuPanier));
		
		/*System.out.println("Panier : ");
		for (Media mediaContenu : mediaDansPanier) {
			System.out.println(mediaContenu.getTitreMedia() + " - ");
		}*/
		
		
		return "ajouterAuPanier";
	}
	
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
           		File f = new File(mediaDansPanier.get(i).getFichier().getCheminFichier()); // on récupère le fichier
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
	
	public String getDetailNotifyAjoutAuPanier() {
		return detailNotifyAjoutAuPanier;
	}
	
	public void setDetailNotifyAjoutAuPanier(String detailNotifyAjoutAuPanier) {
		this.detailNotifyAjoutAuPanier = detailNotifyAjoutAuPanier;
	}
}
