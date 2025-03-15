package prog2.astroplayer.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.time.LocalDateTime;

@Getter
@Setter
public class Musica {
    private int id;
    private String nome;
    private String artista;
    private String album;
    private String genero;
    private int ano;
    private int duracao;
    private int reproducoes;
    private LocalDateTime adicionadaEm;
    private File arquivo;

    public Musica(int id, String nome, String artista, String album, String genero, int ano, int duracao, int reproducoes, String dateTimeStr, String arquivoPath){
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
}
