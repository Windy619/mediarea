package metier.media;

/**
 * Représent un fichier upload
 * @author Benjamin
 *
 */
public class FichierUpload {

    private int taille;
    
    private String nom;
    
    private byte[] data;
    
    private String extension;
 
    public FichierUpload() {
 
    }

	public int getTaille() {
		return taille;
	}

	public void setTaille(int taille) {
		this.taille = taille;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

 
    

}
