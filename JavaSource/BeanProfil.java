import metier.utilisateur.Utilisateur;
import dao.utilisateur.DaoUtilisateur;


public class BeanProfil {
	public DaoUtilisateur daoUtilisateur = new DaoUtilisateur();
	public BeanConnexion beanConnexion = new BeanConnexion();
	public Utilisateur user = new Utilisateur();
	private java.lang.String adrMail = "";
	
	public BeanProfil() {
		adrMail = beanConnexion.getUser().getAdrMail();
		user = daoUtilisateur.rechercheSurAdrMail(adrMail);
	}

	public Utilisateur getUser() {
		return user;
	}

	public void setUser(Utilisateur user) {
		this.user = user;
	}	
	
	
	
}
