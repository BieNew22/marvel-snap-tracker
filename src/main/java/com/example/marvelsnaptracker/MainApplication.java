package com.example.marvelsnaptracker;

import com.example.marvelsnaptracker.controller.normal.NormalViewController;
import com.example.marvelsnaptracker.deck.DeckManager;
import com.example.marvelsnaptracker.utils.db.DatabaseDriver;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;

import java.io.*;
import java.net.URL;

public class MainApplication extends Application {

    private double xOffset;
    private double yOffset;

    @Getter
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

//        // normal view 로드 부분 임시 주석
//        // FXML 파일 로드
//        String fileName = "root-view/normal-view.fxml";
//        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fileName));
//        Scene scene = new Scene(fxmlLoader.load(), 510, 700);

        String fileName = "root-view/game-view.fxml";
        
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fileName));
        Scene scene = new Scene(fxmlLoader.load(), 350, 90);

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