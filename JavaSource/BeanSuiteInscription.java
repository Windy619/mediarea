import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.validation.constraints.Size;
import dao.utilisateur.DaoUtilisateur;
import metier.media.Visibilite;
import metier.utilisateur.Avatar;
import metier.utilisateur.Utilisateur;


public class BeanSuiteInscription {
	public DaoUtilisateur daoUtilisateur = new DaoUtilisateur();
	private FacesMessage message;
	private java.lang.String updated = "";
	private java.lang.String autreAdrMail;
	private java.lang.Integer cp;
	private Date dateNaissance;
	private java.lang.Boolean estPublique;
	private java.lang.String interets;
	private java.lang.String nomUtilisateur = null;
	private java.lang.Boolean notificationAutomatique;
	private java.lang.Boolean notificationParMail;
	private java.lang.String numTelMobile;
	private java.lang.String pays;
	private java.lang.String prenomUtilisateur = null;
	private java.lang.String profession;
	@Size(min=3, max=12, message="Taille doit être entre 3 et 12")
	private java.lang.String pseudo = null;
	private java.lang.String rue;
	private java.lang.String sexe;
	private java.lang.String ville;
	private Avatar avatar;
	private Visibilite visibilite;
	
	public BeanSuiteInscription() {
	}
	
	public boolean checkPseudo() {
		boolean isNotCreated;
		Utilisateur user = daoUtilisateur.rechercheSurPseudo(pseudo);
		if (user == null) {
			isNotCreated = true;
		} else {
			isNotCreated = false;
		}
		return isNotCreated;
	}
	
	public String updateAccount(){
		if (checkPseudo()) {
			daoUtilisateur.sauvegarder(new Utilisateur(nomUtilisateur, prenomUtilisateur, pseudo));
			updated = "isUpdated";
		} else {
			updated = "isNotUpdated";
			message = new FacesMessage("Pseudo existant");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(
					"form-subscribeSuite:pseudo", message);
		}		
		return updated;
	}
	
	
	
	
	public java.lang.String getAutreAdrMail() {
		return autreAdrMail;
	}

	public void setAutreAdrMail(java.lang.String autreAdrMail) {
		this.autreAdrMail = autreAdrMail;
	}

	public java.lang.Integer getCp() {
		return cp;
	}

	public void setCp(java.lang.Integer cp) {
		this.cp = cp;
	}

	public Date getDateNaissance() {
		return dateNaissance;
	}

	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public java.lang.Boolean getEstPublique() {
		return estPublique;
	}

	public void setEstPublique(java.lang.Boolean estPublique) {
		this.estPublique = estPublique;
	}

	public java.lang.String getInterets() {
		return interets;
	}

	public void setInterets(java.lang.String interets) {
		this.interets = interets;
	}

	public java.lang.String getNomUtilisateur() {
		return nomUtilisateur;
	}

	public void setNomUtilisateur(java.lang.String nomUtilisateur) {
		this.nomUtilisateur = nomUtilisateur;
	}

	public java.lang.Boolean getNotificationAutomatique() {
		return notificationAutomatique;
	}

	public void setNotificationAutomatique(java.lang.Boolean notificationAutomatique) {
		this.notificationAutomatique = notificationAutomatique;
	}

	public java.lang.Boolean getNotificationParMail() {
		return notificationParMail;
	}

	public void setNotificationParMail(java.lang.Boolean notificationParMail) {
		this.notificationParMail = notificationParMail;
	}

	public java.lang.String getNumTelMobile() {
		return numTelMobile;
	}

	public void setNumTelMobile(java.lang.String numTelMobile) {
		this.numTelMobile = numTelMobile;
	}

	public java.lang.String getPays() {
		return pays;
	}

	public void setPays(java.lang.String pays) {
		this.pays = pays;
	}

	public java.lang.String getPrenomUtilisateur() {
		return prenomUtilisateur;
	}

	public void setPrenomUtilisateur(java.lang.String prenomUtilisateur) {
		this.prenomUtilisateur = prenomUtilisateur;
	}

	public java.lang.String getProfession() {
		return profession;
	}

	public void setProfession(java.lang.String profession) {
		this.profession = profession;
	}

	public java.lang.String getPseudo() {
		return pseudo;
	}

	public void setPseudo(java.lang.String pseudo) {
		this.pseudo = pseudo;
	}

	public java.lang.String getRue() {
		return rue;
	}

	public void setRue(java.lang.String rue) {
		this.rue = rue;
	}

	public java.lang.String getSexe() {
		return sexe;
	}

	public void setSexe(java.lang.String sexe) {
		this.sexe = sexe;
	}

	public java.lang.String getVille() {
		return ville;
	}

	public void setVille(java.lang.String ville) {
		this.ville = ville;
	}

	public Avatar getAvatar() {
		return avatar;
	}

	public void setAvatar(Avatar avatar) {
		this.avatar = avatar;
	}

	public Visibilite getVisibilite() {
		return visibilite;
	}

	public void setVisibilite(Visibilite visibilite) {
		this.visibilite = visibilite;
	}

	public java.lang.String getUpdated() {
		return updated;
	}

	public void setUpdated(java.lang.String updated) {
		this.updated = updated;
	}
	
	
}
