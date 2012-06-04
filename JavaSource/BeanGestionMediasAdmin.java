import java.util.ArrayList;
import java.util.List;
import javax.faces.event.ActionEvent;
import dao.media.DaoMedia;
import dao.media.DaoPlaylist;
import dao.media.DaoSignalementMedia;
import dao.utilisateur.DaoUtilisateur;
import metier.media.Media;
import metier.media.Playlist;
import metier.media.Signalement_Media;
import metier.utilisateur.Utilisateur;


public class BeanGestionMediasAdmin {
	private List<Media> listMedia = new ArrayList<Media>();
	private DaoMedia daoMedia = new DaoMedia();
	private DaoUtilisateur daoUtilisateur = new DaoUtilisateur();
	private DaoPlaylist daoPlaylist = new DaoPlaylist();
	private DaoSignalementMedia daoSignalementMedia = new DaoSignalementMedia();
	private Media media = null;
	private Media selectedMedia = null;
	
	
	public BeanGestionMediasAdmin() {
		chargerMedia();
	}
	
	public void chargerMedia() {
		listMedia.clear();
		listMedia = daoMedia.getTous();
	}
	
	public void delSelectedSingle(ActionEvent event) {
		if (selectedMedia != null) {
	        
	        // On supprime toutes les références au media
	        for (Utilisateur u : daoUtilisateur.getTous()) {	        	
	        	//  dans les playlist
				for (Playlist pl : u.getPlaylists()) {
					if (pl.getMedias().contains(selectedMedia)) {
						pl.getMedias().remove(selectedMedia);
						daoPlaylist.sauvegarder(pl);
					}				
				}
				// dans les signalements
				for (Signalement_Media sm : u.getSignalementsMedias()) {
					if (sm.getMedia() == selectedMedia) {
						u.getSignalementsMedias().remove(sm);
						daoSignalementMedia.supprimer(sm);
					}				
				}
				
				daoUtilisateur.sauvegarder(u);
			}
	        
			daoMedia.supprimer(selectedMedia);		
			chargerMedia();
		}
	}

	public List<Media> getListMedia() {
		return listMedia;
	}

	public void setListMedia(List<Media> listMedia) {
		this.listMedia = listMedia;
	}

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

	public Media getSelectedMedia() {
		return selectedMedia;
	}

	public void setSelectedMedia(Media selectedMedia) {
		this.selectedMedia = selectedMedia;
	}
	
	

	
}


