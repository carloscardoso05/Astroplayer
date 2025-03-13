package prog2.astroplayer;

import prog2.astroplayer.entities.Musica;
import prog2.astroplayer.entities.Playlist;
import prog2.util.ListaNavegavel;

public class PlayerDeMusica {
    private static PlayerDeMusica instance;
    private final ListaNavegavel<Musica> fila = new ListaNavegavel<>();

    private PlayerDeMusica() {
    }

    public static PlayerDeMusica getInstance() {
        if (instance == null) {
            instance = new PlayerDeMusica();
        }
        return instance;
    }

    void irParaProxima() {
        fila.irParaProximo();
    }

    void irParaAnterior() {
        fila.irParaAnterior();
    }

    void tocar() {
        // TODO: implementar
        throw new UnsupportedOperationException("Not supported yet.");
    }

    void tocar(int segundoInicial) {
        //TODO: implementar
        throw new UnsupportedOperationException("Not supported yet.");
    }

    void pausar() {
        // TODO: implementar
        throw new UnsupportedOperationException("Not supported yet.");
    }

    void adicionarNaFila(Musica musica) {
        fila.add(musica);
    }

    void adicionarNaFila(Playlist playlists) {
        fila.addAll(playlists.getMusicas());
    }

    void limparFila() {
        fila.clear();
    }

    void removerDaFila(Musica musica) {
        fila.remove(musica);
    }
}
