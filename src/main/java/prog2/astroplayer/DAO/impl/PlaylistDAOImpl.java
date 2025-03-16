package prog2.astroplayer.DAO.impl;

import prog2.astroplayer.DAO.PlaylistDAO;
import prog2.astroplayer.DAO.PlaylistsMusicasDAO;
import prog2.astroplayer.DAO.converters.PlaylistConverter;
import prog2.astroplayer.DAO.exceptions.DatabaseException;
import prog2.astroplayer.db.DB;
import prog2.astroplayer.entities.Playlist;

import java.sql.*;
import java.util.List;

class PlaylistDAOImpl implements PlaylistDAO {
    private static final Connection connection = DB.getConnection();
    private static final PlaylistsMusicasDAO playlistsMusicasDAO = new PlaylistsMusicasImpl();

    private void createTable() throws DatabaseException {
        final String sql = """
                CREATE TABLE IF NOT EXISTS playlists (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL
                )
                """;

        try (final Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (final SQLException e) {
            throw new DatabaseException("Erro ao criar tabela das playlists", e);
        }
    }

    public PlaylistDAOImpl() {
        createTable();
    }

    @Override
    public List<Playlist> getAllPlaylists() throws DatabaseException {
        final String sql = "SELECT * FROM playlists";

        try (final Statement statement = connection.prepareStatement(sql)) {
            final ResultSet resultSet = statement.executeQuery(sql);
            final List<Playlist> playlists = PlaylistConverter.playlistsFromResultSet(resultSet);
            for (final Playlist playlist : playlists) {
                playlist.adicionarMusicas(playlistsMusicasDAO.getMusicasFromPlaylistId(playlist.getId()));
            }
            return playlists;
        } catch (final SQLException e) {
            throw new DatabaseException("Erro ao buscar playlists", e);
        }
    }

    @Override
    public Playlist getPlaylistById(int id) {
        final String sql = "SELECT * FROM playlists where id = ?";

        try (final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            final ResultSet resultSet = statement.executeQuery(sql);
            final Playlist playlist = PlaylistConverter.playlistFromResultSet(resultSet);
            playlist.adicionarMusicas(playlistsMusicasDAO.getMusicasFromPlaylistId(playlist.getId()));
            return playlist;
        } catch (final SQLException e) {
            throw new DatabaseException("Erro ao buscar playlist de id %d".formatted(id), e);
        }
    }

    @Override
    public void addPlaylist(Playlist playlist) {
        final String sql = "INSERT INTO playlists (nome) VALUES (?)";

        try (final PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, playlist.getNome());
            statement.executeUpdate();
            final ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                playlist.setId(resultSet.getInt(1));
                playlistsMusicasDAO.updateMusicasFromPlaylist(playlist);
            } else {
                throw new DatabaseException("Erro ao adicionar playlist");
            }
        } catch (final SQLException e) {
            throw new DatabaseException("Erro ao adicionar playlist", e);
        }

    }

    @Override
    public void updatePlaylist(Playlist playlist) {
        final String sql = "UPDATE playlists SET nome = ? WHERE id = ?";

        try (final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, playlist.getNome());
            statement.setInt(2, playlist.getId());
            statement.executeUpdate();
            playlistsMusicasDAO.updateMusicasFromPlaylist(playlist);
        } catch (final SQLException e) {
            throw new DatabaseException("Erro ao atualizar playlist", e);
        }

    }

    @Override
    public void deletePlaylist(int id) {
        final String sql = "DELETE FROM playlists WHERE id = ?";

        try (final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (final SQLException e) {
            throw new DatabaseException("Erro ao deletar playlist de id %d".formatted(id), e);
        }
    }
}
