import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import metier.media.Media;
import metier.media.Playlist;
import metier.utilisateur.Utilisateur;
import javax.faces.event.ActionEvent;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.hibernate.ejb.criteria.expression.function.SubstringFunction;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import antlr.collections.List;

import dao.media.DaoMedia;
import dao.media.DaoPlaylist;
import dao.media.DaoTypePlaylist;
import dao.media.DaoVisibilite;
import dao.utilisateur.DaoUtilisateur;

public class BeanGestionPlaylist implements Serializable {
	private TreeNode root;
	private Object selectedNode;
	private TreeNode[] selectedNodes;
	public DaoPlaylist daoPlaylist = new DaoPlaylist();
	public DaoMedia daoMedia = new DaoMedia();
	public BeanConnexion beanConnexion = new BeanConnexion();
	private TreeNode playlistTreeNode;
	private TreeNode mediaTreeNode;
	private Utilisateur user = new Utilisateur();
	private DaoUtilisateur daoUtilisateur = new DaoUtilisateur();
	private ArrayList<Playlist> playlists = new ArrayList<Playlist>();
	private java.lang.String nom;
	private java.lang.Boolean visible;
	private DaoTypePlaylist daoTypePlaylist = new DaoTypePlaylist();
	private DaoVisibilite daoVisibilite = new DaoVisibilite();

	public BeanGestionPlaylist() {
		user = beanConnexion.getUser();
		chargerPlaylist();
	}

	public void chargerPlaylist() {

		root = new DefaultTreeNode("root", null);

		playlists.clear();
		playlists = new ArrayList<Playlist>(beanConnexion.getUser()
				.getPlaylists());

		for (Playlist playlist : playlists) {
			playlistTreeNode = new DefaultTreeNode(playlist, root);
			ArrayList<Media> medialists = new ArrayList<Media>(
					playlist.getMedias());
			for (Media media : medialists) {
				mediaTreeNode = new DefaultTreeNode(media, playlistTreeNode);
			}
		}
	}

	public boolean evaluatePlaylist(Object object) {
		return object instanceof Playlist;
	}

	public boolean evaluateMedia(Object object) {
		return object instanceof Media;
	}

	public void delSelectedSingle(ActionEvent event) {
		if (selectedNode != null) {
			if (selectedNode.toString().contains("P")) {
				Playlist pl = daoPlaylist.rechercheSurID(Long
						.parseLong(selectedNode.toString().substring(1)));
				user.getPlaylists().remove(pl);
				daoUtilisateur.sauvegarder(user);
				chargerPlaylist();
			} else if (selectedNode.toString().contains("M")) {
				Media me = daoMedia.rechercheSurID(Long.parseLong(selectedNode
						.toString().substring(1)));
				for (Playlist listPlaylist : user.getPlaylists()) {
					if (listPlaylist.getMedias().contains(me)) { // Si la
																	// playlist
																	// contient
																	// le média
																	// recherché
						listPlaylist.getMedias().remove(me);
						daoPlaylist.sauvegarder(listPlaylist);
						daoUtilisateur.sauvegarder(user);
						chargerPlaylist();
					}
				}
			}
		}
	}

	public void create() {
		//CREATION PLAYLIST
		Set<Playlist> playlists = user.getPlaylists();
		if (visible == false) {
			playlists.add(new Playlist(nom, "playlist", "", daoTypePlaylist.typeAutre(), daoVisibilite.typeNonVisible()));
		} else {
			playlists.add(new Playlist(nom, "playlist", "", daoTypePlaylist.typeAutre(), daoVisibilite.typeVisible()));
		}
		user.setPlaylists(playlists);
		
		// SAVE IN BDD
		daoUtilisateur.sauvegarder(user);
		chargerPlaylist();
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public Object getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(Object selectedNode) {
		this.selectedNode = selectedNode;
	}

	public TreeNode[] getSelectedNodes() {
		return selectedNodes;
	}

	public void setSelectedNodes(TreeNode[] selectedNodes) {
		this.selectedNodes = selectedNodes;
	}

	public java.lang.String getNom() {
		return nom;
	}

	public void setNom(java.lang.String nom) {
		this.nom = nom;
	}

	public java.lang.Boolean getVisible() {
		return visible;
	}

	public void setVisible(java.lang.Boolean visible) {
		this.visible = visible;
	}
}