import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.validation.constraints.Pattern;

import metier.utilisateur.Message_Prive;
import metier.utilisateur.Signalement_MessagePrive;
import metier.utilisateur.Utilisateur;
import dao.utilisateur.DaoMessagePrive;
import dao.utilisateur.DaoSignalementMessagePrive;
import dao.utilisateur.DaoUtilisateur;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;


public class BeanMessagesPrives {

	// DAO
	private DaoMessagePrive daoMessagePrive;
	private DaoUtilisateur daoUtilisateur;
	private DaoSignalementMessagePrive daoSignalementMP;
	
	// Propriétés
	@Pattern(regexp = "^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[a-zA-Z]{2,4}$", message = "Mauvaise adresse mail - Veuillez corriger")
	private String destinataire;
	private String objet;
	private String contenu;
	private Message_Prive mpSelectionne;
	private Message_Prive mpSelectionneDetail;
	private ArrayList<Message_Prive> messages;	
	private ArrayList<Message_Prive> reponses;	
	private ArrayList<String> suggestionUtilisateurs;
	private String rechercheMessage;
	
	// Compteur
	private Integer nbMessagesTotal;
	private Integer nbMessages;
	private Integer nbMessagesNonLus;
	
	// Utilisateur connecte actuellement
	private Utilisateur utilisateurConnecte;
	
	// Boolean
	private Boolean afficherNouveauMessage;
	private Boolean afficherReponseMessage;	
	private Boolean afficherDetailMessage;
	private Boolean afficherListeMessage;
	
	//Bean
	BeanConnexion beanConnexion;
	
	/**
	 * Constructeur du Bean
	 */
	public BeanMessagesPrives() {
		daoMessagePrive = new DaoMessagePrive();
		daoUtilisateur = new DaoUtilisateur();
		
		// Chargement de l'utilisateur connecte
		beanConnexion = (BeanConnexion) FacesContext.getCurrentInstance().getCurrentInstance().getExternalContext().getSessionMap().get("beanConnexion");
		
		nbMessages = 0;
		nbMessagesTotal = 0;
		nbMessagesNonLus = 0;
		
		afficherListeMessage = true;
		
		if (beanConnexion != null) {
			// ON charge l'utilisateur connecte
			utilisateurConnecte = beanConnexion.getUser();	
			// Si on trouve l'utilisateur, on charge ses messages
			if (utilisateurConnecte != null)
				chargerMessagesNonLus();
		}
		
	}

	/** 
	 * Chargement des messages de l'utilisateur
	 * @return
	 */
	public String chargerMessages() {
		
		// Récupération de la liste des messages
		messages = new ArrayList(daoMessagePrive.getMessages(utilisateurConnecte));
		
		// Calcule de différentes tailles
		nbMessagesTotal = messages.size();
		nbMessages = nbMessagesTotal;
		nbMessagesNonLus = daoMessagePrive.getMessagesNonLus(utilisateurConnecte).size();
		
		// Règles d'affichages dans la page
		afficherListeMessage = true;
		afficherDetailMessage = false;
		afficherNouveauMessage = false;
		afficherReponseMessage = false;
		
		return "chargerMessages";
	}
	
	/**
	 * Réponses d'un message
	 * @return
	 */
	public String chargerReponses() {
		
		// On charge les liste des réponses
		reponses = new ArrayList(daoMessagePrive.getReponses(mpSelectionne,utilisateurConnecte));
		
		return "chargerResponses";
	}
	
	/**
	 * Recherche d'un message
	 * @return
	 */
	public String rechercherMessages() {
		
		// On charge la liste des messages
		messages = new ArrayList(daoMessagePrive.rechercherMessage(rechercheMessage));
		
		// On calcul la taille
		nbMessages = messages.size();
		
		// Règles d'affichages dans la page
		afficherListeMessage = true;
		afficherDetailMessage = false;
		afficherNouveauMessage = false;
		afficherReponseMessage = false;
		
		return "chargerMessages";
	}	
	
	/**
	 * Chargement des messages envoyés
	 * @return
	 */
	public String chargerMessagesEnvoyes() {
		
		// On charge la liste des messages
		messages = new ArrayList(daoMessagePrive.getMessagesEnvoyes(utilisateurConnecte));
		
		// On calcul la taille
		nbMessages = messages.size();
		
		// Règles d'affichages dans la page
		afficherListeMessage = true;
		afficherDetailMessage = false;
		afficherNouveauMessage = false;
		afficherReponseMessage = false;
		
		return "chargerMessages";
	}	
	
	/**
	 * Chargement des messages recus
	 * @return
	 */
	public String chargerMessagesRecus() {
		
		// On charge la liste des messages
		messages = new ArrayList(daoMessagePrive.getMessagesRecus(utilisateurConnecte));
		
		// On calcul la taille
		nbMessages = messages.size();
		
		// Règles d'affichages dans la page
		afficherListeMessage = true;
		afficherDetailMessage = false;
		afficherNouveauMessage = false;
		afficherReponseMessage = false;
		
		return "chargerMessages";
	}	
	
	/**
	 * Chargement des messages recus
	 * @return
	 */
	public String chargerMessagesNonLus() {
		
		// On charge la liste des messages
		messages = new ArrayList(daoMessagePrive.getMessagesNonLus(utilisateurConnecte));
		
		// On calcul la taille
		nbMessages = messages.size();
		
		// Règles d'affichages dans la page
		afficherListeMessage = true;
		afficherDetailMessage = false;
		afficherNouveauMessage = false;
		afficherReponseMessage = false;
		
		
		return "chargerMessages";
	}		
	
	/**
	 * Suppression d'un message
	 * @return
	 */
	public String supprimerMessage() {
					
		// En fonction de si on est emetteur ou destinnataire
		if (mpSelectionne.getEmetteur() == utilisateurConnecte) {
			mpSelectionne.setDateSuppressionMessage(new Date());
		} else {
			mpSelectionne.setDateSuppressionMessageDestinataire(new Date());
		}
		
		// On enregistre les modifications
		daoUtilisateur.sauvegarder(utilisateurConnecte);
			
		// On recharges des listes si besoins
		
		if (afficherListeMessage) {
			chargerMessages();
		}
		
		if (afficherDetailMessage) {
			chargerReponses();
		}
		
		
		return "supprimerMessages";
	}	
	
	/**
	 * Réponde à un message
	 * @return
	 */
	public String repondreMessage() {
		
		// On récupère le bon utilisateur destinnataire
		Utilisateur uDestinataire = (Utilisateur)daoUtilisateur.rechercheSurAdrMail(destinataire);
		// Si on le trouve
		if (uDestinataire != null) {
			// On crée un nouveau message
			Message_Prive mp = new Message_Prive(contenu,utilisateurConnecte,uDestinataire,objet);
			mp.setIsMessageMere(false);
			
			// On l'ajoute aux réponses du message mère
			mpSelectionne.getReponses().add(mp);
			
			// On sauvegarde le tout
			daoMessagePrive.sauvegarder(mpSelectionne);
		}
		
		// On recharge la liste si besoin
		
		if (afficherDetailMessage) {
			chargerMessages();
		}
		
		return "repondreMessage";
	}
	
	/**
	 * Envoi d'un nouveau message
	 * @return
	 */
	public String envoyerMessage() {
				
		// On cherche l'utilisateur destinnataire
		Utilisateur uDestinataire = (Utilisateur)daoUtilisateur.rechercheSurAdrMail(destinataire);	
		
		// Si on le trouve
		if (uDestinataire != null) {
			
			// On ajoute un nouveau message (par défaut, celui ci est un message mère)
			utilisateurConnecte.getMessagesPrives().add(new Message_Prive(contenu,utilisateurConnecte,uDestinataire,objet));
			
			// On enregistre
			daoUtilisateur.sauvegarder(utilisateurConnecte);	
		}
				
		
		// On recharge des listes si besoins
		
		if (afficherListeMessage) {
			chargerMessages();
		}
		
		if (afficherDetailMessage) {
			chargerReponses();
		}

		return "envoyerMessage";
	}
	
	/**
	 * Signalement d'un message
	 * @return
	 */
	public String signalerMessage() {
		
		// On ne se signale pas nous même ...
		if (mpSelectionne.getEmetteur() != utilisateurConnecte) {
			// On crée le signalement
			Signalement_MessagePrive sMP = new Signalement_MessagePrive(mpSelectionne);
			// Et le sauvegarde
			daoSignalementMP.sauvegarder(sMP);
		}
		
		// On recharge des listes si besoins
		
		if (afficherListeMessage) {
			chargerMessages();
		}
		
		if (afficherDetailMessage) {
			chargerReponses();
		}

		return "envoyerMessage";
	}	
	
	/**
	 * Affichage du formulaire de réponse de message
	 * @return
	 */
	public String afficherFormulaireReponseMessage() {
		
		if (mpSelectionne.getEmetteur() == utilisateurConnecte) {
			destinataire = mpSelectionne.getDestinataire().getAdrMail();
		} else {
			destinataire = mpSelectionne.getEmetteur().getAdrMail();
		}
		
		objet = mpSelectionne.getObjet();
		contenu = "";
		
		afficherNouveauMessage = false;
		afficherReponseMessage = true;
		
		return "formReponseMessage";
	}
	
	/**
	 * Affichage du formulaire de nouveau message
	 * @return
	 */
	public String afficherFormulaireNouveauMessage() {
		
		destinataire = "";
		objet = "";
		contenu = "";
		
		afficherNouveauMessage = true;
		afficherReponseMessage = false;
		
		return "formNouveauMessage";
	}	
	
	/**
	 * Affichage du formulaire de détail d'un message
	 * @return
	 */
	public String afficherFormulaireDetailMessage() {
		
		afficherDetailMessage = true;
		afficherListeMessage = false;
		afficherNouveauMessage = false;
		afficherReponseMessage = false;
		
		mpSelectionne.setDateLecture(new Date());
		daoMessagePrive.sauvegarder(mpSelectionne);
		
		chargerReponses();
		
		return "formDetailMessage";
	}		
	
	/**
	 * Autocomplétion pour rechercher une adresse mail valide
	 * @param search
	 * @return
	 */
	public List<String> autoCompletion(String search) {
		System.out.println("autocomplete");
		
		// On cherche les utilisateurs
		List<?> lstUtilisateur = daoUtilisateur.recherche(search);
		
		// On créer une liste
		suggestionUtilisateurs = new ArrayList<String>();
		
		// On remplie la liste
		for (Object obj : lstUtilisateur) {
			suggestionUtilisateurs.add(((Utilisateur)obj).getAdrMail());
		}
		
		// On retourne les resultats
		return suggestionUtilisateurs;
	}
	
	/*
	 * Envoi d'un email
	 * @return
	 */
	public String envoyerEmail() { //TODO
		//http://www.tutorialspoint.com/java/java_sending_email.htm

		
		// Recipient's email ID needs to be mentioned.
	      String to = "abcd@gmail.com";

	      // Sender's email ID needs to be mentioned
	      String from = "web@gmail.com";

	      // Assuming you are sending email from localhost
	      String host = "localhost";

	      // Get system properties
	      Properties properties = System.getProperties();

	      // Setup mail server
	      properties.setProperty("mail.smtp.host", host);

	      // Get the default Session object.
	      Session session = Session.getDefaultInstance(properties);

	      try{
	         // Create a default MimeMessage object.
	         MimeMessage message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(from));

	         // Set To: header field of the header.
	         message.addRecipient(Message.RecipientType.TO,
	                                  new InternetAddress(to));

	         // Set Subject: header field
	         message.setSubject("This is the Subject Line!");

	         // Send the actual HTML message, as big as you like
	         message.setContent("<h1>... a partagé une vidéo avec vous sur MediArea : ...</h1>",
	                            "text/html" );

	         // Send message
	         Transport.send(message);
	         System.out.println("Sent message successfully....");
	      }catch (MessagingException mex) {
	         mex.printStackTrace();
	      }
	      
		return "envoyerEmail";
	}
		
	// GETTER / SETTER

	public ArrayList<Message_Prive> getMessages() {
		return messages;
	}

	public void setMessages(ArrayList<Message_Prive> messages) {
		this.messages = messages;
	}

	public Utilisateur getUtilisateurConnecte() {
		utilisateurConnecte = beanConnexion.getUser();
		return utilisateurConnecte;
	}

	public void setUtilisateurConnecte(Utilisateur utilisateurConnecte) {
		this.utilisateurConnecte = utilisateurConnecte;
	}

	public String getDestinataire() {
		return destinataire;
	}

	public void setDestinataire(String destinataire) {
		this.destinataire = destinataire;
	}

	public String getObjet() {
		return objet;
	}

	public void setObjet(String objet) {
		this.objet = objet;
	}

	public String getContenu() {
		return contenu;
	}

	public void setContenu(String contenu) {
		this.contenu = contenu;
	}

	public Message_Prive getMpSelectionne() {
		return mpSelectionne;
	}

	public void setMpSelectionne(Message_Prive mpSelectionne) {
		this.mpSelectionne = mpSelectionne;
	}

	public Integer getNbMessagesTotal() {
		return nbMessagesTotal;
	}

	public void setNbMessagesTotal(Integer nbMessagesTotal) {
		this.nbMessagesTotal = nbMessagesTotal;
	}

	public Integer getNbMessages() {
		return nbMessages;
	}

	public void setNbMessages(Integer nbMessages) {
		this.nbMessages = nbMessages;
	}

	public ArrayList<String> getSuggestionUtilisateurs() {
		return suggestionUtilisateurs;
	}

	public void setSuggestionUtilisateurs(ArrayList<String> suggestionUtilisateurs) {
		this.suggestionUtilisateurs = suggestionUtilisateurs;
	}

	public Boolean getAfficherNouveauMessage() {
		return afficherNouveauMessage;
	}

	public void setAfficherNouveauMessage(Boolean afficherNouveauMessage) {
		this.afficherNouveauMessage = afficherNouveauMessage;
	}

	public Boolean getAfficherReponseMessage() {
		return afficherReponseMessage;
	}

	public void setAfficherReponseMessage(Boolean afficherReponseMessage) {
		this.afficherReponseMessage = afficherReponseMessage;
	}

	public Boolean getAfficherDetailMessage() {
		return afficherDetailMessage;
	}

	public void setAfficherDetailMessage(Boolean afficherDetailMessage) {
		this.afficherDetailMessage = afficherDetailMessage;
	}

	public Boolean getAfficherListeMessage() {
		return afficherListeMessage;
	}

	public void setAfficherListeMessage(Boolean afficherListeMessage) {
		this.afficherListeMessage = afficherListeMessage;
	}

	public ArrayList<Message_Prive> getReponses() {
		return reponses;
	}

	public void setReponses(ArrayList<Message_Prive> reponses) {
		this.reponses = reponses;
	}

	public Message_Prive getMpSelectionneDetail() {
		return mpSelectionneDetail;
	}

	public void setMpSelectionneDetail(Message_Prive mpSelectionneDetail) {
		this.mpSelectionneDetail = mpSelectionneDetail;
	}

	public Integer getNbMessagesNonLus() {
		return nbMessagesNonLus;
	}

	public void setNbMessagesNonLus(Integer nbMessagesNonLus) {
		this.nbMessagesNonLus = nbMessagesNonLus;
	}

	public String getRechercheMessage() {
		return rechercheMessage;
	}

	public void setRechercheMessage(String rechercheMessage) {
		this.rechercheMessage = rechercheMessage;
	}	
	
	

	
}
