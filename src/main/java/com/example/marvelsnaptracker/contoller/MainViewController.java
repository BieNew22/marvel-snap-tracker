package com.example.marvelsnaptracker.contoller;

import com.example.marvelsnaptracker.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;

public class MainViewController {

    @FXML
    private VBox contentBox;

    @FXML
    private Button minimizeButton;

    @FXML
    private Button closeButton;

    @Setter
    private Stage primaryStage;

    @FXML
    public void initialize() {
        // taskbar 버튼에 이벤트 추가
        // 창 최소화 버튼 이벤트 추가
        minimizeButton.setOnAction(e -> {
            if (primaryStage != null)
                primaryStage.setIconified(true);
        });

        // 창 닫기 버튼 이벤트 추가
        closeButton.setOnAction(e -> {
            if (primaryStage != null)
                primaryStage.close();
        });

        // 시작 화면 로드 : 현재는 deck-list-view.fxml
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("deck-list-view.fxml"));
            ScrollPane screen = loader.load();

            contentBox.getChildren().setAll(screen);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
