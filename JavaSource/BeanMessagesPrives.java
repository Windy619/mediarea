import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.Pattern;

import metier.utilisateur.Message_Prive;
import metier.utilisateur.Notification;
import metier.utilisateur.Signalement_MessagePrive;
import metier.utilisateur.Utilisateur;

import org.primefaces.event.SelectEvent;

import dao.utilisateur.DaoMessagePrive;
import dao.utilisateur.DaoNotification;
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
	private DaoNotification daoNotification;
	
	// Propriétés
	@Pattern(regexp = "^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[a-zA-Z]{2,4}$", message = "Mauvaise adresse mail - Veuillez corriger")
	private String destinataire;
	private String objet;
	private String contenu;
	private Message_Prive mpSelectionne;
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
		daoSignalementMP = new DaoSignalementMessagePrive();
		
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
		
		return "chargerMessages";
	}
	
	/**
	 * Réponses d'un message
	 * @return
	 */
	public String chargerReponses(Message_Prive mpSelectionne) {
		
		// On charge les liste des réponses
		reponses = new ArrayList(daoMessagePrive.getReponses(mpSelectionne,utilisateurConnecte));
		
		// On met à jour la date de lecture des messages
		for (Message_Prive mp : reponses) {
			if (mp.getDestinataire() == utilisateurConnecte && mp.getDateLecture() == null) {
				mp.setDateLecture(new Date());
			}
		}
		
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
		
		
		return "chargerMessages";
	}		
	
	/**
	 * Suppression d'un message
	 * @return
	 */
	public String supprimerMessage() {
					
		System.out.println(mpSelectionne);
		// En fonction de si on est emetteur ou destinnataire
		if (mpSelectionne.getEmetteur() == utilisateurConnecte) {
			mpSelectionne.setDateSuppressionMessage(new Date());
		} else {
			mpSelectionne.setDateSuppressionMessageDestinataire(new Date());
		}
		
		// On enregistre les modifications
		daoUtilisateur.sauvegarder(utilisateurConnecte);
		
        // On affiche un message à l'utilisateur
        FacesMessage msg = new FacesMessage("Message supprimé avec succès !");  
        FacesContext.getCurrentInstance().addMessage(null, msg); 		
		
		return "supprimerMessages";
	}	
	
	/**
	 * On vide le formulaire
	 */
	public String viderFormulaire() {
		destinataire = "";
		objet = "";
		contenu = "";
		
		return "viderformulaire";
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
		
        // On affiche un message à l'utilisateur
        FacesMessage msg = new FacesMessage("Réponse envoyé avec succès !");  
        FacesContext.getCurrentInstance().addMessage(null, msg); 		
		
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
			
			// On vide le formulaire
			viderFormulaire();
			
			// On crée une notification
			Notification notification = new Notification("Un message privé vous à été envoyé de \"" + utilisateurConnecte.getPseudo() + "\"", uDestinataire);
			notification.setDateEnvoiNotification(new Date());
			// On l'ajoute à l'utilisateur concerné et on le sauvegarde
			uDestinataire.getNotifications().add(notification);
			daoUtilisateur.sauvegarder(uDestinataire);		
			
	        // On affiche un message à l'utilisateur
	        FacesMessage msg = new FacesMessage("Message envoyé avec succès !");  
	        FacesContext.getCurrentInstance().addMessage(null, msg); 	
	        
		}

		return "envoyerMessage";
	}
	
	/**
	 * Signalement d'un message
	 * @return
	 */
	public String signalerMessage() {
		
		System.out.println(mpSelectionne);
		
		// On ne se signale pas nous même ...
		if (mpSelectionne.getEmetteur() != utilisateurConnecte) {
			// On crée le signalement
			Signalement_MessagePrive sMP = new Signalement_MessagePrive(mpSelectionne);
			// Et le sauvegarde
			daoSignalementMP.sauvegarder(sMP);
			
			// On crée une notification
			Notification notification = new Notification("Votre message : \"" + mpSelectionne.getContenuMessage() + "\" a fait l'objet d'un signalement !",mpSelectionne.getDestinataire());
			notification.setDateEnvoiNotification(new Date());
			// On l'ajoute à l'utilisateur concerné et on le sauvegarde
			mpSelectionne.getDestinataire().getNotifications().add(notification);
			daoUtilisateur.sauvegarder(mpSelectionne.getDestinataire());

			System.out.println("Message " + mpSelectionne.getContenuMessage() + " signalé !");
			
	        // On affiche un message à l'utilisateur
	        FacesMessage msg = new FacesMessage("Signalement effectué avec succès !");  
	        FacesContext.getCurrentInstance().addMessage(null, msg); 
	        
		} else {
	        // On affiche un message à l'utilisateur
	        FacesMessage msg = new FacesMessage("Vous ne pouvez pas vous signaler vous même ... si le message que vous avez écrit est hors charte ou hors règle, vous en subirez cependant les conséquences !");  
	        FacesContext.getCurrentInstance().addMessage(null, msg); 			
		}

		return "envoyerMessage";
	}	
	
	/**
	 * Affichage du formulaire de réponse de message
	 * @return
	 */
	public String afficherFormulaireReponseMessage(Message_Prive message) {
		
		System.out.println("afficherFormulaireReponseMessage MPSelectionne : " + mpSelectionne);
		if (mpSelectionne.getEmetteur() == utilisateurConnecte) {
			destinataire = mpSelectionne.getDestinataire().getAdrMail();
		} else {
			destinataire = mpSelectionne.getEmetteur().getAdrMail();
		}
		
		objet = mpSelectionne.getObjet();
		contenu = "";
				
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
		
		return "formNouveauMessage";
	}	
		
	
	/**
	 * Autocomplétion pour rechercher une adresse mail valide
	 * @param search
	 * @return
	 */
	public List<String> autoCompletion(String search) {
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
	/*public String envoyerEmail() { //TODO
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

	      try {
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
	      } catch (MessagingException mex) {
	         mex.printStackTrace();
	      }
	      
		return "envoyerEmail";
	}*/
	
	public ArrayList<Message_Prive> getMessagesFils(Message_Prive messageMere) {
		if (messageMere != null) {
			return new ArrayList(daoMessagePrive.getReponses(messageMere, utilisateurConnecte));
		} else {
			return new ArrayList<Message_Prive>();
		}
		
	}
	
    public void onRowSelect(SelectEvent event) {  
        Message_Prive mp = ((Message_Prive) event.getObject());  
        if (mp != null)
        	chargerReponses(mp); 
    }  	
    
    public String getCouleurDataTable(Message_Prive mp) {
    	if (mp.getEmetteur() == utilisateurConnecte && mp.getDateLecture() == null) {
    		return "color : green";
    	} else if (mp.getEmetteur() != utilisateurConnecte && mp.getDateLecture() == null) {
    		return "color : red";
    	} else {
    		return null;
    	}
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
