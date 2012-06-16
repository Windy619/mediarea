

import java.util.Date;
import java.util.List;
import java.util.Set;

import metier.media.Aimer;
import metier.media.Categorie_Media;
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
import dao.utilisateur.DaoMessagePrive;
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
	public static DaoMessagePrive daoMessagePive = new DaoMessagePrive();
    
	
	
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
        utilAdmin.setNomUtilisateur("DreamTeam");
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
		
		/*Tag tag1 = new Tag("Tag1");
		Tag tag2 = new Tag("Tag2");
		Tag tag3 = new Tag("Tag3");
		Tag tag4 = new Tag("Tag4");
		Tag tag5 = new Tag("Tag5");
		Tag tag6 = new Tag("Tag6");*/
		daoMedia.getUn(1).getTags().add(new Tag("Tag1")); //NE PAS METTRE tags_idTag en PRIMARY KEY TODO
		daoMedia.getUn(1).getTags().add(new Tag("Tag2"));
		daoMedia.getUn(1).getTags().add(new Tag("Tag3"));
		media.getTags().add(new Tag("Tag4"));
		media.getTags().add(new Tag("Tag1"));
		media.getTags().add(new Tag("Tag3"));
		daoMedia.getUn(3).getTags().add(new Tag("Tag1"));
		daoMedia.getUn(3).getTags().add(new Tag("Tag5"));
		daoMedia.getUn(3).getTags().add(new Tag("Tag6"));
		
		
		media.getCommentaires().add(new Commentaire("Bonjour!",daoUtilisateur.getUn(0)));
		
		media.getCategories().add(new Categorie_Media(2,daoCategorie.categorie("CategorieTest").getIdCategorie()));	
		
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
        
        
        util.getMessagesMuraux().add(new Message_Mural());
        
        util.getMessagesPrives().add(new Message_Prive());
        util.getNotifications().add(new Notification());
        
        util.getAmis().add(new Amitie(util,daoUtilisateur.getUn(2)));
        util.getAmis().add(new Amitie(util,daoUtilisateur.getUn(3)));
        util.getAmis().add(new Amitie(util,daoUtilisateur.getUn(4)));
        
        util.getSignalementsUtilisateurs().add(new Signalement_Utilisateur("Pas gentil!",daoUtilisateur.getUn(4)));
        util.getMedias().add(daoMedia.getUn(4));
        util.getPlaylists().add(daoPlaylist.getUn(4));
        util.getCommentaires().add(new Commentaire());
        util.getAimeMedias().add(new Aimer(true,daoMedia.getUn(2)));
        util.getRegardeMedias().add(new Regarder(daoMedia.getUn(1)));
        util.getSignalementsMedias().add(new Signalement_Media("Pas conforme à la charte", daoMedia.getUn(3)));
        util.getNoteMedias().add(new Note(5,daoMedia.getUn(4)));
        util.getSignalementsCommentaires().add(new Signalement_Commentaire("Pas conforme a la charte !",daoCommentaire.getUn(1)));
        util.setAvatar(new Avatar("/resources/avatar/","1.png"));
        util.setAvatar(new Avatar("/resources/avatar/","2.png"));
        util.setAvatar(new Avatar("/resources/avatar/","3.png"));
        util.setAvatar(new Avatar("/resources/avatar/","4.png"));
        util.setAvatar(new Avatar("/resources/avatar/","5.png"));
        util.setAvatar(new Avatar("/resources/avatar/","6.png"));
        util.setAvatar(new Avatar("/resources/avatar/","7.png"));
        util.setAvatar(new Avatar("/resources/avatar/","8.png"));
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
        
        
        /*lister_utilisateurs();
        lister_medias();*/
		
        
        DaoUtilisateur dao = new DaoUtilisateur();
        Utilisateur u = dao.getUn(3);
        Avatar a = new Avatar("http://127.0.0.1:8080/MediArea/resources/avatar/8.png");
        u.setAvatar(a);
        dao.sauvegarder(u);
        
        u = dao.getUn(4);
        a = new Avatar("http://127.0.0.1:8080/MediArea/resources/avatar/2.png");
        u.setAvatar(a);
        dao.sauvegarder(u);  
        
        u = dao.getUn(5);
        a = new Avatar("http://127.0.0.1:8080/MediArea/resources/avatar/3.png");
        u.setAvatar(a);
        dao.sauvegarder(u); 
        
        u = dao.getUn(6);
        a = new Avatar("http://127.0.0.1:8080/MediArea/resources/avatar/4.png");
        u.setAvatar(a);
        dao.sauvegarder(u);         
        
        u = dao.getUn(7);
        a = new Avatar("http://127.0.0.1:8080/MediArea/resources/avatar/5.png");
        u.setAvatar(a);
        dao.sauvegarder(u);
        
        u = dao.getUn(1);
        a = new Avatar("http://127.0.0.1:8080/MediArea/resources/avatar/6.png");
        u.setAvatar(a);
        dao.sauvegarder(u);       
        
        u = dao.getUn(2);
        a = new Avatar("http://127.0.0.1:8080/MediArea/resources/avatar/7.png");
        u.setAvatar(a);
        dao.sauvegarder(u);
        
        /*Utilisateur u = daoUtilisateur.getUn(1);
        Set<Amitie> set = u.getAmis();
        for (Amitie a : set) {
			System.out.println(a.getAmi().getAdrMail());
		}
        
        System.out.println(set.isEmpty()); 
        System.out.println(set.size());*/
        
        Utilisateur u1 = daoUtilisateur.getUn(1);
        Utilisateur u2 = daoUtilisateur.getUn(2);
        Utilisateur u3 = daoUtilisateur.getUn(3);
        Utilisateur u4 = daoUtilisateur.getUn(1);
        
        Message_Prive mp1 = new Message_Prive("Bonjour, Cordialement",u1,u2,"Objet");
        Message_Prive mp2 = new Message_Prive("Bonjour, Cordialement",u1,u3,"Objet");
        Message_Prive mp3 = new Message_Prive("Bonjour, Cordialement",u1,u4,"Objet");
        Message_Prive mp4 = new Message_Prive("Bonjour, Cordialement",u1,u2,"Objet");
        Message_Prive mp5 = new Message_Prive("Bonjour, Cordialement",u1,u2,"Objet");
               
        
        u1.getMessagesPrives().add(mp1);
        u1.getMessagesPrives().add(mp2);
        u1.getMessagesPrives().add(mp3);
        u1.getMessagesPrives().add(mp4);
        u1.getMessagesPrives().add(mp5);
             
        
        daoUtilisateur.sauvegarder(u1);
        daoUtilisateur.sauvegarder(u2);
        daoUtilisateur.sauvegarder(u3);
        
        /*Utilisateur u = daoUtilisateur.getUn(2);
        
    	System.out.println(u.getMessagesPrives().size());
        
        Message_Prive mp = daoMessagePive.getUn(5);
        System.out.println(mp);
        u.getMessagesPrives().remove(mp);
        daoUtilisateur.sauvegarder(u);
        
    	System.out.println(u.getMessagesPrives().size());*/
    	
        //daoMessagePive.supprimer(mp);
        
        
        
        System.out.println("=============");

        System.out.println("Fin de session Hibernate");           
           
	}

}
