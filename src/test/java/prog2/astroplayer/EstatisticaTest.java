package prog2.astroplayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import prog2.astroplayer.entities.Musica;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EstatisticaTest {
    private Estatistica estatistica;
    private Musica musica1;
    private Musica musica2;

    @BeforeEach
    void configurar() {
        estatistica = Estatistica.getInstance();
        
        musica1 = new Musica(1, "Teste Música 1", "Artista 1", "Album 1", "Pop", 2024, 180, 0,
            LocalDateTime.now().toString(), "test1.mp3");
        
        musica2 = new Musica(2, "Teste Música 2", "Artista 2", "Album 2", "Rock", 2024, 240, 0,
            LocalDateTime.now().toString(), "test2.mp3");
    }

    @Test
    void testarObterInstancia() {
        Estatistica instancia1 = Estatistica.getInstance();
        Estatistica instancia2 = Estatistica.getInstance();
        assertSame(instancia1, instancia2, "getInstance deve retornar a mesma instância");
    }

    @Test
    void testarReproducoesTotal() {
        estatistica.setReproducoesTotal(10);
        assertEquals(10, estatistica.getReproducoesTotal(), "Total de reproduções deve ser atualizado corretamente");
    }

    @Test
    void testarReproducoesMes() {
        estatistica.setReproducoesMes(5);
        assertEquals(5, estatistica.getReproducoesMes(), "Reproduções do mês devem ser atualizadas corretamente");
    }

    @Test
    void testarMusicaMaisTocadaTotal() {
        estatistica.setMusicaMaisTocadaTotal(musica1);
        assertEquals(musica1, estatistica.getMusicaMaisTocadaTotal(), "Música mais tocada no total deve ser atualizada corretamente");
    }

    @Test
    void testarMusicaMaisTocadaMes() {
        estatistica.setMusicaMaisTocadaMes(musica2);
        assertEquals(musica2, estatistica.getMusicaMaisTocadaMes(), "Música mais tocada do mês deve ser atualizada corretamente");
    }
} 