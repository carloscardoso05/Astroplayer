package prog2.astroplayer.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import prog2.util.ListaNavegavel;

import java.util.List;

@Getter
@Setter
public class Playlist {
    private int id;
    private String nome;
    private final ListaNavegavel<Musica> musicas = new ListaNavegavel<>();

    public Playlist(int id, String nome, List<Musica> musicas) {
        this.id = id;
        this.nome = nome;
        this.musicas.addAll(musicas);
    }

    public int getDuracaoTotal() {
        return musicas.stream().map(Musica::getDuracao).reduce(0, Integer::sum);
    }

    public void adicionarMusica(Musica musica) {
        musicas.add(musica);
    }

    public void adicionarMusicas(List<Musica> musicas) {
        this.musicas.addAll(musicas);
    }

    public void removerMusica(Musica musica) {
        musicas.remove(musica);
    }

    public void limpar() {
        musicas.clear();
    }
}
