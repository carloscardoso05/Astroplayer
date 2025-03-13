module prog2.astroplayer.ui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires static lombok;


    opens prog2.astroplayer.ui to javafx.fxml;
    exports prog2.astroplayer.ui;
}