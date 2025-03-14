package prog2.astroplayer.DAO.impl;

import prog2.astroplayer.DAO.MusicDAO;
import prog2.astroplayer.db.DB;
import prog2.astroplayer.entities.Musica;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MusicDAOImpl implements MusicDAO {
    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS musicas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT NOT NULL, " +
                "artista TEXT, " +
                "album TEXT, " +
                "genero TEXT, " +
                "ano INTEGER, " +
                "duracao INTEGER NOT NULL, " +
                "reproducoes INTEGER NOT NULL, " +
                "adicionada_em TEXT NOT NULL, " +
                "arquivo TEXT NOT NULL)";

        try (Connection conn = DB.getConnection()) {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public MusicDAOImpl() {
        createTable();
    }

    @Override
    public List<Musica> getAllMusicas() {
        String sql = "SELECT * FROM musicas";
        List<Musica> musicas = new ArrayList<>();

        try (Connection conn = DB.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                musicas.add(new Musica(rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("artista"),
                        rs.getString("album"),
                        rs.getString("genero"),
                        rs.getInt("ano"),
                        rs.getInt("duracao"),
                        rs.getInt("reproducoes"),
                        rs.getString("adicionada_em"),
                        rs.getString("arquivo")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return musicas;
    }

    @Override
    public Musica getMusicaById(int id) {
        String sql = "SELECT * FROM musicas WHERE id = ?";
        Musica musica = null;

        try (Connection conn = DB.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                musica = new Musica(rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("artista"),
                        rs.getString("album"),
                        rs.getString("genero"),
                        rs.getInt("ano"),
                        rs.getInt("duracao"),
                        rs.getInt("reproducoes"),
                        rs.getString("adicionada_em"),
                        rs.getString("arquivo"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return musica;
    }

    @Override
    public void addMusica(Musica musica) {
        String sql = "INSERT INTO musicas (nome, artista, album, genero, ano, duracao, reproducoes, adicionada_em, arquivo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DB.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, musica.getNome());
            pstmt.setString(2, musica.getArtista());
            pstmt.setString(3, musica.getAlbum());
            pstmt.setString(4, musica.getGenero());
            pstmt.setInt(5, musica.getAno());
            pstmt.setInt(6, musica.getDuracao());
            pstmt.setInt(7, 0); // Número de reproduções
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");
            pstmt.setString(8, musica.getAdicionadaEm().format(formatter));
            pstmt.setString(9, musica.getArquivo().getAbsolutePath());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateMusica(Musica musica) {
        String sql = "UPDATE musicas SET nome = ?, " +
                "artista = ?, " +
                "album = ?, " +
                "genero = ?, " +
                "ano = ?, " +
                "duracao = ?, " +
                "reproducoes = ?, " +
                "adicionada_em = ?, " +
                "arquivo = ? WHERE id = ?";

        try (Connection conn = DB.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, musica.getNome());
            pstmt.setString(2, musica.getArtista());
            pstmt.setString(3, musica.getAlbum());
            pstmt.setString(4, musica.getGenero());
            pstmt.setInt(5, musica.getAno());
            pstmt.setInt(6, musica.getDuracao());
            pstmt.setInt(7, musica.getReproducoes());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");
            pstmt.setString(8, musica.getAdicionadaEm().format(formatter));
            pstmt.setString(9, musica.getArquivo().getAbsolutePath());
            pstmt.setInt(10, musica.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteMusica(int id) {
        String sql = "DELETE FROM musicas WHERE id = ?";

        try (Connection conn = DB.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
