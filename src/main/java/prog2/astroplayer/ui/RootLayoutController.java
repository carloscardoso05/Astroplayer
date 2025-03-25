package prog2.astroplayer.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

public class RootLayoutController {
    @FXML private BorderPane rootLayout;

    @FXML
    public void initialize() {
        showMusicaPlayerScreen();
    }

    public void showMusicaPlayerScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(RootLayoutController.class.getResource("/fxml/music_player.fxml"));
            rootLayout.setCenter(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showPlaylistScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(RootLayoutController.class.getResource("/fxml/playlist_management.fxml"));
            rootLayout.setCenter(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showMusicaManagementScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(RootLayoutController.class.getResource("/fxml/music_management.fxml"));
            rootLayout.setCenter(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 