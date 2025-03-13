package prog2.astroplayer;

import prog2.astroplayer.entities.Playlist;

import java.util.ArrayList;
import java.util.List;

public class App {
    private static App instance;
    private final PlayerDeMusica player = PlayerDeMusica.getInstance();
    private final List<Playlist> playlists = new ArrayList<>();
    private final Estatistica estatisticas = Estatistica.getInstance();

    private App() {}

    public static App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }

    public void adicionarPlaylist(Playlist playlist) {
        playlists.add(playlist);
    }

    public void removerPlaylist(Playlist playlist) {
        playlists.remove(playlist);
    }

    public void limparPlaylists() {
        playlists.clear();
    }
}
