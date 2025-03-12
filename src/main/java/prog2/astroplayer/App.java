package prog2.astroplayer;

import prog2.astroplayer.entities.Playlist;

import java.util.List;

public class App {
    private static App instance;
    private PlayerDeMusica player;
    private List<Playlist> playlists;
    private Estatistica estatisticas;

    private App() {}

    public static App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }

    public void adicionarPlaylist(Playlist playlist) {

    }

    public void removerPlaylist(Playlist playlist) {

    }

    public void limparPlaylists() {

    }
}
