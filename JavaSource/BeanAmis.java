

import java.util.ArrayList;
import java.util.List;

import metier.utilisateur.Amitie;
import metier.utilisateur.Utilisateur;
import dao.utilisateur.DaoUtilisateur;

/**
 * @author Benjamin
 *
 */
public class BeanAmis {
	private String recherche;
	private List resultats;
	private List amis;
	private Utilisateur nouvelAmis;
	private DaoUtilisateur daoUtilisateur;
	private Utilisateur utilisateurConnecte;
	
	public BeanAmis() {
		daoUtilisateur = new DaoUtilisateur();
		
		utilisateurConnecte = daoUtilisateur.getUn(1);		
	}
	
	/**
	 * Recherche d'un utilisateur
	 */
	public String rechercher() {
		
		resultats = daoUtilisateur.recherche(recherche);
		chargerAmis();
				
		return "rechercher";
	}	
	

	public String ajouterAmis() {
		
		utilisateurConnecte.getAmis().add(new Amitie(nouvelAmis));
		daoUtilisateur.sauvegarder(utilisateurConnecte);

		return "ajouteramis";
	}	
	
	public String chargerAmis() {
		
		
		amis = new ArrayList<Object>(utilisateurConnecte.getAmis());
		
		daoUtilisateur.sauvegarder(new Utilisateur("test",true,"test","test"));

		return "chargeramis";
	}		
	

	public String getRecherche() {
		return recherche;
	}

	public void setRecherche(String recherche) {
		this.recherche = recherche;
	}

	public java.util.List getResultats() {
		return resultats;
	}

	public void setResultats(java.util.List resultats) {
		this.resultats = resultats;
	}

	public List getAmis() {
		return amis;
	}

	public void setAmis(List amis) {
		this.amis = amis;
	}	

	public Utilisateur getNouvelAmis() {
		return nouvelAmis;
	}

	public void setNouvelAmis(Utilisateur nouvelAmis) {
		this.nouvelAmis = nouvelAmis;
	}
	

}
