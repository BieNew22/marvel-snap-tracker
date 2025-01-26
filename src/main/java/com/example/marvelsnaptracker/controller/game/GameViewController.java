package com.example.marvelsnaptracker.controller.game;

import com.example.marvelsnaptracker.MainApplication;
import com.example.marvelsnaptracker.manager.normal.TaskbarViewManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.HashMap;

public class GameViewController {

    @FXML private HBox taskbarContainer;

    @FXML
    public void initialize() {
        // taskbar 초기화
        try {
            String taskbarFile = "util-view/taskbar.fxml";
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(taskbarFile));
            HBox content = loader.load();

            taskbarContainer.getChildren().add(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
