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
    void configurar() {
        player = PlayerDeMusica.getInstance();
        player.limparFila();
        
        // Criar arquivos de música de teste com dados mínimos necessários
        musica1 = new Musica(1, "Teste Música 1", "Artista 1", "Album 1", "Pop", 2024, 180, 0, 
            LocalDateTime.now().toString(), "test1.mp3");
        
        musica2 = new Musica(2, "Teste Música 2", "Artista 2", "Album 2", "Rock", 2024, 240, 0,
            LocalDateTime.now().toString(), "test2.mp3");
        
        playlist = new Playlist(1, "Playlist de Teste", Arrays.asList(musica1, musica2));
    }

    @Test
    void testarObterInstancia() {
        PlayerDeMusica instancia1 = PlayerDeMusica.getInstance();
        PlayerDeMusica instancia2 = PlayerDeMusica.getInstance();
        assertSame(instancia1, instancia2, "getInstance deve retornar a mesma instância");
    }

    @Test
    void testarAdicionarNaFila() {
        player.adicionarNaFila(musica1);
        assertEquals(1, player.getTamanhoFila(), "A fila deve conter uma música após adicionar");
        assertEquals(musica1, player.getMusicaAtual(), "A música atual deve ser a primeira adicionada");
    }

    @Test
    void testarAdicionarPlaylistNaFila() {
        player.adicionarNaFila(playlist);
        assertEquals(2, player.getTamanhoFila(), "A fila deve conter duas músicas após adicionar a playlist");
        assertEquals(musica1, player.getMusicaAtual(), "A primeira música da playlist deve ser a atual");
    }

    @Test
    void testarLimparFila() {
        player.adicionarNaFila(musica1);
        player.adicionarNaFila(musica2);
        assertEquals(2, player.getTamanhoFila(), "A fila deve conter duas músicas antes de limpar");
        
        player.limparFila();
        assertEquals(0, player.getTamanhoFila(), "A fila deve estar vazia após limpar");
        assertNull(player.getMusicaAtual(), "Não deve haver música atual após limpar a fila");
    }

    @Test
    void testarRemoverDaFila() {
        player.adicionarNaFila(musica1);
        player.adicionarNaFila(musica2);
        assertEquals(2, player.getTamanhoFila(), "A fila deve conter duas músicas antes de remover");
        
        player.removerDaFila(musica1);
        assertEquals(1, player.getTamanhoFila(), "A fila deve conter uma música após remover");
        assertEquals(musica2, player.getMusicaAtual(), "A música atual deve ser a segunda música");
    }

    @Test
    void testarNavegacao() {
        player.adicionarNaFila(musica1);
        player.adicionarNaFila(musica2);
        
        assertEquals(musica1, player.getMusicaAtual(), "A primeira música deve ser a atual inicialmente");
        
        player.irParaProxima();
        assertEquals(musica2, player.getMusicaAtual(), "A segunda música deve ser a atual após avançar");
        
        player.irParaAnterior();
        assertEquals(musica1, player.getMusicaAtual(), "A primeira música deve ser a atual após voltar");
    }
} 