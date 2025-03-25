package prog2.astroplayer.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import prog2.astroplayer.DAO.MusicDAO;
import prog2.astroplayer.DAO.PlaylistDAO;
import prog2.astroplayer.DAO.impl.MusicDAOImpl;
import prog2.astroplayer.DAO.impl.PlaylistDAOImpl;
import prog2.astroplayer.entities.Musica;
import prog2.astroplayer.entities.Playlist;

import java.util.ArrayList;

public class PlaylistManagementController {
    @FXML private ListView<Playlist> playlistList;
    @FXML private Button addPlaylistButton;
    @FXML private Button removePlaylistButton;

    private final ObservableList<Playlist> playlists = FXCollections.observableArrayList();
    private final PlaylistDAO playlistDAO = new PlaylistDAOImpl();
    private final MusicDAO musicDAO = new MusicDAOImpl();
    private MusicPlayerController musicPlayerController;

    public void setMusicPlayerController(MusicPlayerController controller) {
        this.musicPlayerController = controller;
    }

    @FXML
    public void initialize() {
        playlists.addAll(playlistDAO.getAllPlaylists());
        setupPlaylistList();
        setupButtons();
    }

    private void setupPlaylistList() {
        playlistList.setItems(playlists);
        playlistList.setCellFactory(lv -> {
            ListCell<Playlist> cell = new ListCell<>() {
                @Override
                protected void updateItem(Playlist item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item.getNome() + " (" + item.getMusicas().size() + " músicas)");
                }
            };

            ContextMenu contextMenu = new ContextMenu();
            MenuItem addToQueueItem = new MenuItem("Adicionar à fila de reprodução");
            addToQueueItem.setOnAction(e -> {
                Playlist playlist = cell.getItem();
                if (playlist != null && musicPlayerController != null && !playlist.getMusicas().isEmpty()) {
                    musicPlayerController.addToQueue(playlist.getMusicas());
                } else if (playlist != null && playlist.getMusicas().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Playlist Vazia");
                    alert.setHeaderText(null);
                    alert.setContentText("Esta playlist não contém músicas.");
                    alert.showAndWait();
                }
            });
            contextMenu.getItems().add(addToQueueItem);

            cell.setContextMenu(contextMenu);
            return cell;
        });

        playlistList.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && !playlistList.getSelectionModel().isEmpty()) {
                mostrarEditorDePlaylist(playlistList.getSelectionModel().getSelectedItem());
            }
        });
    }

    private void setupButtons() {
        addPlaylistButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Nova Playlist");
            dialog.setHeaderText("Nome da Playlist:");
            dialog.showAndWait().ifPresent(name -> {
                Playlist newPlaylist = new Playlist(0, name, new ArrayList<>());
                playlistDAO.addPlaylist(newPlaylist);
                playlists.add(newPlaylist);
            });
        });

        removePlaylistButton.setOnAction(e -> {
            Playlist selected = playlistList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                playlistDAO.deletePlaylist(selected.getId());
                playlists.remove(selected);
            }
        });
    }

    private void mostrarEditorDePlaylist(Playlist playlist) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Editar Playlist: " + playlist.getNome());

        ListView<Musica> playlistMusicas = new ListView<>(FXCollections.observableArrayList(playlist.getMusicas()));
        ListView<Musica> allSongs = new ListView<>(FXCollections.observableArrayList(musicDAO.getAllMusicas()));

        Button botaoAdicionar = new Button("Adiciona →");
        Button botaoRemover = new Button("← Remove");

        botaoAdicionar.setOnAction(e -> {
            Musica selected = allSongs.getSelectionModel().getSelectedItem();
            if (selected != null) {
                playlist.adicionarMusica(selected);
                playlistDAO.updatePlaylist(playlist);
                playlistMusicas.getItems().add(selected);
                playlistList.refresh();
            }
        });

        botaoRemover.setOnAction(e -> {
            Musica selecionada = playlistMusicas.getSelectionModel().getSelectedItem();
            if (selecionada != null) {
                playlist.removerMusica(selecionada);
                playlistDAO.updatePlaylist(playlist);
                playlistMusicas.getItems().remove(selecionada);
                playlistList.refresh();
            }
        });

        HBox conteudo = new HBox(10, allSongs, new VBox(botaoAdicionar, botaoRemover), playlistMusicas);
        conteudo.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(conteudo);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }
} 