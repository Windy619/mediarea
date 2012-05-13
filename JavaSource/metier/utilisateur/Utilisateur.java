package metier.utilisateur;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import metier.media.Aimer;
import metier.media.Commentaire;
import metier.media.Media;
import metier.media.Note;
import metier.media.Playlist;
import metier.media.Regarder;
import metier.media.Signalement_Commentaire;
import metier.media.Signalement_Media;
import metier.media.Telechargement_Media;
import metier.media.Visibilite;


/**
 * @author Benjamin
 * @version 1.0
 * @created 26-mars-2012 22:27:00
 */

@Entity
@NamedQueries({
	@NamedQuery(name = Utilisateur.NQ_VALIDES, query = "FROM Utilisateur WHERE dateBanissement = NULL AND dateSuppressionUtilisateur = NULL"),
	@NamedQuery(name = Utilisateur.NQ_NON_VALIDES, query = "FROM Utilisateur WHERE dateBanissement != NULL OR dateSuppressionUtilisateur != NULL"),
	@NamedQuery(name = Utilisateur.NQ_BANNIS, query = "FROM Utilisateur WHERE dateBanissement = NULL"),
	@NamedQuery(name = Utilisateur.NQ_SUPPRIMES, query = "FROM Utilisateur WHERE dateSuppressionUtilisateur = NULL"),
	@NamedQuery(name = Utilisateur.NQ_ADMINISTRATEURS, query = "FROM Utilisateur WHERE estAdministrateur IS TRUE"),
	@NamedQuery(name = Utilisateur.NQ_PSEUDO, query = "FROM Utilisateur WHERE pseudo = :pseudo"),
	@NamedQuery(name = Utilisateur.NQ_MAIL, query = "FROM Utilisateur WHERE adrMail = :mail")
})
/**
 * Class Utilisateur
 * Utilisateur du systeme
 * @author Benjamin
 *
 */
public class Utilisateur {


	/** Récupération des utilisateurs valides (non bannis, non supprimés) **/
	public static final String NQ_VALIDES = "utilisateur_valides";
	
	/** Récupération des utilisateurs non valides  **/
	public static final String NQ_NON_VALIDES = "utilisateur_non_valides";	
	
	/** Récupération des utilisateurs bannis  **/
	public static final String NQ_BANNIS = "utilisateur_bannis";	
	
	/** Récupération des utilisateurs non valides  **/
	public static final String NQ_SUPPRIMES = "utilisateur_supprimes";	
	
	/** Récupération des utilisateurs Administrateur  **/
	public static final String NQ_ADMINISTRATEURS = "utilisateur_administrateurs";		
	
	/** Récupération des utilisateurs par pseudo  **/
	public static final String NQ_PSEUDO = "utilisateur_pseudo";	
	
	/** Récupération des utilisateurs par pseudo  **/
	public static final String NQ_MAIL = "utilisateur_mail";		
	
	@Id
	@GeneratedValue
	private long idUtilisateur;
	
	private String adrMail;
	
	private String autreAdrMail;
	
	private int cp;
	
	private Date dateBanissement;
	
	private Date dateInscription;
	
	private Date dateNaissance;
	
	private Date dateSuppressionUtilisateur;
	
	private boolean estAdministrateur;
	
	private boolean estPublique;

	private String interets;
	
	private String mdp;
	
	private String nomUtilisateur;
	
	private boolean notificationAutomatique;
	
	private boolean notificationParMail;
	
	private String numTelMobile;
	
	private String pays;
	
	private String prenomUtilisateur;
	
	private String profession;
	
	private String pseudo;
	
	private String rue;
	
	private String sexe;
	
	private String ville;
	
	@ManyToOne(cascade = {CascadeType.ALL})
	private Avatar avatar;
	
	@ManyToOne(cascade = {CascadeType.ALL})
	private Visibilite visibilite;	
	
	@OneToMany(mappedBy="utilisateur", cascade = {CascadeType.ALL})
	private Set<Amitie> amis;
	
	@OneToMany(cascade = {CascadeType.ALL})
	private Set<Signalement_Utilisateur> signalementsUtilisateurs;
	
	@OneToMany(cascade = {CascadeType.ALL})
	private Set<Signalement_Media> signalementsMedias;	
	
	@OneToMany(cascade = {CascadeType.ALL})
	private Set<Signalement_Commentaire> signalementsCommentaires;		
	
	@OneToMany(cascade = {CascadeType.ALL})
	private Set<Notification> notifications;
	
	@OneToMany(cascade = {CascadeType.ALL})
	private Set<Media> medias;
	
	@OneToMany(cascade = {CascadeType.ALL})
	private Set<Playlist> playlists;
	
	@OneToMany(cascade = {CascadeType.ALL})
	private Set<Commentaire> commentaires;
	
	@OneToMany(cascade = {CascadeType.ALL})
	private Set<Aimer> aimeMedias;
	
	@OneToMany(cascade = {CascadeType.ALL})
	private Set<Regarder> regardeMedias;
	
	@OneToMany(cascade = {CascadeType.ALL})
	private Set<Note> noteMedias;
	
	@OneToMany(cascade = {CascadeType.ALL})
	private Set<Telechargement_Media> telechargementsMedias;	
	
	@OneToMany(cascade = {CascadeType.ALL})
	private Set<Message_Mural> messagesMuraux;
	
	@OneToMany(mappedBy = "emetteur", cascade = {CascadeType.ALL})
	private Set<Message_Prive> messagesPrives;
		
	
	public boolean isEstAdministrateur() {
		return estAdministrateur;
	}


	public void setEstAdministrateur(boolean estAdministrateur) {
		this.estAdministrateur = estAdministrateur;
	}


	/**
	 * Constructeur vide
	 */
	public Utilisateur(){
		adrMail = "";
		autreAdrMail = "";
		cp = 0;
		dateBanissement = null;
		dateInscription = new Date();
		dateNaissance = null;
		dateSuppressionUtilisateur = null;
		estAdministrateur = false;
		estPublique = false;
		interets = "";
		mdp = "";
		nomUtilisateur = "";
		notificationAutomatique = false;
		notificationParMail = false;
		numTelMobile = "";
		pays = "";
		prenomUtilisateur = "";
		profession = "";
		pseudo = "";
		rue = "";
		sexe = "";
		ville = "";
		avatar = null;
		amis = new HashSet<Amitie>();
		signalementsUtilisateurs  = new HashSet<Signalement_Utilisateur>();
		notifications = new HashSet<Notification>();
		medias  = new HashSet<Media>();
		playlists  = new HashSet<Playlist>();
		commentaires  = new HashSet<Commentaire>();
		visibilite = null;
		aimeMedias = new HashSet<Aimer>();
		regardeMedias = new HashSet<Regarder>();
		signalementsMedias = new HashSet<Signalement_Media>();
		noteMedias = new HashSet<Note>();
		signalementsCommentaires = new HashSet<Signalement_Commentaire>();
		messagesMuraux = new HashSet<Message_Mural>();
		messagesPrives = new HashSet<Message_Prive>();
		telechargementsMedias = new HashSet<Telechargement_Media>();
		
	}
	
	
	/**
	 * Constructeur par défaut
	 * @param adrMail
	 * @param estAministrateur
	 * @param pseudo
	 * @param mdp
	 */
	public Utilisateur (String adrMail, boolean estAministrateur, String pseudo, String mdp){
		this.adrMail = adrMail;
		this.estAdministrateur = estAministrateur;
		this.pseudo = pseudo;
		this.mdp = mdp;
		this.dateInscription = new Date();
		avatar = null;
		amis = new HashSet<Amitie>();
		signalementsUtilisateurs  = new HashSet<Signalement_Utilisateur>();
		notifications = new HashSet<Notification>();
		medias  = new HashSet<Media>();
		playlists  = new HashSet<Playlist>();
		commentaires  = new HashSet<Commentaire>();
		visibilite = null;
		aimeMedias = new HashSet<Aimer>();
		regardeMedias = new HashSet<Regarder>();
		signalementsMedias = new HashSet<Signalement_Media>();
		noteMedias = new HashSet<Note>();
		signalementsCommentaires = new HashSet<Signalement_Commentaire>();		
		notificationAutomatique = false;
		notificationParMail = false;	
		messagesMuraux = new HashSet<Message_Mural>();
		messagesPrives = new HashSet<Message_Prive>();
		telechargementsMedias = new HashSet<Telechargement_Media>();
	}	

	public Set<Message_Mural> getMessagesMuraux() {
		return messagesMuraux;
	}


	public void setMessagesMuraux(Set<Message_Mural> messagesMuraux) {
		this.messagesMuraux = messagesMuraux;
	}


	public Set<Message_Prive> getMessagesPrives() {
		return messagesPrives;
	}


	public void setMessagesPrives(Set<Message_Prive> messagesPrives) {
		this.messagesPrives = messagesPrives;
	}


	/**
	 * Suppression d'une instance
	 */
	public void finalize() throws Throwable {

	}

	// GETTER SETTER
	
	public String getAdrMail() {
		return adrMail;
	}

	public void setAdrMail(String adrMail) {
		this.adrMail = adrMail;
	}

	public String getAutreAdrMail() {
		return autreAdrMail;
	}

	public void setAutreAdrMail(String autreAdrMail) {
		this.autreAdrMail = autreAdrMail;
	}

	public int getCp() {
		return cp;
	}

	public void setCp(int cp) {
		this.cp = cp;
	}

	public Date getDateBanissement() {
		return dateBanissement;
	}

	public void setDateBanissement(Date dateBanissement) {
		this.dateBanissement = dateBanissement;
	}

	public Date getDateInscription() {
		return dateInscription;
	}

	public void setDateInscription(Date dateInscription) {
		this.dateInscription = dateInscription;
	}

	public Date getDateNaissance() {
		return dateNaissance;
	}

	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public Date getDateSuppressionUtilisateur() {
		return dateSuppressionUtilisateur;
	}

	public void setDateSuppressionUtilisateur(Date dateSuppressionUtilisateur) {
		this.dateSuppressionUtilisateur = dateSuppressionUtilisateur;
	}

	public boolean isEstAministrateur() {
		return estAdministrateur;
	}

	public void setEstAministrateur(boolean estAministrateur) {
		this.estAdministrateur = estAministrateur;
	}

	public boolean isEstPublique() {
		return estPublique;
	}

	public void setEstPublique(boolean estPublique) {
		this.estPublique = estPublique;
	}

	public long getIdUtilisateur() {
		return idUtilisateur;
	}

	public void setIdUtilisateur(long idUtilisateur) {
		this.idUtilisateur = idUtilisateur;
	}

	public String getInterets() {
		return interets;
	}

	public void setInterets(String interets) {
		this.interets = interets;
	}

	public String getMdp() {
		return mdp;
	}

	public void setMdp(String mdp) {
		this.mdp = mdp;
	}

	public String getNomUtilisateur() {
		
		return nomUtilisateur;
	}

	public void setNomUtilisateur(String nomUtilisateur) {
		
		this.nomUtilisateur = nomUtilisateur;
	}

	public boolean isNotificationAutomatique() {
		return notificationAutomatique;
	}

	public void setNotificationAutomatique(boolean notificationAutomatique) {
		this.notificationAutomatique = notificationAutomatique;
	}

	public boolean isNotificationParMail() {
		return notificationParMail;
	}

	public void setNotificationParMail(boolean notificationParMail) {
		this.notificationParMail = notificationParMail;
	}

	public String getNumTelMobile() {
		return numTelMobile;
	}

	public void setNumTelMobile(String numTelMobile) {
		this.numTelMobile = numTelMobile;
	}

	public String getPays() {
		return pays;
	}

	public void setPays(String pays) {
		this.pays = pays;
	}

	public String getPrenomUtilisateur() {
		return prenomUtilisateur;
	}

	public void setPrenomUtilisateur(String prenomUtilisateur) {
		this.prenomUtilisateur = prenomUtilisateur;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public String getRue() {
		return rue;
	}

	public void setRue(String rue) {
		this.rue = rue;
	}

	public String getSexe() {
		return sexe;
	}

	public void setSexe(String sexe) {
		this.sexe = sexe;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public Set<Amitie> getAmis() {
		return amis;
	}

	public void setAmis(Set<Amitie> amis) {
		this.amis = amis;
	}

	public Set<Signalement_Utilisateur> getSignalementsUtilisateurs() {
		return signalementsUtilisateurs;
	}

	public void setSignalementsUtilisateurs(
			Set<Signalement_Utilisateur> signalementsUtilisateurs) {
		this.signalementsUtilisateurs = signalementsUtilisateurs;
	}

	public Set<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(Set<Notification> notifications) {
		this.notifications = notifications;
	}

	public Avatar getAvatar() {
		return avatar;
	}

	public void setAvatar(Avatar avatar) {
		this.avatar = avatar;
	}

	public Set<Media> getMedias() {
		return medias;
	}

	public void setMedias(Set<Media> medias) {
		this.medias = medias;
	}

	public Set<Playlist> getPlaylists() {
		return playlists;
	}

	public void setPlaylists(Set<Playlist> playlists) {
		this.playlists = playlists;
	}

	public Set<Commentaire> getCommentaires() {
		return commentaires;
	}

	public void setCommentaires(Set<Commentaire> commentaires) {
		this.commentaires = commentaires;
	}

	public Visibilite getVisibilite() {
		return visibilite;
	}

	public void setVisibilite(Visibilite visibilite) {
		this.visibilite = visibilite;
	}

	public Set<Aimer> getAimeMedias() {
		return aimeMedias;
	}

	public void setAimeMedias(Set<Aimer> aimeMedias) {
		this.aimeMedias = aimeMedias;
	}

	public Set<Regarder> getRegardeMedias() {
		return regardeMedias;
	}

	public void setRegardeMedias(Set<Regarder> regardeMedias) {
		this.regardeMedias = regardeMedias;
	}

	public Set<Signalement_Media> getSignalementsMedias() {
		return signalementsMedias;
	}

	public void setSignalementsMedias(
			Set<Signalement_Media> signalementsMedias) {
		this.signalementsMedias = signalementsMedias;
	}

	public Set<Note> getNoteMedias() {
		return noteMedias;
	}

	public void setNoteMedias(Set<Note> noteMedias) {
		this.noteMedias = noteMedias;
	}

	public Set<Signalement_Commentaire> getSignalementsCommentaires() {
		return signalementsCommentaires;
	}

	public void setSignalementsCommentaires(
			Set<Signalement_Commentaire> signalementsCommentaires) {
		this.signalementsCommentaires = signalementsCommentaires;
	}


	public Set<Telechargement_Media> getTelechargementsMedias() {
		return telechargementsMedias;
	}


	public void setTelechargementsMedias(
			Set<Telechargement_Media> telechargementsMedias) {
		this.telechargementsMedias = telechargementsMedias;
	}

	
	
}