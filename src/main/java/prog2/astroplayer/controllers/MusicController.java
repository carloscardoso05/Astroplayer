package prog2.astroplayer.controllers;

import prog2.astroplayer.DAO.MusicDAO;
import prog2.astroplayer.DAO.impl.MusicDAOImpl;
import prog2.astroplayer.entities.Musica;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MusicController {
    private final MusicDAO musicDAO = new MusicDAOImpl();

    public void salvarMusica(String nome, String artista, String album, String genero, int ano, int duracao, String arquivoPath) {
        if (nome == null || nome.isEmpty() || duracao < 0 || arquivoPath == null || arquivoPath.isEmpty()) {
            throw new IllegalArgumentException();
        }
        musicDAO.addMusica(new Musica(0, nome, artista, album, genero, ano, duracao, 0, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), arquivoPath));
        System.out.println("Música salva no banco de dados com sucesso");
    }

    public Musica getMusica(int id) {
        Musica musica = musicDAO.getMusicaById(id);
        System.out.println("Busca concluída com sucesso");
        if (musica == null) {
            System.out.println("Música não encontrada na base de dados");
        }
        return musica;
    }

    public List<Musica> getAllMusicas() {
        List<Musica> musicas = musicDAO.getAllMusicas();
        System.out.println("Busca concluída com sucesso");
        if (musicas == null) {
            System.out.println("Nenhuma música registrada na base de dados");
        }
        return musicas;
    }

    public void editarMusica(int id, String nome, String artista, String album, String genero, int ano) {
        Musica musica = this.getMusica(id);
        if (nome != null || !nome.isEmpty()) {
            musica.setNome(nome);
        }
        if (artista != null || !artista.isEmpty()) {
            musica.setArtista(artista);
        }
        if (album != null || !album.isEmpty()) {
            musica.setAlbum(album);
        }
        if (genero != null || !genero.isEmpty()) {
            musica.setGenero(genero);
        }
        if (ano != 0) {
            musica.setAno(ano);
        }
        musicDAO.updateMusica(musica);
        System.out.println("Dados da música modificados com sucesso");
    }
}
