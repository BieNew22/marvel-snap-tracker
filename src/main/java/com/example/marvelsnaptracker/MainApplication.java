package com.example.marvelsnaptracker;

import com.example.marvelsnaptracker.controller.NormalViewController;
import com.example.marvelsnaptracker.deck.DeckManager;
import com.example.marvelsnaptracker.utils.db.DatabaseDriver;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;

public class MainApplication extends Application {

    private double xOffset;
    private double yOffset;

    @Override
    public void start(Stage stage) throws IOException {
        // FXML 파일 로드
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("root-view/normal-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 510, 700);

        // 컨트롤러에 인스턴스 전달
        NormalViewController controller = fxmlLoader.getController();
        controller.setPrimaryStage(stage);

        // 드래그를 통하여 창 이동 이벤트 추가
        scene.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        // stage 기본 설정
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Marvel Snap");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {

        // 0. DB 초기화
        DatabaseDriver.getInstance().initDB();

        // 1. 사용자 덱 정보 초기화하기
        DeckManager.getInstance().initDeck();

        launch();
    }
}