package prog2.astroplayer.entities;

import java.io.File;
import java.time.LocalDateTime;

public class Musica {
    private int id;
    private String nome;
    private String artista;
    private String album;
    private String genero;
    private int ano;
    private int duracao;
    private int reproducoes;
    LocalDateTime adicionadaEm;
    File arquivo;
}
