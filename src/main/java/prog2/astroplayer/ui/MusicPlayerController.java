package prog2.astroplayer.ui;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import prog2.astroplayer.DAO.MusicDAO;
import prog2.astroplayer.DAO.impl.MusicDAOImpl;
import prog2.astroplayer.entities.Musica;

import java.util.ArrayList;

public class MusicPlayerController {
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

    @FXML
    public void initialize() {
        todasMusicas.addAll(musicDAO.getAllMusicas());
        setupMusicTable();
        setupQueueList();
        setupControls();
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
                currentQueue.remove(musica);
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
            if (mediaPlayer != null) {
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
                mediaPlayer.setVolume(newVal.doubleValue());
            }
        });

        progressSlider.setOnMousePressed(e -> {
            if (mediaPlayer != null) {
                mediaPlayer.seek(Duration.seconds(
                        progressSlider.getValue() / 100 * mediaPlayer.getTotalDuration().toSeconds()));
            }
        });
    }

    private String formatTime(Duration duration) {
        int minutes = (int) duration.toMinutes();
        int seconds = (int) duration.toSeconds() % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void tocarMusica(int index) {
        if (index < 0 || index >= currentQueue.size())
            return;
        currentTrackIndex = index;
        Musica music = currentQueue.get(index);
        Media media = new Media(music.getArquivo().toURI().toString());

        if (mediaPlayer != null)
            mediaPlayer.dispose();
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
        playPauseButton.setSelected(true);

        mediaPlayer.setOnEndOfMedia(() -> tocarMusica(currentTrackIndex + 1));
    }
}