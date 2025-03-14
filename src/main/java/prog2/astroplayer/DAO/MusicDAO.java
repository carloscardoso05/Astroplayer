package prog2.astroplayer.DAO;

import prog2.astroplayer.entities.Musica;

import java.util.List;

public interface MusicDAO {
    List<Musica> getAllMusicas();
    Musica getMusicaById(int id);
    void addMusica(Musica musica);
    void updateMusica(Musica musica);
    void deleteMusica(int id);
}
