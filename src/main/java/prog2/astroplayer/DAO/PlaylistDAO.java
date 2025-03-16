package prog2.astroplayer.DAO;

import prog2.astroplayer.entities.Playlist;

import java.util.List;

public interface PlaylistDAO {
    List<Playlist> getAllPlaylists();
    Playlist getPlaylistById(int id);
    void addPlaylist(Playlist playlist);
    void updatePlaylist(Playlist playlist);
    void deletePlaylist(int id);
}
