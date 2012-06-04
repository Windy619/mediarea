import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.event.ActionEvent;
import dao.utilisateur.DaoUtilisateur;
import metier.utilisateur.Utilisateur;


public class BeanGestionUtilisateursAdmin {
	private List<Utilisateur> listUtilisateur = new ArrayList<Utilisateur>();
	private DaoUtilisateur daoUtilisateur = new DaoUtilisateur();
	private Utilisateur utilisateur = null;
	private Utilisateur selectedUser = null;
	
	
	public BeanGestionUtilisateursAdmin() {
		//RECUPERATION DE TOUS LES USERS NON BANNI ET NON SUPPRIMES
		chargerUtilisateur();
	}
	
	public void chargerUtilisateur() {
		listUtilisateur.clear();
		listUtilisateur = (List<Utilisateur>) daoUtilisateur.getValides();
	}
	
	public void chargerUtilisateurBannis() {
		listUtilisateur.clear();
		listUtilisateur = (List<Utilisateur>) daoUtilisateur.getBannis();
	}
	
	public void chargerUtilisateurSupprimes() {
		listUtilisateur.clear();
		listUtilisateur = (List<Utilisateur>) daoUtilisateur.getSupprimes();
	}
	
	public void delSelectedSingle(ActionEvent event) {
		if (selectedUser != null) {
			selectedUser.setDateSuppressionUtilisateur(new Date());
			daoUtilisateur.sauvegarder(selectedUser);		
			chargerUtilisateur();
		}
	}
	
	public void banSelectedSingle(ActionEvent event) {
		if (selectedUser != null) {
			selectedUser.setDateBanissement(new Date());
			daoUtilisateur.sauvegarder(selectedUser);		
			chargerUtilisateur();
		}
	}

	public List<Utilisateur> getListUtilisateur() {
		return listUtilisateur;
	}

	public void setListUtilisateur(List<Utilisateur> listUtilisateur) {
		this.listUtilisateur = listUtilisateur;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public Utilisateur getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(Utilisateur selectedUser) {
		this.selectedUser = selectedUser;
	}
	
}


