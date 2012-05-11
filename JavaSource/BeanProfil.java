import java.util.Date;
import metier.utilisateur.Avatar;
import metier.utilisateur.Utilisateur;
import dao.utilisateur.DaoUtilisateur;


public class BeanProfil {
	public DaoUtilisateur daoUtilisateur = new DaoUtilisateur();
	public BeanConnexion beanConnexion = new BeanConnexion();
	public Utilisateur user = new Utilisateur();
	public Avatar avatar;
	private java.lang.String autreAdrMail;
	private java.lang.Integer cp;
	private Date dateNaissance;
	private java.lang.Boolean estPublique;
	private java.lang.String interets;
	private java.lang.String nomUtilisateur;
	private java.lang.Boolean notificationAutomatique;
	private java.lang.Boolean notificationParMail;
	private java.lang.String numTelMobile;
	private java.lang.String pays;
	private java.lang.String prenomUtilisateur;
	private java.lang.String adrMail;
	private java.lang.String profession;
	private java.lang.String pseudo;
	private java.lang.String rue;
	private java.lang.String sexe;
	private java.lang.String ville;
	
	public BeanProfil() {
		adrMail = beanConnexion.getUser().getAdrMail();
		user = daoUtilisateur.rechercheSurAdrMail(adrMail);
	}
	
	public String update() {
		return "update";
	}

	public Avatar getAvatar() {
		return avatar;
	}

	public void setAvatar(Avatar avatar) {
		this.avatar = avatar;
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

	public java.lang.String getAdrMail() {
		return adrMail;
	}

	public void setAdrMail(java.lang.String adrMail) {
		this.adrMail = adrMail;
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

	public Utilisateur getUser() {
		return user;
	}

	public void setUser(Utilisateur user) {
		this.user = user;
	}	
	
	
	
}
