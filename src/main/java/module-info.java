module prog2.astroplayer {
    requires javafx.controls;
    requires javafx.fxml;


    opens prog2.astroplayer to javafx.fxml;
    exports prog2.astroplayer;
}