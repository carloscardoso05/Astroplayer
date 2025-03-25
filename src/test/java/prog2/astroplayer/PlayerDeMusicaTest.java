package prog2.astroplayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import prog2.astroplayer.entities.Musica;
import prog2.astroplayer.entities.Playlist;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PlayerDeMusicaTest {
    private PlayerDeMusica player;
    private Musica musica1;
    private Musica musica2;
    private Playlist playlist;

    @BeforeEach
    void setUp() {
        player = PlayerDeMusica.getInstance();
        player.limparFila();
        
        // Create test music files with minimum required data
        musica1 = new Musica(1, "Teste Música 1", "Artista 1", "Album 1", "Pop", 2024, 180, 0, 
            LocalDateTime.now().toString(), "test1.mp3");
        
        musica2 = new Musica(2, "Teste Música 2", "Artista 2", "Album 2", "Rock", 2024, 240, 0,
            LocalDateTime.now().toString(), "test2.mp3");
        
        playlist = new Playlist(1, "Playlist de Teste", Arrays.asList(musica1, musica2));
    }

    @Test
    void testGetInstance() {
        PlayerDeMusica instance1 = PlayerDeMusica.getInstance();
        PlayerDeMusica instance2 = PlayerDeMusica.getInstance();
        assertSame(instance1, instance2, "getInstance should return the same instance");
    }

    @Test
    void testAdicionarNaFila() {
        player.adicionarNaFila(musica1);
        // TODO: Add verification once getters are implemented
        // For now, we can only test that no exception is thrown
    }

    @Test
    void testAdicionarPlaylistNaFila() {
        player.adicionarNaFila(playlist);
        // TODO: Add verification once getters are implemented
        // For now, we can only test that no exception is thrown
    }

    @Test
    void testLimparFila() {
        player.adicionarNaFila(musica1);
        player.adicionarNaFila(musica2);
        player.limparFila();
        // TODO: Add verification once getters are implemented
        // For now, we can only test that no exception is thrown
    }

    @Test
    void testRemoverDaFila() {
        player.adicionarNaFila(musica1);
        player.removerDaFila(musica1);
        // TODO: Add verification once getters are implemented
        // For now, we can only test that no exception is thrown
    }

    @Test
    void testNavegacao() {
        player.adicionarNaFila(musica1);
        player.adicionarNaFila(musica2);
        
        player.irParaProxima();
        player.irParaAnterior();
        // TODO: Add verification once getters are implemented
        // For now, we can only test that no exception is thrown
    }
} 