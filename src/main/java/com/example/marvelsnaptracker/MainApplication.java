package com.example.marvelsnaptracker;

import com.example.marvelsnaptracker.deck.DeckManager;
import com.example.marvelsnaptracker.utils.db.DatabaseDriver;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;

import java.io.*;

public class MainApplication extends Application {

    private double xOffset;
    private double yOffset;

    @Getter private static Stage primaryStage;
    @Getter private static Scene mainView;
    @Getter private static Scene gameView;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        // normal view 로드 부분 임시 주석
        // FXML 파일 로드
        String mainViewFile = "root-view/normal-view.fxml";
        FXMLLoader mainLoader = new FXMLLoader(MainApplication.class.getResource(mainViewFile));
        mainView = new Scene(mainLoader.load(), 510, 700);

        String gameViewFile = "root-view/game-view.fxml";
        
        FXMLLoader gameLoader = new FXMLLoader(MainApplication.class.getResource(gameViewFile));
        gameView = new Scene(gameLoader.load(), 350, 90);

        // 드래그를 통하여 창 이동 이벤트 추가
        addDragEvent(mainView, stage);
        addDragEvent(gameView, stage);

        // stage 포커스 listener 추가
        stage.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            // newValue = True : 창에 포커스를 가짐.
            if (!newValue) {
                stage.setScene(gameView);
            }
        }));

        // stage 기본 설정
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Marvel Snap");
        stage.setScene(gameView);
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.show();
    }

    /**
     * 현재 scene 에 창 드래그 이벤트 추가
     *
     * @param scene 현재 scene
     * @param stage 현재 stage
     */
    public void addDragEvent(Scene scene, Stage stage) {
        scene.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    public static void main(String[] args) {

        // 0. DB 초기화
        DatabaseDriver.getInstance().initDB();

        // 1. 사용자 덱 정보 초기화하기
        DeckManager.getInstance().initDeck();

        launch();
    }
}