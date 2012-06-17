
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import dao.media.DaoCategorie;
import dao.media.DaoMedia;
import metier.media.Categorie;
import metier.media.Media;


/**
 * 
 */

/**
 * @author Administrateur
 *
 */
public class BeanRecherche {
	String value;
	
	String typeMedia;
	String typeRecherche;
	
	String tri;
	String tri_ordre;
	String page;
	String nbPages;
	ArrayList<String> pagination;
	String nb_par_pages;
	String afficher_par;
	Boolean seulementTelechargeable;
	int nbMediasTrouves;
	HashMap<Long, Integer> l_mediaVues;
	HashMap<Long, Integer> l_mediaComs;
	
	ArrayList<Media> resRechMedia;
	DaoMedia daoMedia;
	
	
	String titre;
	String auteur;
	String description;
	String tag;
	Long categorie;
	ArrayList<Categorie> lstCategories;
	DaoCategorie daoCategorie;
	
	

	public BeanRecherche() {
		daoMedia = new DaoMedia();
		l_mediaVues = new HashMap<Long, Integer>();
		l_mediaComs = new HashMap<Long, Integer>();
		resRechMedia = new ArrayList<Media>();
		typeRecherche = "1";
		nbMediasTrouves = 0;
		page = "1";
		nbPages = "1";
		pagination = new ArrayList<String>();
		pagination.add("1");
		daoCategorie = new DaoCategorie();
		lstCategories = new ArrayList<Categorie>(daoCategorie.getTous());
	}
	
	public String rechercher(){
		typeRecherche = "1";
		titre = null;
		auteur = null;
		description = null;
		tag = null;
		categorie = null;
		page = "1";
		return "/pages/recherche?faces-redirect=true&amp;includeViewParams=true";
	}
	
	public String rechercherAvancee(){
		typeRecherche = "2";
		value = null;
		page = "1";
		return "/pages/recherche?faces-redirect=true&amp;includeViewParams=true";
	}
	
	public String processFiltre(){
		page = "1";
		return "/pages/recherche?faces-redirect=true&amp;includeViewParams=true";
	}
	
	public void processRecherchePre() {
		if(typeRecherche != null && !typeRecherche.equals("2"))
			typeRecherche = "1";
		processRecherche();
	}
	
	public String processRecherche() {
		HashMap<String, Object> resultat = daoMedia.recherche(value, typeMedia, seulementTelechargeable, tri, tri_ordre, page, nb_par_pages, titre, description, auteur, tag, categorie, typeRecherche);
		nbMediasTrouves = Integer.parseInt(resultat.get("nbMedias").toString());
		if(nbMediasTrouves > 0) {
			l_mediaVues = (HashMap<Long, Integer>)(resultat.get("l_nbVues"));
			l_mediaComs = (HashMap<Long, Integer>)(resultat.get("l_nbComs"));
			resRechMedia = new ArrayList((List<Media>)(resultat.get("l_medias")));
		}
		else {
			l_mediaVues.clear();
			l_mediaComs.clear();
			resRechMedia.clear();
		}
		majPagination();
		return "/pages/recherche?faces-redirect=true&amp;includeViewParams=true";
	}
	
	public void majPagination() {
		pagination.clear();
		int pageActuelle = Integer.parseInt(page);
		int nbParPage = Integer.parseInt(nb_par_pages);
		int nbPage = nbMediasTrouves/nbParPage;
		if(nbMediasTrouves%nbParPage > 0)
			nbPage++;
		if(nbPage <= 0)
			nbPage = 1;
		nbPages = Integer.toString(nbPage);
		int nbPageMax = 10;
		if(nbPage <= nbPageMax) {
			for(int i = 1; i <= nbPage; i++)
				pagination.add(Integer.toString(i));
		}
		else if (pageActuelle <= (nbPageMax/2)) {
			for(int i = 1; i <= nbPageMax; i++)
				pagination.add(Integer.toString(i));		
		}
		else {
			for(int i = (nbPage-(nbPageMax/2)-1); i <= (nbPage+((nbPageMax/2))); i++)
				pagination.add(Integer.toString(i));
		}	
	}
	
	public String changePage(String p) {
		//FacesContext context = FacesContext.getCurrentInstance();  
		page = p;
		return "/pages/recherche?faces-redirect=true&amp;includeViewParams=true";
	}
	
	public String accessMedia(Long id) {
		return "/pages/detailMedia?faces-redirect=true&amp;includeViewParams=true&v="+id;
	}
	
	public String clickOnPublisher(String pseudo) {
		value = null;
		titre = null;
		auteur = null;
		description = null;
		tag = null;
		categorie = null;
		typeRecherche = "2";
		page = "1";
		auteur = pseudo;
		return "/pages/recherche?faces-redirect=true&amp;includeViewParams=true";
	}
	
	public String rechercheParTitre(String _titre) {
		value = null;
		titre = null;
		auteur = null;
		description = null;
		tag = null;
		categorie = null;
		typeRecherche = "2";
		page = "1";
		titre = _titre;
		return "/pages/recherche?faces-redirect=true&amp;includeViewParams=true";
	}
	
	
	
	public Boolean getSeulementTelechargeable() {
		return seulementTelechargeable;
	}
	public void setSeulementTelechargeable(Boolean seulementTelechargeable) {
		this.seulementTelechargeable = seulementTelechargeable;
	}
	public String getAfficher_par() {
		return afficher_par;
	}
	public void setAfficher_par(String afficher_par) {
		this.afficher_par = afficher_par;
	}
	public String getTri() {
		return tri;
	}
	public void setTri(String tri) {
		this.tri = tri;
	}
	public String getTri_ordre() {
		return tri_ordre;
	}
	public void setTri_ordre(String tri_ordre) {
		this.tri_ordre = tri_ordre;
	}
	public String getNb_par_pages() {
		return nb_par_pages;
	}
	public void setNb_par_pages(String nb_par_pages) {
		this.nb_par_pages = nb_par_pages;
	}
	public DaoMedia getDaoMedia() {
		return daoMedia;
	}
	public void setDaoMedia(DaoMedia daoMedia) {
		this.daoMedia = daoMedia;
	}
	public ArrayList<Media> getResRechMedia() {
		return resRechMedia;
	}
	public void setResRechMedia(ArrayList<Media> resRechMedia) {
		this.resRechMedia = resRechMedia;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getTypeMedia() {
		return typeMedia;
	}
	public void setTypeMedia(String typeMedia) {
		this.typeMedia = typeMedia;
	}
	public String getTypeRecherche() {
		return typeRecherche;
	}
	public void setTypeRecherche(String typeRecherche) {
		this.typeRecherche = typeRecherche;
	}

	public HashMap<Long, Integer> getL_mediaVues() {
		return l_mediaVues;
	}

	public void setL_mediaVues(HashMap<Long, Integer> l_mediaVues) {
		this.l_mediaVues = l_mediaVues;
	}

	public HashMap<Long, Integer> getL_mediaComs() {
		return l_mediaComs;
	}

	public void setL_mediaComs(HashMap<Long, Integer> l_mediaComs) {
		this.l_mediaComs = l_mediaComs;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public int getNbMediasTrouves() {
		return nbMediasTrouves;
	}

	public void setNbMediasTrouves(int nbMediasTrouves) {
		this.nbMediasTrouves = nbMediasTrouves;
	}

	public String getNbPages() {
		return nbPages;
	}

	public void setNbPages(String nbPages) {
		this.nbPages = nbPages;
	}

	public ArrayList<String> getPagination() {
		return pagination;
	}

	public void setPagination(ArrayList<String> pagination) {
		this.pagination = pagination;
	}

	public Long getCategorie() {
		return categorie;
	}

	public void setCategorie(Long categorie) {
		this.categorie = categorie;
	}

	public ArrayList<Categorie> getLstCategories() {
		return lstCategories;
	}

	public void setLstCategories(ArrayList<Categorie> lstCategories) {
		this.lstCategories = lstCategories;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getAuteur() {
		return auteur;
	}

	public void setAuteur(String auteur) {
		this.auteur = auteur;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	
	
	
	
}
