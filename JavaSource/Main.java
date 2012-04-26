

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import metier.media.Aimer;
import metier.media.Commentaire;
import metier.media.Media;
import metier.media.Note;
import metier.media.Photo_Couverture;
import metier.media.Playlist;
import metier.media.Regarder;
import metier.media.Signalement_Commentaire;
import metier.media.Signalement_Media;
import metier.media.Tag;
import metier.media.Visibilite;
import metier.utilisateur.Amitie;
import metier.utilisateur.Avatar;
import metier.utilisateur.Message_Mural;
import metier.utilisateur.Message_Prive;
import metier.utilisateur.Notification;
import metier.utilisateur.Signalement_Utilisateur;
import metier.utilisateur.Utilisateur;
import dao.media.DaoCategorie;
import dao.media.DaoCommentaire;
import dao.media.DaoMedia;
import dao.media.DaoPhotoCouverture;
import dao.media.DaoPlaylist;
import dao.media.DaoTypeMedia;
import dao.media.DaoTypePlaylist;
import dao.media.DaoVisibilite;
import dao.utilisateur.DaoUtilisateur;

public class Main {

	public static DaoUtilisateur daoUtilisateur = new DaoUtilisateur();
	public static DaoMedia daoMedia = new DaoMedia();
	public static DaoVisibilite daoVisibilite = new DaoVisibilite();
	public static DaoCategorie daoCategorie = new DaoCategorie();
	public static DaoPhotoCouverture daoPhoto = new DaoPhotoCouverture();
	public static DaoTypeMedia daoTypeMedia = new DaoTypeMedia();
	public static DaoTypePlaylist daoTypePlaylist = new DaoTypePlaylist();
	public static DaoPlaylist daoPlaylist = new DaoPlaylist();
	public static DaoCommentaire daoCommentaire = new DaoCommentaire();
	
	
	public static void init_types() {
		daoVisibilite.typeVisible();
		daoVisibilite.typeNonVisible();
		daoTypeMedia.typeSon();
		daoTypeMedia.typeVideo();
		daoTypePlaylist.typeAutre();
		daoTypePlaylist.typeFavoris();
		daoTypePlaylist.typeEnCours();
	}
	
	public static void creer_medias() {
		Visibilite visible = daoVisibilite.typeVisible();
		Visibilite nonVisible = daoVisibilite.typeNonVisible();
		
		daoMedia.sauvegarder(new Media(true, daoUtilisateur.getUn(1),"Description1",false,null,"La terre est Ronde",visible,null));
		daoMedia.sauvegarder(new Media(true, daoUtilisateur.getUn(1),"Description2",false,null,"Thor HD",visible,null));
		daoMedia.sauvegarder(new Media(true, daoUtilisateur.getUn(2),"Description3",false,null,"Hulk",nonVisible,null));
		daoMedia.sauvegarder(new Media(true, daoUtilisateur.getUn(3),"Description4",false,null,"Titre",nonVisible,null));
		daoMedia.sauvegarder(new Media(true, daoUtilisateur.getUn(4),"Description5",false,null,"Youtube1",visible,null));
		daoMedia.sauvegarder(new Media(true, daoUtilisateur.getUn(5),"Description6",false,null,"Dailymotion",visible,null));
	}
	
	public static void creer_utilisateurs() {
        
        Utilisateur utilAdmin = new Utilisateur("admin@gmail.com",true,"admin","password");
        utilAdmin.setEstAministrateur(true);
        daoUtilisateur.sauvegarder(utilAdmin);
        
        daoUtilisateur.sauvegarder(new Utilisateur("Benjamin.Marzolf@gmail.com",false,"Benjamin","password"));
        daoUtilisateur.sauvegarder(new Utilisateur("Jean@gmail.com",false,"Jean","password"));
        daoUtilisateur.sauvegarder(new Utilisateur("Marc@gmail.com",false,"Marc","password"));
        daoUtilisateur.sauvegarder(new Utilisateur("Luc@gmail.com",false,"Luc","password"));
        daoUtilisateur.sauvegarder(new Utilisateur("Isabelle@gmail.com",false,"Isabelle","password"));
        daoUtilisateur.sauvegarder(new Utilisateur("Louise@gmail.com",false,"Louise","password"));
        
        Utilisateur utilBanni = new Utilisateur("Banni@gmail.com",true,"Banni","password");
        utilBanni.setDateBanissement(new Date());
        daoUtilisateur.sauvegarder(utilBanni);		
	}
	
	public static void creer_Playlist() {
		
		daoPlaylist.sauvegarder(new Playlist("Ma playlist","Musique de rock", "Description",daoTypePlaylist.typeAutre(),daoVisibilite.typeVisible()));
		daoPlaylist.sauvegarder(new Playlist("Mes favoris","Favoris", "Description",daoTypePlaylist.typeFavoris(),daoVisibilite.typeVisible()));
		daoPlaylist.sauvegarder(new Playlist("En cours de lecture","En cours", "Description",daoTypePlaylist.typeEnCours(),daoVisibilite.typeVisible()));
		daoPlaylist.sauvegarder(new Playlist("Ma playlist2","Videos fun", "Description",daoTypePlaylist.typeAutre(),daoVisibilite.typeVisible()));
	}
	
	public static void creer_Commentaire() {
		
		daoCommentaire.sauvegarder(new Commentaire("Tous pourri -_-", daoUtilisateur.getUn(2)));
	}	
	
	public static void ajouter_objets_commentaire() {
		
		Commentaire commentaire = daoCommentaire.getUn(3);
		
		commentaire.getCommentairesFils().add(daoCommentaire.getUn(1));
		
		daoCommentaire.sauvegarder(commentaire);
	}
	
	public static void ajouter_objets_media() {
		Media media = daoMedia.getUn(2);
		
		media.setType(daoTypeMedia.typeVideo());
		
		media.getTags().add(new Tag("Tag"));
		
		media.getCommentaires().add(new Commentaire("Bonjour!",daoUtilisateur.getUn(0)));
		
		media.getCategories().add(daoCategorie.categorie("CategorieTest"));	
		
		media.setPhoto(new Photo_Couverture("RepPhoto","MaPhoto.png"));		
		
		daoMedia.sauvegarder(media);
	}
	
	public static void ajouter_objets_playlist() {
		Playlist playlist = daoPlaylist.getUn(2);
		
		playlist.getMedias().add(daoMedia.getUn(1));
		playlist.getMedias().add(daoMedia.getUn(2));
		playlist.getMedias().add(daoMedia.getUn(3));
		
		daoPlaylist.sauvegarder(playlist);
	}
	
	public static void ajouter_objets_utilisateur() {
		
        Utilisateur util = daoUtilisateur.getUn(1);
        
        util.getMessagesPrivesEnvoyes().add(new Message_Prive("Contenu", util, daoUtilisateur.getUn(2), "Objet"));
        util.getMessagesMuraux().add(new Message_Mural());
        util.getMessagesMurauxEnvoyes().add(new Message_Mural("Contenu", util, daoUtilisateur.getUn(2)));
        util.getMessagesPrives().add(new Message_Prive());
        util.getNotifications().add(new Notification());
        util.getAmis().add(new Amitie(daoUtilisateur.getUn(2)));
        util.getAmis().add(new Amitie(daoUtilisateur.getUn(3)));
        util.getAmis().add(new Amitie(daoUtilisateur.getUn(4)));
        util.getSignalementsUtilisateurs().add(new Signalement_Utilisateur("Pas gentil!",daoUtilisateur.getUn(4)));
        util.getMedias().add(new Media());
        util.getPlaylists().add(new Playlist());
        util.getCommentaires().add(new Commentaire());
        util.getAimeMedias().add(new Aimer(true,daoMedia.getUn(2)));
        util.getRegardeMedias().add(new Regarder(daoMedia.getUn(1)));
        util.getSignalementsMedias().add(new Signalement_Media("Pas conforme à la charte", daoMedia.getUn(3)));
        util.getNoteMedias().add(new Note(5,daoMedia.getUn(4)));
        util.getSignalementsCommentaires().add(new Signalement_Commentaire("Pas conforme a la charte !",daoCommentaire.getUn(1)));
        util.setAvatar(new Avatar("Fleur.jpg"));
        util.setVisibilite(daoVisibilite.typeVisible());
        
        daoUtilisateur.sauvegarder(util);		
	}
	
	public static void lister_utilisateurs() {

        System.out.println("Utilisateurs valides : ");
        List<?> lst2 = daoUtilisateur.getValides();
        for (Object object : lst2) {
        	System.out.println(((Utilisateur)object).getAdrMail());
		}
        System.out.println("");
        
        System.out.println("Utilisateurs non valides : ");
        List<?> lst3 = daoUtilisateur.getNonValides();
        for (Object object : lst3) {
        	System.out.println(((Utilisateur)object).getAdrMail());
		}
        System.out.println("");
        
        System.out.println("Utilisateurs admins : ");
        List<?> lst4 = daoUtilisateur.getAdministrateurs();
        for (Object object : lst4) {
        	System.out.println(((Utilisateur)object).getAdrMail());
		}
        System.out.println("");          
        
        System.out.println("Utilisateurs avec le pseudo Benjamin : ");
        List<?> lst5 = daoUtilisateur.getPseudo("Benjamin");
        for (Object object : lst5) {
        	System.out.println(((Utilisateur)object).getAdrMail());
		}
        System.out.println("");          
        
        System.out.println("Utilisateurs avec l'adrmail Luc@gmail.com : ");
        List<?> lst6 = daoUtilisateur.getMail("Luc@gmail.com");
        for (Object object : lst6) {
        	System.out.println(((Utilisateur)object).getAdrMail());
		}
        System.out.println("");        
        
        System.out.println("Tous : ");
        List<?> lst7 = daoUtilisateur.getTous();
        for (Object object : lst7) {
        	System.out.println(((Utilisateur)object).getAdrMail());
		}
        System.out.println("");
                		
	}
	
	public static void lister_medias() {
        System.out.println("Tous les medias :");
        List<?> liste = daoMedia.getTous();
        for (Object object : liste) {
			System.out.println(((Media)object).getTitreMedia());
		}
        
        System.out.println("Media qui correspondent à la recherche : 1");
        List<?> liste2 = daoMedia.recherche("1");
        for (Object object : liste2) {
			System.out.println(((Media)object).getTitreMedia());
		}        		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub


        System.out.println("Test");
        System.out.println("=============");
        
        
        // Méthodes créées pour vérifier le bon fonctionnement du mapping
        
        init_types();
        creer_utilisateurs();
        creer_Playlist();
        creer_medias();
        creer_Commentaire();
        ajouter_objets_utilisateur();
        ajouter_objets_media();
        ajouter_objets_playlist();
        ajouter_objets_commentaire();
        
        /*
        lister_utilisateurs();
        lister_medias();
		*/
        
        Utilisateur u = daoUtilisateur.getUn(1);
        
        System.out.println("Amis : ");
        List<?> liste = new ArrayList<Object>(u.getAmis());
        for (Object object : liste) {
			System.out.println(((Amitie)object).getAmi().getPseudo());
		}       
        
        
        System.out.println("=============");
        

        System.out.println("Fin de session Hibernate");           
        
        
	}

}
