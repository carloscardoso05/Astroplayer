package prog2.astroplayer.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.time.LocalDateTime;

@Builder
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
}
