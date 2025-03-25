package prog2.astroplayer.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import prog2.astroplayer.DAO.MusicDAO;
import prog2.astroplayer.DAO.impl.MusicDAOImpl;
import prog2.astroplayer.entities.Musica;

import java.io.File;

public class MusicManagementController {
    @FXML private TableView<Musica> musicTable;
    @FXML private TableColumn<Musica, String> titleColumn;
    @FXML private TableColumn<Musica, String> artistColumn;
    @FXML private Button addMusicButton;
    @FXML private Button removeMusicButton;

    private final ObservableList<Musica> todasMusicas = FXCollections.observableArrayList();
    private final MusicDAO musicDAO = new MusicDAOImpl();

    @FXML
    public void initialize() {
        todasMusicas.addAll(musicDAO.getAllMusicas());
        setupMusicTable();
        setupButtons();
    }

    private void setupMusicTable() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artista"));
        musicTable.setItems(todasMusicas);
    }

    private void setupButtons() {
        addMusicButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(musicTable.getScene().getWindow());
            if (file != null) {
                try {
                    if (todasMusicas.stream().anyMatch(m -> m.getArquivo().equals(file))) {
                        showErrorDialog("Música duplicada", "Essa música já foi adicionada a biblioteca", "");
                        return;
                    }

                    Musica music = new Musica(file);
                    musicDAO.addMusica(music);
                    todasMusicas.add(music);
                    
                    // Refresh the player screen
                    MusicPlayerController playerController = MusicPlayerController.getInstance();
                    if (playerController != null) {
                        playerController.refreshMusicTable();
                    }
                } catch (Exception ex) {
                    showErrorDialog("Erro", "Não foi possível adicionar a música", ex.getMessage());
                }
            }
        });

        removeMusicButton.setOnAction(e -> {
            Musica selecionada = musicTable.getSelectionModel().getSelectedItem();
            if (selecionada != null) {
                todasMusicas.remove(selecionada);
                musicDAO.deleteMusica(selecionada.getId());
                
                // Refresh the player screen
                MusicPlayerController playerController = MusicPlayerController.getInstance();
                if (playerController != null) {
                    playerController.refreshMusicTable();
                }
            }
        });
    }

    private void showErrorDialog(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 