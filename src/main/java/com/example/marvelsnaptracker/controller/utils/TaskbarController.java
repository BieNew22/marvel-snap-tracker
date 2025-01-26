package com.example.marvelsnaptracker.controller.utils;

import com.example.marvelsnaptracker.MainApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lombok.Setter;

public class TaskbarController {
    @FXML
    private Button minimizeButton;

    @FXML private Button closeButton;

    @FXML
    public void initialize() {
        // taskbar 버튼에 이벤트 추가
        // 창 최소화 버튼 이벤트 추가
        minimizeButton.setOnAction(e -> {
            Stage stage = MainApplication.getPrimaryStage();
            if (stage != null)
                stage.setIconified(true);
        });

        // 창 닫기 버튼 이벤트 추가
        closeButton.setOnAction(e -> {
            Stage stage = MainApplication.getPrimaryStage();
            if (stage != null)
                stage.close();
        });
    }
}
