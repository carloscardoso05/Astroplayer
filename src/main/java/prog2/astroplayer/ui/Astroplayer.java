package prog2.astroplayer.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import prog2.astroplayer.controllers.MusicController;

import java.util.Objects;

public class Astroplayer extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("player.fxml")));
        primaryStage.setTitle("FXML MP3 Player");
        primaryStage.setScene(new Scene(root, 400, 200));
        primaryStage.show();
    }

    public static void main(String[] args) {
        MusicController.salvarMusica("nome", "artista", "album", "genero", 2015, 150, "C:/Donwloads/teste.mp3");
        launch(args);
    }
}