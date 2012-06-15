package dao.media;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import metier.media.Commentaire;
import dao.Dao;

public class DaoCommentaire extends Dao<Commentaire>{

	/**
	 * Constructeur
	 */
	public DaoCommentaire() {
		super(Commentaire.class);
	}

	
}
