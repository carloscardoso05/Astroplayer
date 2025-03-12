package prog2.astroplayer;

import prog2.astroplayer.entities.Musica;
import prog2.astroplayer.entities.Playlist;
import prog2.util.ListaNavegavel;

public class PlayerDeMusica {
    private static PlayerDeMusica instance;
    private ListaNavegavel<Musica> fila;

    private PlayerDeMusica() {}

    public static PlayerDeMusica getInstance() {
        if (instance == null) {
            instance = new PlayerDeMusica();
        }
        return instance;
    }

    void irParaProxima() {

    }

    void irParaAnterior() {

    }

    void tocar() {

    }

    void tocar(int segundoInicial) {

    }

    void pausar() {

    }

    void adicionarNaFila(Musica musica) {

    }

    void adicionarNaFila(Playlist playlists) {

    }

    void limparFila() {

    }

    void removerDaFila(Musica musica) {

    }
}
