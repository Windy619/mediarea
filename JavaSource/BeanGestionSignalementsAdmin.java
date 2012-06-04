import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.event.ActionEvent;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import metier.media.Commentaire;
import metier.media.Media;
import metier.media.Playlist;
import metier.media.Signalement_Commentaire;
import metier.utilisateur.Utilisateur;
import dao.media.DaoCommentaire;
import dao.media.DaoMedia;
import dao.media.DaoPlaylist;
import dao.media.DaoSignalementCommentaire;
import dao.media.DaoTypePlaylist;
import dao.media.DaoVisibilite;
import dao.utilisateur.DaoUtilisateur;

public class BeanGestionSignalementsAdmin implements Serializable {
	private TreeNode root;
	private Object selectedNode;
	private TreeNode[] selectedNodes;
	private DaoSignalementCommentaire daoSignalementCommentaire = new DaoSignalementCommentaire();
	private DaoCommentaire daoCommentaire = new DaoCommentaire();
	private TreeNode signalementCommentaireTreeNode;
	private TreeNode commentaireTreeNode;
	private ArrayList<Commentaire> listCommentaires = new ArrayList<Commentaire>();

	public BeanGestionSignalementsAdmin() {
		chargerCommentairesSignales();
	}

	public void chargerCommentairesSignales() {

		root = new DefaultTreeNode("root", null);

		listCommentaires.clear();
		listCommentaires = new ArrayList<Commentaire>(daoCommentaire.getTous());
		for (Commentaire commentaire : listCommentaires) {
			ArrayList<Signalement_Commentaire> listSignalementCommentaire = (ArrayList<Signalement_Commentaire>) daoSignalementCommentaire
					.rechercheListSC(commentaire);
			if (listSignalementCommentaire.size() != 0) {
				commentaireTreeNode = new DefaultTreeNode(commentaire, root);
				for (Signalement_Commentaire signalementCommentaire : listSignalementCommentaire) {
					signalementCommentaireTreeNode = new DefaultTreeNode(
							signalementCommentaire, commentaireTreeNode);
				}
			}
		}
	}

	public boolean evaluateCommentaire(Object object) {
		return object instanceof Commentaire;
	}

	public boolean evaluateSignalementCommentaire(Object object) {
		return object instanceof Signalement_Commentaire;
	}

	public void delSelectedSingle(ActionEvent event) {
		if (selectedNode != null) {
			if (selectedNode.toString().contains("C")) {
				Commentaire c = daoCommentaire.getUn(Long
						.parseLong(selectedNode.toString().substring(1)));	
				ArrayList<Signalement_Commentaire> listSignalementCommentaires = new ArrayList<Signalement_Commentaire>();
				listSignalementCommentaires = (ArrayList<Signalement_Commentaire>) daoSignalementCommentaire.rechercheListSC(c);
				for (Signalement_Commentaire sc : listSignalementCommentaires) {
					Utilisateur u = new Utilisateur();
					DaoUtilisateur daoUtilisateur = new DaoUtilisateur();
					u = sc.getAuteurSignalementCommentaire();
					u.getSignalementsCommentaires().remove(sc);
					daoUtilisateur.sauvegarder(u);
					daoSignalementCommentaire.supprimer(sc);
				}
				
				for (Commentaire commFils : c.getCommentairesFils()) {
					daoCommentaire.supprimer(commFils);
				}
				
				daoCommentaire.supprimer(c);			
				chargerCommentairesSignales();
			} else if (selectedNode.toString().contains("S")) {
				Signalement_Commentaire s = daoSignalementCommentaire.getUn(Long.parseLong(selectedNode
						.toString().substring(1)));
				Utilisateur u = new Utilisateur();
				DaoUtilisateur daoUtilisateur = new DaoUtilisateur();
				u = s.getAuteurSignalementCommentaire();
				u.getSignalementsCommentaires().remove(s);
				daoUtilisateur.sauvegarder(u);
				daoSignalementCommentaire.supprimer(s);
				chargerCommentairesSignales();
			}
		}
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

}