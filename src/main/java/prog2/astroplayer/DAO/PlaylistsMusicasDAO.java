package prog2.astroplayer.DAO;

import prog2.astroplayer.entities.Musica;
import prog2.astroplayer.entities.Playlist;

import java.util.List;

public interface PlaylistsMusicasDAO {
    List<Musica> getMusicasFromPlaylistId(int playlistId);

    List<Playlist> getPlaylistsFromMusicaId(int musicaId);

    void updateMusicasFromPlaylist(Playlist playlist);
}
