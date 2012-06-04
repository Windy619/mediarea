import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import metier.media.Playlist;
import metier.utilisateur.Avatar;
import metier.utilisateur.Utilisateur;

import org.primefaces.event.DragDropEvent;
import org.primefaces.event.FlowEvent;

import dao.media.DaoTypePlaylist;
import dao.media.DaoVisibilite;
import dao.utilisateur.DaoAvatar;
import dao.utilisateur.DaoUtilisateur;

public class BeanInscription {
	public DaoUtilisateur daoUtilisateur = new DaoUtilisateur();
	private DaoTypePlaylist daoTypePlaylist = new DaoTypePlaylist();
	private DaoVisibilite daoVisibilite = new DaoVisibilite();
	public BeanConnexion beanConnexion = new BeanConnexion();
	private FacesMessage message;
	@Pattern(regexp = "^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[a-zA-Z]{2,4}$", message = "Mauvaise adresse mail")
	private java.lang.String adrMail = null;
	private java.lang.String oldAdrMail = null;
	@Size(min = 3, max = 12, message = "La taille du mot de passe doit être constitué de 3 à 12 caractère alphanumérique")
	private java.lang.String password = null;
	private java.lang.String pseudo;
	private java.lang.String oldPseudo = null;
	public ArrayList<Avatar> listAvatar = new ArrayList<Avatar>();
	public DaoAvatar daoAvatar = new DaoAvatar();
	public Avatar avatar = null;
	private java.lang.String sexe;
	private java.lang.String nomUtilisateur;
	private java.lang.String prenomUtilisateur;
	private Date dateNaissance;
	private java.lang.String profession;
	private java.lang.String interets;
	private java.lang.String rue = "";
	private java.lang.Integer cp = 0;
	private java.lang.String ville = "";
	private java.lang.String pays = "";
	private java.lang.String autreAdrMail = "";
	private java.lang.String numTelMobile = "";
	private java.lang.Boolean estPublique;
	private java.lang.Boolean notificationAutomatique;
	private java.lang.Boolean notificationParMail;
	private java.lang.Boolean conditionsGenerales;
	private Utilisateur user;
	private boolean update = false;

	public BeanInscription() {
		if (beanConnexion.getUser() != null) {

			adrMail = beanConnexion.getUser().getAdrMail();
			user = daoUtilisateur.rechercheSurAdrMail(adrMail);
			if (user != null) {
				setUpdate(true);
				sexe = user.getSexe();
				nomUtilisateur = user.getNomUtilisateur();
				prenomUtilisateur = user.getPrenomUtilisateur();
				dateNaissance = user.getDateNaissance();
				profession = user.getProfession();

				rue = user.getRue();
				cp = user.getCp();
				ville = user.getVille();
				pays = user.getPays();
				adrMail = user.getAdrMail();
				oldAdrMail = user.getAdrMail();
				autreAdrMail = user.getAutreAdrMail();
				numTelMobile = user.getNumTelMobile();

				interets = user.getInterets();

				pseudo = user.getPseudo();
				oldPseudo = user.getPseudo();
				avatar = user.getAvatar();

				estPublique = user.isEstPublique();
				notificationAutomatique = user.isNotificationAutomatique();
				notificationParMail = user.isNotificationParMail();
			}
		}
	}

	public String save() {
		if (conditionsGenerales == false) {
			message = new FacesMessage(
					"Veuillez accepter les conditions générales d'utilisation");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(
					"form-subscribe:adrMail", message);
		} else {
			if (checkAdrMail()
					|| (!checkAdrMail() && oldAdrMail != null && oldAdrMail.equals(adrMail))) {
				if (checkPseudo()
						|| (!checkPseudo() && oldPseudo.equals(pseudo))) {
					if (update == false) {
						user = new Utilisateur();
					} else {
						user = daoUtilisateur.rechercheSurAdrMail(oldAdrMail);
					}

					if (update == false
							|| (update == true && !oldAdrMail.equals(adrMail))) {
						user.setAdrMail(adrMail);
					}

					user.setMdp(password);

					user.setPseudo(pseudo);

					user.setAvatar(avatar);

					user.setSexe(sexe);
					user.setNomUtilisateur(nomUtilisateur);
					user.setPrenomUtilisateur(prenomUtilisateur);
					user.setDateNaissance(dateNaissance);
					if (profession.isEmpty()) {
						profession = "non renseigné";
					}
					user.setProfession(profession);
					if (interets.isEmpty()) {
						interets = "non renseigné";
					}
					user.setInterets(interets);

					user.setRue(rue);
					user.setCp(cp);
					user.setVille(ville);
					user.setPays(pays);
					if (autreAdrMail.isEmpty()) {
						autreAdrMail = "non renseigné";
					}
					user.setAutreAdrMail(autreAdrMail);
					if (numTelMobile.isEmpty()) {
						numTelMobile = "non renseigné";
					}
					user.setNumTelMobile(numTelMobile);

					user.setEstPublique(estPublique);
					user.setNotificationAutomatique(notificationAutomatique);
					user.setNotificationParMail(notificationParMail);

					// CREATION PLAYLIST FAVORIS
					Set<Playlist> playlists = new HashSet<Playlist>();
					playlists.add(new Playlist("Favoris", "favoris", "",
							daoTypePlaylist.typeFavoris(), daoVisibilite
									.typeNonVisible()));
					user.setPlaylists(playlists);

					// SAVE IN BDD
					daoUtilisateur.sauvegarder(user);
					beanConnexion.seConnecter(adrMail);
					return beanConnexion.seDeconnecter();
				} else {
					message = new FacesMessage("Pseudo existant");
					message.setSeverity(FacesMessage.SEVERITY_ERROR);
					FacesContext.getCurrentInstance().addMessage(
							"form-subscribeSuite:pseudo", message);
				}
			} else {
				message = new FacesMessage("Adresse mail déjà existante");
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage(
						"form-subscribe:adrMail", message);
			}
		}
		return "";
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

	public boolean checkPseudo() {
		boolean isNotCreated;
		user = daoUtilisateur.rechercheSurPseudo(pseudo);
		if (user == null) {
			isNotCreated = true;
		} else {
			isNotCreated = false;
		}
		return isNotCreated;
	}

	/**
	 * On Drop d'un avatar dans la zone dropable
	 * 
	 * @param ddEvent
	 */
	public void onAvatarDrop(DragDropEvent ddEvent) {
		// On récupère l'avatar
		avatar = new Avatar();
		avatar = ((Avatar) ddEvent.getData());
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

	public java.lang.String getNomUtilisateur() {
		return nomUtilisateur;
	}

	public void setNomUtilisateur(java.lang.String nomUtilisateur) {
		this.nomUtilisateur = nomUtilisateur;
	}

	public java.lang.String getPrenomUtilisateur() {
		return prenomUtilisateur;
	}

	public void setPrenomUtilisateur(java.lang.String prenomUtilisateur) {
		this.prenomUtilisateur = prenomUtilisateur;
	}

	public java.lang.String getSexe() {
		return sexe;
	}

	public void setSexe(java.lang.String sexe) {
		this.sexe = sexe;
	}

	public Date getDateNaissance() {
		return dateNaissance;
	}

	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public java.lang.String getProfession() {
		return profession;
	}

	public void setProfession(java.lang.String profession) {
		this.profession = profession;
	}

	public java.lang.String getRue() {
		return rue;
	}

	public void setRue(java.lang.String rue) {
		this.rue = rue;
	}

	public java.lang.Integer getCp() {
		return cp;
	}

	public void setCp(java.lang.Integer cp) {
		this.cp = cp;
	}

	public java.lang.String getPays() {
		return pays;
	}

	public void setPays(java.lang.String pays) {
		this.pays = pays;
	}

	public java.lang.String getVille() {
		return ville;
	}

	public void setVille(java.lang.String ville) {
		this.ville = ville;
	}

	public java.lang.String getAutreAdrMail() {
		return autreAdrMail;
	}

	public void setAutreAdrMail(java.lang.String autreAdrMail) {
		this.autreAdrMail = autreAdrMail;
	}

	public java.lang.String getNumTelMobile() {
		return numTelMobile;
	}

	public void setNumTelMobile(java.lang.String numTelMobile) {
		this.numTelMobile = numTelMobile;
	}

	public java.lang.String getInterets() {
		return interets;
	}

	public void setInterets(java.lang.String interets) {
		this.interets = interets;
	}

	public java.lang.String getPseudo() {
		return pseudo;
	}

	public void setPseudo(java.lang.String pseudo) {
		this.pseudo = pseudo;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Avatar> getListAvatar() {
		listAvatar = (ArrayList<Avatar>) daoAvatar.listAvatar();
		return listAvatar;
	}

	public void setListAvatar(ArrayList<Avatar> listAvatar) {
		this.listAvatar = listAvatar;
	}

	public java.lang.Boolean getNotificationAutomatique() {
		return notificationAutomatique;
	}

	public void setNotificationAutomatique(
			java.lang.Boolean notificationAutomatique) {
		this.notificationAutomatique = notificationAutomatique;
	}

	public java.lang.Boolean getEstPublique() {
		return estPublique;
	}

	public void setEstPublique(java.lang.Boolean estPublique) {
		this.estPublique = estPublique;
	}

	public java.lang.Boolean getNotificationParMail() {
		return notificationParMail;
	}

	public void setNotificationParMail(java.lang.Boolean notificationParMail) {
		this.notificationParMail = notificationParMail;
	}

	public java.lang.Boolean getConditionsGenerales() {
		return conditionsGenerales;
	}

	public void setConditionsGenerales(java.lang.Boolean conditionsGenerales) {
		this.conditionsGenerales = conditionsGenerales;
	}

	public Avatar getAvatar() {
		return avatar;
	}

	public void setAvatar(Avatar avatar) {
		this.avatar = avatar;
	}

	public Utilisateur getUser() {
		return user;
	}

	public void setUser(Utilisateur user) {
		this.user = user;
	}

	public java.lang.String getOldAdrMail() {
		return oldAdrMail;
	}

	public void setOldAdrMail(java.lang.String oldAdrMail) {
		this.oldAdrMail = oldAdrMail;
	}

	public java.lang.String getOldPseudo() {
		return oldPseudo;
	}

	public void setOldPseudo(java.lang.String oldPseudo) {
		this.oldPseudo = oldPseudo;
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

}