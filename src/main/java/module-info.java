module prog2.astroplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires static lombok;
    requires java.sql;
    requires mp3agic;
    requires jaudiotagger;


    exports prog2.astroplayer.ui;
    exports prog2.astroplayer.DAO;
    exports prog2.astroplayer.db;
    exports prog2.astroplayer.entities;
    exports prog2.astroplayer.controllers;
    exports prog2.util;
}