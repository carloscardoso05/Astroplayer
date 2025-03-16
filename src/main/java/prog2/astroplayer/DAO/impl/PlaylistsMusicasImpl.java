package prog2.astroplayer.DAO.impl;

import prog2.astroplayer.DAO.PlaylistsMusicasDAO;
import prog2.astroplayer.DAO.converters.MusicaConverter;
import prog2.astroplayer.DAO.converters.PlaylistConverter;
import prog2.astroplayer.DAO.exceptions.DatabaseException;
import prog2.astroplayer.db.DB;
import prog2.astroplayer.entities.Musica;
import prog2.astroplayer.entities.Playlist;

import java.sql.*;
import java.util.List;

public class PlaylistsMusicasImpl implements PlaylistsMusicasDAO {
    private static final Connection connection = DB.getConnection();

    private void createTable() throws DatabaseException {
        final String sql = """
                CREATE TABLE IF NOT EXISTS playlist_musicas (
                    playlist_id INTEGER NOT NULL,
                    musica_id INTEGER NOT NULL,
                    PRIMARY KEY (playlist_id, musica_id),
                    FOREIGN KEY (playlist_id) REFERENCES playlists(id),
                    FOREIGN KEY (musica_id) REFERENCES musicas(id)
                )
                """;
        try (final Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (final SQLException e) {
            throw new DatabaseException("Erro ao criar tabela playlist_musicas", e);
        }
    }

    public PlaylistsMusicasImpl() {
        createTable();
    }

    @Override
    public List<Musica> getMusicasFromPlaylistId(int playlistId) {
        final String sql = "SELECT * FROM musicas WHERE id IN (SELECT musica_id FROM playlists_musicas WHERE playlist_id = ?)";
        try (final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, playlistId);
            final ResultSet resultSet = statement.executeQuery(sql);
            return MusicaConverter.musicasFromResultSet(resultSet);
        } catch (final SQLException e) {
            throw new DatabaseException("Erro ao buscar musicas da playlist", e);
        }
    }

    @Override
    public List<Playlist> getPlaylistsFromMusicaId(int musicaId) {
        final String sql = "SELECT * FROM playlists WHERE id IN (SELECT playlist_id FROM playlists_musicas WHERE musica_id = ?)";
        try (final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, musicaId);
            final ResultSet resultSet = statement.executeQuery(sql);
            final List<Playlist> playlists = PlaylistConverter.playlistsFromResultSet(resultSet);
            for (final Playlist playlist : playlists) {
                playlist.adicionarMusicas(getMusicasFromPlaylistId(playlist.getId()));
            }
            return playlists;
        } catch (final SQLException e) {
            throw new DatabaseException("Erro ao buscar playlists da musica", e);
        }
    }

    @Override
    public void updateMusicasFromPlaylist(Playlist playlist) {
        final String sqlDelete = "DELETE FROM playlists_musicas WHERE playlist_id = ?";
        try (final PreparedStatement statement = connection.prepareStatement(sqlDelete)) {
            statement.setInt(1, playlist.getId());
            statement.executeUpdate();
        } catch (final SQLException e) {
            throw new DatabaseException("Erro ao atualizar musicas da playlist", e);
        }

        final String sqlInsert = "INSERT INTO playlists_musicas (playlist_id, musica_id) VALUES (?, ?)";
        try (final PreparedStatement statement = connection.prepareStatement(sqlInsert)) {
            for (final Musica musica : playlist.getMusicas()) {
                statement.setInt(1, playlist.getId());
                statement.setInt(2, musica.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (final SQLException e) {
            throw new DatabaseException("Erro ao atualizar musicas da playlist", e);
        }
    }
}
