import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import metier.utilisateur.Utilisateur;
import dao.utilisateur.DaoUtilisateur;

/**
 * @author Administrateur
 * 
 */
public class BeanConnexion {
	public DaoUtilisateur daoUtilisateur = new DaoUtilisateur();
	private java.lang.String connected = "";
	private static java.lang.Boolean isConnected = false;
	private FacesMessage message;
	private static Utilisateur User = null;
	@Pattern(regexp = "^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[a-zA-Z]{2,4}$", message = "Mauvaise adresse mail - Veuillez corriger")
	private java.lang.String identifiant = null;
	@Size(min = 3, max = 12, message = "La taille du Password doit être entre 3 et 12")
	private java.lang.String password = null;

	public BeanConnexion() {
	}

	public String getIsAccountOk() {
		if (identifiant != null){
			Utilisateur user = daoUtilisateur.rechercheSurAdrMail(identifiant);
			if (user == null) {
				connected = "isNotConnected";
				isConnected = false;
				message = new FacesMessage("Adresse mail inconnu");
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage(
						"form-login:identifiant", message);
			} else {
				if (user.getMdp().contentEquals(password)) {
					connected = "isConnected";
					isConnected = true;
					User = user;
				} else {
					connected = "isNotConnected";
					isConnected = false;
					message = new FacesMessage("Mot de passe incorrect");
					message.setSeverity(FacesMessage.SEVERITY_ERROR);
					FacesContext.getCurrentInstance().addMessage(
							"form-login:password", message);
				}
			}
		}
		return connected;
	}
	
	public String seConnecter(String aMail) {
		Utilisateur user = daoUtilisateur.rechercheSurAdrMail(aMail);
		connected = "isConnected";
		isConnected = true;
		User = user;
		return connected;
	}

	public String seDeconnecter() {
		connected = "isNotConnected";
		isConnected = false;
		User = null;
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		session.invalidate();
		return connected;
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

	public java.lang.String getConnected() {
		return connected;
	}

	public void setConnected(java.lang.String connected) {
		this.connected = connected;
	}

	public java.lang.Boolean getIsConnected() {
		return isConnected;
	}

	public void setIsConnected(java.lang.Boolean isConnected) {
		BeanConnexion.isConnected = isConnected;
	}

	public Utilisateur getUser() {
		return User;
	}

	public void setUser(Utilisateur user) {
		User = user;
	}

	
}
