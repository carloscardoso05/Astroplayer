package prog2.astroplayer.controllers;

import prog2.astroplayer.DAO.MusicDAO;
import prog2.astroplayer.DAO.impl.MusicDAOImpl;
import prog2.astroplayer.entities.Musica;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MusicController {
    public static void salvarMusica(String nome, String artista, String album, String genero, int ano, int duracao, String arquivoPath) {
        Musica musica = new Musica(0, nome, artista, album, genero, ano, duracao, 0, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), arquivoPath);
        MusicDAO musicDAO = new MusicDAOImpl();

        musicDAO.addMusica(musica);
        musicDAO.getAllMusicas();
    }
}
