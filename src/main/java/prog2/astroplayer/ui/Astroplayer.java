package prog2.astroplayer.ui;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import prog2.astroplayer.DAO.MusicDAO;
import prog2.astroplayer.DAO.PlaylistDAO;
import prog2.astroplayer.DAO.impl.MusicDAOImpl;
import prog2.astroplayer.DAO.impl.PlaylistDAOImpl;
import prog2.astroplayer.entities.Musica;
import prog2.astroplayer.entities.Playlist;

import java.io.File;
import java.util.ArrayList;

public class Astroplayer extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;
    private MediaPlayer mediaPlayer;
    private final ObservableList<Musica> todasMusicas = FXCollections.observableArrayList();
    private final ObservableList<Musica> currentQueue = FXCollections.observableArrayList();
    private final ObservableList<Playlist> playlists = FXCollections.observableArrayList();
    private int currentTrackIndex = -1;

    private final MusicDAO musicDAO = new MusicDAOImpl();
    private final PlaylistDAO playlistDAO = new PlaylistDAOImpl();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        todasMusicas.addAll(musicDAO.getAllMusicas());
        playlists.addAll(playlistDAO.getAllPlaylists());
        initRootLayout();
        showMusicaPlayerScreen();
    }


    private void initRootLayout() {
        rootLayout = new BorderPane();
        Scene scene = new Scene(rootLayout, 800, 600);
        primaryStage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("styles/general.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("styles/button.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("styles/slider.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("styles/slider.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("styles/list.css").toExternalForm());

        // Menu de navegação
        MenuBar menuBar = new MenuBar();
        Menu menuNavegacao = new Menu("Navegação");
        MenuItem playerItem = new MenuItem("Player");
        MenuItem playlistsItem = new MenuItem("Playlists");
        MenuItem musicasItem = new MenuItem("Músicas");

        menuNavegacao.getItems().addAll(playerItem, playlistsItem, musicasItem);
        menuBar.getMenus().add(menuNavegacao);
        rootLayout.setTop(menuBar);

        // Event handlers
        playerItem.setOnAction(e -> showMusicaPlayerScreen());
        playlistsItem.setOnAction(e -> showPlaylistScreen());
        musicasItem.setOnAction(e -> showMusicaManagementScreen());

        primaryStage.show();
    }

    // Tela do Player
    private void showMusicaPlayerScreen() {
        VBox musicPlayerScreen = new VBox(10);
        musicPlayerScreen.setPadding(new Insets(10));

        // Lista de músicas disponíveis
        TableView<Musica> musicTable = new TableView<>();
        musicTable.setItems(todasMusicas);

        TableColumn<Musica, String> titleCol = new TableColumn<>("Nome");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("nome"));
        TableColumn<Musica, String> artistCol = new TableColumn<>("Artista");
        artistCol.setCellValueFactory(new PropertyValueFactory<>("artista"));
        musicTable.getColumns().addAll(titleCol, artistCol);

        // Fila de músicas
        ListView<Musica> queueList = new ListView<>(currentQueue);

        // Controles do player
        Button playButton = new Button("▶");
        Button pauseButton = new Button("⏸");
        Button nextButton = new Button("▶▶");
        Slider progressSlider = new Slider();
        Label currentTime = new Label("00:00");
        Button previousButton = new Button("◀◀");
        Slider volumeSlider = new Slider(0, 1, 0.5);
        volumeSlider.setPrefWidth(100);


        HBox controls = new HBox(
                10, previousButton, playButton, pauseButton, nextButton,
                new Label("Volume:"), volumeSlider, progressSlider, currentTime
        );

        // Adiciona contexto à lista
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
                            .otherwise(contextMenu)
            );
            return cell;
        });

        // Manipula eventos
        musicTable.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                Musica selected = musicTable.getSelectionModel().getSelectedItem();
                currentQueue.add(selected);
                if (currentTrackIndex == -1) {
                    tocarMusica(0);
                }
            }
        });

        playButton.setOnAction(e -> {
            if (mediaPlayer != null) mediaPlayer.play();
        });

        pauseButton.setOnAction(e -> {
            if (mediaPlayer != null) mediaPlayer.pause();
        });

        SplitPane splitPane = new SplitPane(musicTable, queueList);
        musicPlayerScreen.getChildren().addAll(splitPane, controls);
        rootLayout.setCenter(musicPlayerScreen);

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
                        progressSlider.getValue() / 100 * mediaPlayer.getTotalDuration().toSeconds()
                ));
            }
        });

    }

    private String formatTime(Duration duration) {
        int minutes = (int) duration.toMinutes();
        int seconds = (int) duration.toSeconds() % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void tocarMusica(int index) {
        if (index < 0 || index >= currentQueue.size()) return;
        currentTrackIndex = index;
        Musica music = currentQueue.get(index);
        Media media = new Media(music.getArquivo().toURI().toString());

        if (mediaPlayer != null) mediaPlayer.dispose();
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();

        mediaPlayer.setOnEndOfMedia(() -> tocarMusica(currentTrackIndex + 1));
    }

    // Tela de gerenciamento de playlists
    private void showPlaylistScreen() {
        VBox telaPlaylists = new VBox(10);
        telaPlaylists.setPadding(new Insets(10));

        ListView<Playlist> listaPlaylists = new ListView<>(playlists);
        Button botaoAdicionaPlaylists = new Button("Adicionar Playlist");
        Button botaoRemoverPlaylists = new Button("Remover Playlist");

        HBox playlistControls = new HBox(10, botaoAdicionaPlaylists, botaoRemoverPlaylists);
        telaPlaylists.getChildren().addAll(listaPlaylists, playlistControls);
        rootLayout.setCenter(telaPlaylists);

        botaoAdicionaPlaylists.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Nova Playlist");
            dialog.setHeaderText("Nome da Playlist:");
            dialog.showAndWait().ifPresent(name -> {
                Playlist newPlaylist = new Playlist(0, name, new ArrayList<>());
                playlistDAO.addPlaylist(newPlaylist);
                playlists.add(newPlaylist);
            });
        });

        botaoRemoverPlaylists.setOnAction(e -> {
            Playlist selected = listaPlaylists.getSelectionModel().getSelectedItem();
            if (selected != null) {
                playlistDAO.deletePlaylist(selected.getId());
                playlists.remove(selected);
            }
        });

        listaPlaylists.setCellFactory(lv -> {
            ListCell<Playlist> cell = new ListCell<>() {
                @Override
                protected void updateItem(Playlist item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item.getNome() + " (" + item.getMusicas().size() + " músicas)");
                }
            };

            cell.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && !cell.isEmpty()) {
                    mostrarEditorDePlaylist(cell.getItem());
                }
            });

            return cell;
        });

    }

    private void mostrarEditorDePlaylist(Playlist playlist) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Editar Playlist: " + playlist.getNome());

        ListView<Musica> playlistMusicas = new ListView<>(FXCollections.observableArrayList(playlist.getMusicas()));
        ListView<Musica> allSongs = new ListView<>(todasMusicas);

        Button botaoAdicionar = new Button("Adiciona →");
        Button botaoRemover = new Button("← Remove");

        botaoAdicionar.setOnAction(e -> {
            Musica selected = allSongs.getSelectionModel().getSelectedItem();
            if (selected != null) {
                playlist.adicionarMusica(selected);
                playlistDAO.updatePlaylist(playlist);
                playlistMusicas.getItems().add(selected);
            }
        });

        botaoRemover.setOnAction(e -> {
            Musica selecionada = playlistMusicas.getSelectionModel().getSelectedItem();
            if (selecionada != null) {
                playlist.removerMusica(selecionada);
                playlistDAO.updatePlaylist(playlist);
                playlistMusicas.getItems().remove(selecionada);
            }
        });

        HBox conteudo = new HBox(10, allSongs, new VBox(botaoAdicionar, botaoRemover), playlistMusicas);
        conteudo.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(conteudo);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    // Tela de gerenciamento de músicas
    private void showMusicaManagementScreen() {
        VBox telaDeMusicas = new VBox(10);
        telaDeMusicas.setPadding(new Insets(10));

        TableView<Musica> musicTable = new TableView<>(todasMusicas);
        TableColumn<Musica, String> titleCol = new TableColumn<>("Nome");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("nome"));
        TableColumn<Musica, String> artistCol = new TableColumn<>("Artista");
        artistCol.setCellValueFactory(new PropertyValueFactory<>("artista"));
        musicTable.getColumns().addAll(titleCol, artistCol);

        Button addButton = new Button("Adicionar música");
        Button botaoRemover = new Button("Remover música");

        addButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                    if (todasMusicas.stream().anyMatch(m -> m.getArquivo().equals(file))) {
                        showErrorDialog("Música duplicada", "Essa música já foi adicionada a biblioteca", "");
                        return;
                    }

                    Musica music = new Musica(file);
                    musicDAO.addMusica(music);
                    todasMusicas.add(music);
                } catch (Exception ex) {
                    showErrorDialog("Erro", "Não foi possível adicionar a música", ex.getMessage());
                }
            }
        });


        botaoRemover.setOnAction(e -> {
            Musica selecionada = musicTable.getSelectionModel().getSelectedItem();
            if (selecionada != null) {
                todasMusicas.remove(selecionada);
                musicDAO.deleteMusica(selecionada.getId());
            }
        });


        HBox controls = new HBox(10, addButton, botaoRemover);
        telaDeMusicas.getChildren().addAll(musicTable, controls);
        rootLayout.setCenter(telaDeMusicas);
    }


    private void showErrorDialog(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}