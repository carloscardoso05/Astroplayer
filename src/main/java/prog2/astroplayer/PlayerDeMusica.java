package prog2.astroplayer;

import prog2.astroplayer.entities.Musica;
import prog2.astroplayer.entities.Playlist;
import prog2.util.ListaNavegavel;

public class PlayerDeMusica {
    private static PlayerDeMusica instance;
    private final ListaNavegavel<Musica> fila = new ListaNavegavel<>();
    private boolean isPaused = true;

    private PlayerDeMusica() {
    }

    public static PlayerDeMusica getInstance() {
        if (instance == null) {
            instance = new PlayerDeMusica();
        }
        return instance;
    }

    public Musica getMusicaAtual() {
        if (fila.isEmpty()) {
            return null;
        }
        return fila.getAtual();
    }

    public boolean temProxima() {
        return fila.temProximo();
    }

    public boolean temAnterior() {
        return fila.temAnterior();
    }

    public Musica irParaProxima() {
        if (!temProxima()) {
            return null;
        }
        return fila.irParaProximo();
    }

    public Musica irParaAnterior() {
        if (!temAnterior()) {
            return null;
        }
        return fila.irParaAnterior();
    }

    public void tocar() {
        isPaused = false;
    }

    public void tocar(int segundoInicial) {
        isPaused = false;
    }

    public void pausar() {
        isPaused = true;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void adicionarNaFila(Musica musica) {
        fila.add(musica);
    }

    public void adicionarNaFila(Playlist playlists) {
        fila.addAll(playlists.getMusicas());
    }

    public void limparFila() {
        fila.clear();
    }

    public void removerDaFila(Musica musica) {
        fila.remove(musica);
    }

    public int getTamanhoFila() {
        return fila.size();
    }

    public boolean filaVazia() {
        return fila.isEmpty();
    }
}
