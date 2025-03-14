module prog2.astroplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires static lombok;
    requires java.sql;


    opens prog2.astroplayer.ui to javafx.fxml;
    exports prog2.astroplayer.ui;
}