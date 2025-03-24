package prog2.astroplayer.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import prog2.astroplayer.DAO.impl.MusicDAOImpl;
import prog2.astroplayer.entities.Musica;
import prog2.astroplayer.DAO.MusicDAO;

public class MP3Controller {
    @FXML private Label currentTimeLabel;
    @FXML private Label totalTimeLabel;
    @FXML private Slider progressSlider;
    @FXML private Slider volumeSlider;
    @FXML private Button playButton;

    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private boolean isDragging = false;

    @FXML
    public void initialize() {
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
            String fileName = file.getName();
            if (fileName.lastIndexOf(".") > 0) {
                fileName = fileName.substring(0, fileName.lastIndexOf("."));
            }

            String mediaUri = file.toURI().toString();
            Media media = new Media(mediaUri);

            int durationSeconds = (int) media.getDuration().toSeconds();
            loadMedia(file.toURI().toString());

            MusicDAO musicDAO = new MusicDAOImpl();
            musicDAO.addMusica(new Musica(0, fileName, "", "", "", 0, durationSeconds, 0, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), file.getAbsolutePath()));
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
                playButton.setText("Play");
            } else {
                mediaPlayer.play();
                playButton.setText("Pause");
            }
            isPlaying = !isPlaying;
        }
    }

    private void stopMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            playButton.setText("Play");
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