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
    void setUp() {
        estatistica = Estatistica.getInstance();
        
        musica1 = new Musica(1, "Teste Música 1", "Artista 1", "Album 1", "Pop", 2024, 180, 0,
            LocalDateTime.now().toString(), "test1.mp3");
        
        musica2 = new Musica(2, "Teste Música 2", "Artista 2", "Album 2", "Rock", 2024, 240, 0,
            LocalDateTime.now().toString(), "test2.mp3");
    }

    @Test
    void testGetInstance() {
        Estatistica instance1 = Estatistica.getInstance();
        Estatistica instance2 = Estatistica.getInstance();
        assertSame(instance1, instance2, "getInstance should return the same instance");
    }

    @Test
    void testReproducoesTotal() {
        estatistica.setReproducoesTotal(10);
        assertEquals(10, estatistica.getReproducoesTotal(), "Reproduções total should be updated correctly");
    }

    @Test
    void testReproducoesMes() {
        estatistica.setReproducoesMes(5);
        assertEquals(5, estatistica.getReproducoesMes(), "Reproduções do mês should be updated correctly");
    }

    @Test
    void testMusicaMaisTocadaTotal() {
        estatistica.setMusicaMaisTocadaTotal(musica1);
        assertEquals(musica1, estatistica.getMusicaMaisTocadaTotal(), "Música mais tocada total should be updated correctly");
    }

    @Test
    void testMusicaMaisTocadaMes() {
        estatistica.setMusicaMaisTocadaMes(musica2);
        assertEquals(musica2, estatistica.getMusicaMaisTocadaMes(), "Música mais tocada do mês should be updated correctly");
    }
} 