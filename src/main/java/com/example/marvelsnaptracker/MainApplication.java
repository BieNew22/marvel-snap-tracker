package com.example.marvelsnaptracker;

import com.example.marvelsnaptracker.decks.Deck;
import com.example.marvelsnaptracker.decks.DeckManager;
import com.example.marvelsnaptracker.utils.EnvManager;
import com.example.marvelsnaptracker.utils.Initializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.util.Iterator;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        // 사용자 덱 정보 초기화하기
        DeckManager.getInstance().initDeck();
        DeckManager.getInstance().showDeckList();
        launch();
    }
}