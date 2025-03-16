package prog2.astroplayer.DAO.converters;

import prog2.astroplayer.DAO.exceptions.DatabaseException;
import prog2.astroplayer.entities.Musica;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class MusicaConverter {
    public static Musica musicaFromResultSet(final ResultSet resultSet) throws DatabaseException {
        try {
            return new Musica(
                    resultSet.getInt("id"),
                    resultSet.getString("nome"),
                    resultSet.getString("artista"),
                    resultSet.getString("album"),
                    resultSet.getString("genero"),
                    resultSet.getInt("ano"),
                    resultSet.getInt("duracao"),
                    resultSet.getInt("reproducoes"),
                    resultSet.getString("adicionada_em"),
                    resultSet.getString("arquivo")
            );
        } catch (Exception e) {
            throw new DatabaseException("Erro ao converter ResultSet para Musica", e);
        }
    }

    public static List<Musica> musicasFromResultSet(final ResultSet resultSet) throws DatabaseException {
        final List<Musica> musicas = new ArrayList<>();
        try {
            while (resultSet.next())
                musicas.add(musicaFromResultSet(resultSet));
            return musicas;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao converter ResultSet para Musicas", e);
        }
    }
}
