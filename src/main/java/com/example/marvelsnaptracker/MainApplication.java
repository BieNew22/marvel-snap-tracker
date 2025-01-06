package com.example.marvelsnaptracker;

import com.example.marvelsnaptracker.decks.DeckManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 510, 700);
        stage.setTitle("Marvel Snap");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {

        // 사용자 덱 정보 초기화하기
        DeckManager.getInstance().initDeck();
        DeckManager.getInstance().showDeckList();
        launch();
    }
}