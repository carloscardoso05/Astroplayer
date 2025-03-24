package prog2.astroplayer.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javafx.application.Platform;
import java.net.URL;

import java.io.File;

public class MP3Controller {
    @FXML private Label currentTimeLabel;
    @FXML private Label totalTimeLabel;
    @FXML private Slider progressSlider;
    @FXML private Slider volumeSlider;
    @FXML private Button playButton;
    @FXML private Button pauseButton;
    @FXML private Button backButton;
    @FXML private Button passButton;

    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private boolean isDragging = false;

    @FXML
    public void initialize() {
        // Aguarda a Scene ser inicializada antes de adicionar o CSS
        Platform.runLater(() -> {
            if (root.getScene() != null) {
                URL cssUrl = getClass().getResource("styles/general.css");
                if (cssUrl != null) {
                    root.getScene().getStylesheets().add(cssUrl.toExternalForm());
                } else {
                    System.out.println("Erro: Arquivo style.css não encontrado!");
                }
            } else {
                System.out.println("Erro: Scene ainda não foi definida.");
            }
        });
        // Volume control setup
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(newValue.doubleValue() / 100);
            }
        });

        // Progress slider events
        progressSlider.setOnMousePressed(e -> isDragging = true);
        progressSlider.setOnMouseReleased(e -> {
            if (mediaPlayer != null) {
                mediaPlayer.seek(Duration.seconds(progressSlider.getValue()));
                isDragging = false;
            }
        });
    }

    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));
        File file = fileChooser.showOpenDialog(playButton.getScene().getWindow());
        if (file != null) {
            loadMedia(file.toURI().toString());
        }
    }

    @FXML
    private void handlePlayPause() {
        togglePlayPause();
    }

    @FXML
    private void handleStop() {
        stopMedia();
    }

    @FXML
    private void handleBack() {
        if (mediaPlayer != null) {
            mediaPlayer.seek(mediaPlayer.getCurrentTime().subtract(Duration.seconds(10)));
        }
    }

    @FXML
    private void handlePass() {
        if (mediaPlayer != null) {
            mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(10)));
        }
    }

    private void loadMedia(String mediaUrl) {
        // Clean up previous media player
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
        }

        Media media = new Media(mediaUrl);
        mediaPlayer = new MediaPlayer(media);

        // Set up media player event handlers
        mediaPlayer.setOnReady(() -> {
            Duration totalDuration = media.getDuration();
            totalTimeLabel.setText(formatTime(totalDuration));
            progressSlider.setMax(totalDuration.toSeconds());
        });

        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            if (!isDragging) {
                progressSlider.setValue(newValue.toSeconds());
                currentTimeLabel.setText(formatTime(newValue));
            }
        });

        mediaPlayer.setOnEndOfMedia(() -> {
            stopMedia();
            progressSlider.setValue(0);
        });

        // Enable play button
        playButton.setDisable(false);
    }

    private void togglePlayPause() {
        if (mediaPlayer != null) {
            if (isPlaying) {
                mediaPlayer.pause();
                playButton.setVisible(true);
                pauseButton.setVisible(false);
            } else {
                mediaPlayer.play();
                playButton.setVisible(false);
                pauseButton.setVisible(true);
            }
            isPlaying = !isPlaying;
        }
    }

    private void stopMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            playButton.setVisible(true);
            pauseButton.setVisible(false);
            isPlaying = false;
            progressSlider.setValue(0);
            currentTimeLabel.setText("00:00");
        }
    }

    private String formatTime(Duration duration) {
        int minutes = (int) duration.toMinutes();
        int seconds = (int) duration.toSeconds() % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public void shutdown() {
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
        }
    }
}