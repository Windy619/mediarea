import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import dao.utilisateur.DaoUtilisateur;


public class BeanInscription {
	public DaoUtilisateur daoUtilisateur = new DaoUtilisateur();
	
	@Pattern(regexp = "^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[a-zA-Z]{2,4}$" , message="Mauvaise adresse mail - Veuillez corriger")
	private java.lang.String identifiant = null;
	@Size(min=3, max=12)
	private java.lang.String password = null;
	
	public BeanInscription() {
	}

	public java.lang.String getIdentifiant() {
		return identifiant;
	}

	public void setIdentifiant(java.lang.String identifiant) {
		this.identifiant = identifiant;
	}

	public java.lang.String getPassword() {
		return password;
	}

	public void setPassword(java.lang.String password) {
		this.password = password;
	}
}
