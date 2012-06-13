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

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import dao.media.DaoMedia;

import metier.media.Media;


public class BeanPanier {
	
	private List<Media> mediaDansPanier;

	public BeanPanier() {
		DaoMedia daoMedia = new DaoMedia();
		mediaDansPanier = new ArrayList<Media>();
		mediaDansPanier.add(daoMedia.getUn(1));
		mediaDansPanier.add(daoMedia.getUn(2));
		mediaDansPanier.add(daoMedia.getUn(3));
		mediaDansPanier.add(daoMedia.getUn(4));
		mediaDansPanier.add(daoMedia.getUn(5));
	}
	
	public String downloadPanier() {
		FileOutputStream fos;
        BufferedOutputStream bos;
        ZipOutputStream zos;
        ServletContext request = 
        		(ServletContext)FacesContext.getCurrentInstance().getExternalContext().getRequest();

        try {

            final int BUFFER = 4096; // taille du buffer

            byte[] buffer = new byte[BUFFER];

            fos = new FileOutputStream("monPanier.zip"); // nom de l'archive rar
            
            bos = new BufferedOutputStream(fos);
            zos = new ZipOutputStream(bos);


            for (int i = 0; i < mediaDansPanier.size(); i++) { // On boucle pour ajouter tout les fichier au rar
           		File f = new File(request.getRealPath(mediaDansPanier.get(i).getFichier().getCheminFichier())); // on r�cup�re le fichier

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

            String mimetype = null; // ???

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
            System.out.println("Toto erreur");
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
