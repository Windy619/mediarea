/**
 * 
 */

/**
 * @author Administrateur
 *
 */
public class BeanConnexion {
	private java.lang.String identifiant;
	private java.lang.String password;
	private java.lang.Boolean isConnected;

	public BeanConnexion() {
	}

	public java.lang.String getIdentifiant() {
		return identifiant;
	}

	public void setIdentifiant(java.lang.String identifiant) {
		this.identifiant = identifiant;
	}

	public java.lang.String getPassword() {
		return password;
	}

	public void setPassword(java.lang.String password) {
		this.password = password;
	}
	

	public java.lang.Boolean getIsConnected() {
		return isConnected;
	}

	public void setIsConnected(java.lang.Boolean isConnected) {
		this.isConnected = isConnected;
	}
	
	public String seConnecter() {
		if(identifiant.equals("david") && password.equals("dada")) {
			isConnected = true;
			return "connected";
		}
		else 
			return "nconnected";
	}
	
	public String seDeconnecter() {
		isConnected = false;
		return "deconnected";
	}
}
