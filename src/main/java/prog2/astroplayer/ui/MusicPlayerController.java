package prog2.astroplayer.ui;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import prog2.astroplayer.DAO.MusicDAO;
import prog2.astroplayer.DAO.impl.MusicDAOImpl;
import prog2.astroplayer.entities.Musica;
import prog2.astroplayer.PlayerDeMusica;

import java.util.ArrayList;

public class MusicPlayerController {
    private static MusicPlayerController instance;
    
    @FXML
    private TableView<Musica> musicTable;
    @FXML
    private TableColumn<Musica, String> titleColumn;
    @FXML
    private TableColumn<Musica, String> artistColumn;
    @FXML
    private ListView<Musica> queueList;
    @FXML
    private ToggleButton playPauseButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button previousButton;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Slider progressSlider;
    @FXML
    private Label currentTime;

    private MediaPlayer mediaPlayer;
    private final ObservableList<Musica> todasMusicas = FXCollections.observableArrayList();
    private final ObservableList<Musica> currentQueue = FXCollections.observableArrayList();
    private int currentTrackIndex = -1;
    private final MusicDAO musicDAO = new MusicDAOImpl();
    private double lastVolume = 0.5; // Store the last volume setting
    private boolean isInitialized = false;
    private javafx.beans.value.ChangeListener<Duration> timeListener;

    public static MusicPlayerController getInstance() {
        return instance;
    }

    @FXML
    public void initialize() {
        if (instance == null) {
            instance = this;
            isInitialized = true;
            todasMusicas.addAll(musicDAO.getAllMusicas());
            setupMusicTable();
            setupQueueList();
            setupControls();
            updateNavigationButtons();
        } else {
            // If we already have an instance, copy the state
            this.mediaPlayer = instance.mediaPlayer;
            this.currentTrackIndex = instance.currentTrackIndex;
            this.lastVolume = instance.lastVolume;
            this.currentQueue.setAll(instance.currentQueue);
            this.todasMusicas.setAll(instance.todasMusicas);
            
            // Set up UI
            setupMusicTable();
            setupQueueList();
            setupControls();
            updateNavigationButtons();
            
            // Sync UI state
            syncUIState();
        }
    }

    public void syncUIState() {
        if (mediaPlayer != null) {
            // Update play/pause button state
            playPauseButton.setSelected(mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING);
            
            // Update time and progress
            if (mediaPlayer.getCurrentTime() != null) {
                currentTime.setText(formatTime(mediaPlayer.getCurrentTime()));
            }
            if (mediaPlayer.getTotalDuration() != null) {
                double currentTime = mediaPlayer.getCurrentTime().toSeconds();
                double totalDuration = mediaPlayer.getTotalDuration().toSeconds();
                progressSlider.setValue((currentTime / totalDuration) * 100.0);
            }
            
            // Update volume without triggering the listener
            volumeSlider.setValue(mediaPlayer.getVolume());
            
            // Reattach time listener if needed
            if (timeListener == null) {
                setupTimeListener();
            }
        } else {
            // Reset UI if no media is playing
            playPauseButton.setSelected(false);
            currentTime.setText("00:00");
            progressSlider.setValue(0);
            volumeSlider.setValue(lastVolume);
        }
        updateNavigationButtons();
    }

    private void setupTimeListener() {
        if (timeListener != null && mediaPlayer != null) {
            mediaPlayer.currentTimeProperty().removeListener(timeListener);
        }
        
        timeListener = (obs, oldTime, newTime) -> {
            if (!progressSlider.isPressed() && mediaPlayer != null && mediaPlayer.getTotalDuration() != null) {
                double currentTime = newTime.toSeconds();
                double totalDuration = mediaPlayer.getTotalDuration().toSeconds();
                progressSlider.setValue((currentTime / totalDuration) * 100.0);
                this.currentTime.setText(formatTime(newTime));
            }
        };
        
        if (mediaPlayer != null) {
            mediaPlayer.currentTimeProperty().addListener(timeListener);
        }
    }

    private void setupMusicTable() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artista"));
        musicTable.setItems(todasMusicas);

        musicTable.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                Musica selected = musicTable.getSelectionModel().getSelectedItem();
                currentQueue.add(selected);
                if (currentTrackIndex == -1) {
                    tocarMusica(0);
                }
            }
        });
    }

    private void setupQueueList() {
        queueList.setItems(currentQueue);
        queueList.setCellFactory(lv -> {
            ListCell<Musica> cell = new ListCell<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem removeItem = new MenuItem("Remove from queue");

            removeItem.setOnAction(e -> {
                Musica musica = cell.getItem();
                int index = currentQueue.indexOf(musica);
                currentQueue.remove(musica);
                
                // Handle removal of currently playing song
                if (index == currentTrackIndex) {
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.dispose();
                        mediaPlayer = null;
                    }
                    if (!currentQueue.isEmpty()) {
                        // If there are more songs, play the next one
                        tocarMusica(Math.min(index, currentQueue.size() - 1));
                    } else {
                        // If queue is empty, reset everything
                        currentTrackIndex = -1;
                        playPauseButton.setSelected(false);
                        currentTime.setText("00:00");
                        progressSlider.setValue(0);
                    }
                } else if (index < currentTrackIndex) {
                    // If we removed a song before the current one, adjust the index
                    currentTrackIndex--;
                }
                updateNavigationButtons();
            });

            contextMenu.getItems().add(removeItem);
            cell.contextMenuProperty().bind(
                    Bindings.when(cell.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu));
            return cell;
        });
    }

    private void setupControls() {
        playPauseButton.setOnAction(e -> {
            if (mediaPlayer != null && mediaPlayer.getStatus() != null) {
                if (playPauseButton.isSelected()) {
                    mediaPlayer.play();
                } else {
                    mediaPlayer.pause();
                }
            }
        });

        nextButton.setOnAction(e -> {
            if (currentTrackIndex < currentQueue.size() - 1) {
                tocarMusica(currentTrackIndex + 1);
            }
        });

        previousButton.setOnAction(e -> {
            if (currentTrackIndex > 0) {
                tocarMusica(currentTrackIndex - 1);
            }
        });

        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (mediaPlayer != null) {
                double volume = newVal.doubleValue(); // Convert percentage to 0-1 range
                mediaPlayer.setVolume(volume);
                lastVolume = volume;
            }
        });

        progressSlider.setOnMousePressed(e -> {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
        });

        progressSlider.setOnMouseDragged(e -> {
            if (mediaPlayer != null && mediaPlayer.getTotalDuration() != null) {
                double percentage = progressSlider.getValue();
                double duration = mediaPlayer.getTotalDuration().toSeconds();
                mediaPlayer.seek(Duration.seconds(percentage * duration / 100.0));
            }
        });

        progressSlider.setOnMouseReleased(e -> {
            if (mediaPlayer != null && mediaPlayer.getTotalDuration() != null) {
                double percentage = progressSlider.getValue();
                double duration = mediaPlayer.getTotalDuration().toSeconds();
                mediaPlayer.seek(Duration.seconds(percentage * duration / 100.0));
                if (playPauseButton.isSelected()) {
                    mediaPlayer.play();
                }
            }
        });

        // Set initial volume
        volumeSlider.setValue(lastVolume);
    }

    private void updateNavigationButtons() {
        nextButton.setDisable(currentTrackIndex >= currentQueue.size() - 1 || currentQueue.isEmpty());
        previousButton.setDisable(currentTrackIndex <= 0 || currentQueue.isEmpty());
    }

    private String formatTime(Duration duration) {
        int minutes = (int) duration.toMinutes();
        int seconds = (int) duration.toSeconds() % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void tocarMusica(int index) {
        if (index < 0 || index >= currentQueue.size()) {
            return;
        }
            
        currentTrackIndex = index;
        Musica music = currentQueue.get(index);
        
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
            }
            
            String uri = music.getArquivo().toURI().toString();
            Media media = new Media(uri);
            
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setVolume(lastVolume);
            
            // Set up media player event handlers
            mediaPlayer.setOnReady(() -> {
                Duration total = mediaPlayer.getTotalDuration();
                progressSlider.setValue(0);
                currentTime.setText("00:00");
                mediaPlayer.play();
                playPauseButton.setSelected(true);
                setupTimeListener();
            });
            
            mediaPlayer.setOnEndOfMedia(() -> {
                if (currentTrackIndex < currentQueue.size() - 1) {
                    tocarMusica(currentTrackIndex + 1);
                } else {
                    mediaPlayer.stop();
                    mediaPlayer.dispose();
                    mediaPlayer = null;
                    playPauseButton.setSelected(false);
                    currentTime.setText("00:00");
                    progressSlider.setValue(0);
                }
            });
            
            mediaPlayer.setOnError(() -> {
                MediaException error = mediaPlayer.getError();
                System.err.println("Media Player Error: " + error.getMessage());
                // Skip to next track if available
                if (currentTrackIndex < currentQueue.size() - 1) {
                    tocarMusica(currentTrackIndex + 1);
                }
            });
            
            updateNavigationButtons();
            
        } catch (Exception e) {
            System.err.println("Error playing track: " + e.getMessage());
            e.printStackTrace();
            // Skip to next track if available
            if (currentTrackIndex < currentQueue.size() - 1) {
                tocarMusica(currentTrackIndex + 1);
            }
        }
    }

    public void addToQueue(java.util.List<Musica> musicas) {
        currentQueue.addAll(musicas);
        if (currentTrackIndex == -1 && !currentQueue.isEmpty()) {
            tocarMusica(0);
        }
        updateNavigationButtons();
    }
}