package prog2.astroplayer.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Astroplayer extends Application {
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initRootLayout();
    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader(Astroplayer.class.getResource("/fxml/root_layout.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            primaryStage.setScene(scene);
            
            scene.getStylesheets().add(Astroplayer.class.getResource("/styles/general.css").toExternalForm());
            scene.getStylesheets().add(Astroplayer.class.getResource("/styles/button.css").toExternalForm());
            scene.getStylesheets().add(Astroplayer.class.getResource("/styles/slider.css").toExternalForm());
            scene.getStylesheets().add(Astroplayer.class.getResource("/styles/list.css").toExternalForm());
            scene.getStylesheets().add(Astroplayer.class.getResource("/styles/table.css").toExternalForm());

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}