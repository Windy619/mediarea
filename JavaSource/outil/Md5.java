package outil;

import java.security.MessageDigest;


/**
 * Classe permettant de crypter une chaine de caractere
 * @author Benjamin
 *
 */
public class Md5 {

	/**
	 * R�cup�ration du Hash (Md5) d'une chaine de caract�res
	 * @param chaine
	 * @return Le Hash
	 */
	public static String getHash(String chaine) {
		
		// Initialisation des structures n�c�ssaires
		byte[] uniqueKey = chaine.getBytes();
		byte[] hash      = null;	
		
		// Cr�ation du Hash
		try {
			hash = MessageDigest.getInstance("MD5").digest(uniqueKey);			
		} catch (Exception e) {
			System.err.println(e.getStackTrace());
		}

		// Reformation du hash en chaine de caract�re
		StringBuilder hashString = new StringBuilder();
		for (int i = 0; i < hash.length; i++) {
		        String hex = Integer.toHexString(hash[i]);
		        if (hex.length() == 1)
		        {
		                hashString.append('0');
		                hashString.append(hex.charAt(hex.length() - 1));
		        }
		        else
		                hashString.append(hex.substring(hex.length() - 2));
		}		
		
		// Return du String
		return hashString.toString();
	}
}
