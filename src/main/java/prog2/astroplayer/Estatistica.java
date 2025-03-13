package prog2.astroplayer;

import lombok.Getter;
import lombok.Setter;
import prog2.astroplayer.entities.Musica;

@Getter
@Setter
public class Estatistica {
    private static Estatistica instance;
    private int reproducoesTotal;
    private int reproducoesMes;
    private Musica musicaMaisTocadaTotal;
    private Musica musicaMaisTocadaMes;

    private Estatistica() {}

    public static Estatistica getInstance() {
        if (instance == null) {
            instance = new Estatistica();
        }
        return instance;
    }
}
