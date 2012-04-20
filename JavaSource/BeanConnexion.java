import java.util.List;

import dao.utilisateur.DaoUtilisateur;
import metier.utilisateur.Utilisateur;
import dao.Hibernate;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author Administrateur
 * 
 */
public class BeanConnexion {
	public DaoUtilisateur daoUtilisateur = new DaoUtilisateur();
	private java.lang.Boolean isConnected;
	@Pattern(regexp = "^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[a-zA-Z]{2,4}$" , message="Mauvaise adresse mail - Veuillez corriger")
	private java.lang.String identifiant = null;
	@Size(min=3, max=12)
	private java.lang.String password = null;
	
	public BeanConnexion() {
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

	public java.lang.Boolean getIsConnected() {
		return isConnected;
	}

	public void setIsConnected(java.lang.Boolean isConnected) {
		this.isConnected = isConnected;
	}
	
	
	
	@AssertTrue(message = "Comptes inexistant!")
	public boolean isAccountOk() {
		List<Utilisateur> listUser = (List<Utilisateur>) daoUtilisateur
				.recherche(identifiant);
		if (listUser.isEmpty()) {
			isConnected = false;
		} else {
			for (Utilisateur user : listUser) {
				if (user.getAdrMail().contentEquals(identifiant)) {
					if (user.getMdp().contentEquals(password)) {
						isConnected = true;
					}
				} else {
					isConnected = false;
				}
			}
		}
		return isConnected;
	}

	public String seDeconnecter() {
		isConnected = false;
		return "deconnected";
	}
}
