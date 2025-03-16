package prog2.astroplayer.DAO.converters;

import prog2.astroplayer.DAO.exceptions.DatabaseException;
import prog2.astroplayer.entities.Playlist;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class PlaylistConverter {

    public static Playlist playlistFromResultSet(final ResultSet resultSet) throws DatabaseException {
        try {
            return new Playlist(
                    resultSet.getInt("id"),
                    resultSet.getString("nome"),
                    new ArrayList<>()
            );
        } catch (final SQLException e) {
            throw new DatabaseException("Erro ao converter ResultSet para Playlist", e);
        }
    }

    /**
     * Converte um ResultSet em uma lista de Playlists
     * Obs: O as musicas da Playlist não são adicionadas
     * @param resultSet ResultSet a ser convertido
     * @return Lista de Playlists
     * @throws DatabaseException Caso ocorra um erro ao converter o ResultSet
     */
    public static List<Playlist> playlistsFromResultSet(final ResultSet resultSet) throws DatabaseException {
        final List<Playlist> playlists = new ArrayList<>();
        try {
            while (resultSet.next())
                playlists.add(playlistFromResultSet(resultSet));
            return playlists;
        } catch (final SQLException e) {
            throw new DatabaseException("Erro ao converter ResultSet para Playlists", e);
        }
    }
}
