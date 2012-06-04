import java.security.SecureRandom;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.validation.constraints.Pattern;
import dao.utilisateur.DaoUtilisateur;

import metier.utilisateur.Utilisateur;


public class BeanRecuperationMDP {
	@Pattern(regexp = "^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[a-zA-Z]{2,4}$", message = "Mauvaise adresse mail")
	private java.lang.String adrMail = null;
	public DaoUtilisateur daoUtilisateur = new DaoUtilisateur();
	private FacesMessage message;
	private Utilisateur user;

	public java.lang.String getAdrMail() {
		return adrMail;
	}

	public void setAdrMail(java.lang.String adrMail) {
		this.adrMail = adrMail;
	}
	
	public void send(){
		if(checkAdrMail()){
			message = new FacesMessage("Adresse mail non existante dans la Base De Donnée");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(
					"form-subscribe:adrMail", message);
		} else {
			System.out.println("@ mail ==> " + adrMail);
			String newMDP = Long.toHexString(Double.doubleToLongBits(Math.random()));
			System.out.println("STRING ==> " + newMDP);
			//ENVOI MDP PAR MAIL A FAIRE
			//sendMail();
			//MISE A JOUR DE LA BDD
			//user.setMdp(newMDP);
			//daoUtilisateur.sauvegarder(user);
		}
		
	}
	
	public boolean checkAdrMail() {
		boolean isAlreadyCreated;
		user = daoUtilisateur.rechercheSurAdrMail(adrMail);
		if (user != null) {
			isAlreadyCreated = false;
		} else {
			isAlreadyCreated = true;
		}
		return isAlreadyCreated;
	}
	
}
