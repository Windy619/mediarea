import java.util.ArrayList;

import metier.utilisateur.Utilisateur;
import dao.utilisateur.DaoUtilisateur;


public class BeanMessagesPrives {

	// DAO
	private DaoUtilisateur daoUtilisateur;
	
	// Propriétés
	private ArrayList<Utilisateur> correspondances;
	
	// Utilisateur connecte actuellement
	private Utilisateur utilisateurConnecte;
	
	/**
	 * Constructeur du Bean
	 */
	public BeanMessagesPrives() {
		daoUtilisateur = new DaoUtilisateur();
		
		// Chargement de l'utilisateur connecte
		utilisateurConnecte = daoUtilisateur.getUn(1);	
		
		// Liste des coresspondances
		correspondances = new ArrayList(daoUtilisateur.getCorrespondancesPrives(utilisateurConnecte));	
	}

	public String chargerDiscussion() {
		return "chargerDiscussion";
	}
	
	public ArrayList<Utilisateur> getCorrespondances() {
		return correspondances;
	}

	public void setCorrespondances(ArrayList<Utilisateur> correspondances) {
		this.correspondances = correspondances;
	}
	
	
	// GETTER / SETTER
}
