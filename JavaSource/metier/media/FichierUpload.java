package metier.media;

/**
 * Représent un fichier upload
 * @author Benjamin
 *
 */
public class FichierUpload {

    private long taille;
    
    private String uniteTaille;
    
    private String nom;
    
    private byte[] data;
    
    private String extension;
 
    public FichierUpload() {
    	uniteTaille = "octet";
    }
    
    public void reCalculerTaille() {
    	uniteTaille = "octet";
    	if (taille > 1000000000) {
    		uniteTaille = "Go";
    		taille = taille / 1000000000;
    	} else if (taille > 1000000) {
    		uniteTaille = "Mo";
    		taille = taille / 1000000;    		
    	} else if (taille > 1000) {
    		uniteTaille = "Ko";
    		taille = taille / 1000;    		
    	}    	
    }

	public long getTaille() {
		return taille;
	}

	public void setTaille(long taille) {
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

	public String getUniteTaille() {
		return uniteTaille;
	}

	public void setUniteTaille(String uniteTaille) {
		this.uniteTaille = uniteTaille;
	}


	
    

}
