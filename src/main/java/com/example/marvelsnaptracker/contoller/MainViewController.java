package com.example.marvelsnaptracker.contoller;

import com.example.marvelsnaptracker.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainViewController {

    @FXML
    private VBox contentBox;

    @FXML
    public void initialize() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("deck-list-view.fxml"));
            ScrollPane screen = loader.load();

            contentBox.getChildren().setAll(screen);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
