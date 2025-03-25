package prog2.astroplayer.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

public class RootLayoutController {
    @FXML private BorderPane rootLayout;
    
    private MusicPlayerController musicPlayerController;
    private PlaylistManagementController playlistController;

    @FXML
    public void initialize() {
        showMusicaPlayerScreen();
    }

    public void showMusicaPlayerScreen() {
        try {
            // Get the existing controller instance if it exists
            MusicPlayerController existingController = MusicPlayerController.getInstance();
            
            // Load the FXML
            FXMLLoader loader = new FXMLLoader(RootLayoutController.class.getResource("/fxml/music_player.fxml"));
            rootLayout.setCenter(loader.load());
            
            // Get the new controller instance
            musicPlayerController = loader.getController();
            
            // If we had an existing controller, sync its state
            if (existingController != null) {
                // Wait for the new controller to be fully initialized
                javafx.application.Platform.runLater(() -> {
                    musicPlayerController.syncUIState();
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showPlaylistScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(RootLayoutController.class.getResource("/fxml/playlist_management.fxml"));
            rootLayout.setCenter(loader.load());
            playlistController = loader.getController();
            if (musicPlayerController != null) {
                playlistController.setMusicPlayerController(musicPlayerController);
            }
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