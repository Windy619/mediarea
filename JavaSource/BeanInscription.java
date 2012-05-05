import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import metier.utilisateur.Utilisateur;
import dao.utilisateur.DaoUtilisateur;


public class BeanInscription {
	public DaoUtilisateur daoUtilisateur = new DaoUtilisateur();
	private FacesMessage message;
	private java.lang.String created = "";
	@Pattern(regexp = "^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[a-zA-Z]{2,4}$" , message="Mauvaise adresse mail")
	private java.lang.String adrMail = null;
	@Size(min=3, max=12, message="Taille doit être entre 3 et 12")
	private java.lang.String password = null;
	@Size(min=3, max=12, message="Taille doit être entre 3 et 12")
	private java.lang.String confirm = null;
	@AssertTrue(message = "Différents passwords ont été renseignés!")
    public boolean isPasswordsEquals() {
        return password.equals(confirm);
    }
	
	public BeanInscription() {
	}
	
	public String createAccount() {
		if (checkAdrMail()) {
			daoUtilisateur.sauvegarder(new Utilisateur(adrMail, false, null, password));
			created = "isCreated";
		} else {
			created = "isNotCreated";
			message = new FacesMessage("Adresse mail déjà existante");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(
					"form-subscribe:adrMail", message);
		}
		return created;
	}
	
	public boolean checkAdrMail() {
		boolean isAlreadyCreated;
		Utilisateur user = daoUtilisateur.rechercheSurAdrMail(adrMail);
		if (user != null) {
			isAlreadyCreated = false;
		} else {
			isAlreadyCreated = true;	
		}
		return isAlreadyCreated;
	}
	
	
	
	
	
	
	public java.lang.String getAdrMail() {
		return adrMail;
	}
	public void setAdrMail(java.lang.String adrMail) {
		this.adrMail = adrMail;
	}
	public java.lang.String getPassword() {
		return password;
	}
	public void setPassword(java.lang.String password) {
		this.password = password;
	}
	public java.lang.String getConfirm() {
		return confirm;
	}
	public void setConfirm(java.lang.String confirm) {
		this.confirm = confirm;
	}
	public java.lang.String getCreated() {
		return created;
	}
	public void setCreated(java.lang.String created) {
		this.created = created;
	}

	
}
