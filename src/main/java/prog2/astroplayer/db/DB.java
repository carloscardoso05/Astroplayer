package prog2.astroplayer.db;

import prog2.astroplayer.DAO.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    private static Connection connection;
    private static final String url = "jdbc:sqlite:astroplayer.db";

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                try {
                    connection = DriverManager.getConnection(url);
                } catch (SQLException e) {
                    throw new RuntimeException("Erro ao conectar ao banco de dados", e);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao conectar ao banco de dados", e);
        }
        return connection;
    }
}
