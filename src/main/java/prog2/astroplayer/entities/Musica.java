package prog2.astroplayer.entities;

import com.mpatric.mp3agic.Mp3File;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
public class Musica {
    private int id;
    private String nome;
    private String artista;
    private String album;
    private String genero;
    private Integer ano;
    private int duracao;
    private int reproducoes;
    private LocalDateTime adicionadaEm;
    private File arquivo;

    public Musica(int id, String nome, String artista, String album, String genero, int ano, int duracao, int reproducoes, String dateTimeStr, String arquivoPath) {
        this.setId(id);
        this.setNome(nome);
        this.setArtista(artista);
        this.setAlbum(album);
        this.setGenero(genero);
        this.setAno(ano);
        this.setDuracao(duracao);
        this.setReproducoes(reproducoes);
        this.setAdicionadaEm(LocalDateTime.parse(dateTimeStr));
        this.setArquivo(new File(arquivoPath));
    }

    private String getOrDefault(String obj, String fileName) {
        return Objects.requireNonNullElse(obj, fileName);
    }

    private Integer tryParseInt(String obj) {
        try {
            return Integer.parseInt(obj);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public Musica(File arquivo) {
        try {
            final Mp3File mp3File = new Mp3File(arquivo);
            this.setNome(getOrDefault(mp3File.getId3v2Tag().getTitle(), arquivo.getName()));
            this.setArtista(getOrDefault(mp3File.getId3v2Tag().getArtist(), "Desconhecido"));
            this.setAlbum(getOrDefault(mp3File.getId3v2Tag().getAlbum(), "Desconhecido"));
            this.setGenero(getOrDefault(mp3File.getId3v2Tag().getGenreDescription(), "Desconhecido"));
            this.setAno(tryParseInt(mp3File.getId3v2Tag().getYear()));
            this.setDuracao((int) mp3File.getLengthInSeconds());
            this.setReproducoes(0);
            this.setAdicionadaEm(LocalDateTime.now());
            this.setArquivo(arquivo);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler arquivo MP3", e);
        }
    }

    @Override
    public String toString() {
        return String.format("%s - %s", getNome(), getArtista());
    }
}
