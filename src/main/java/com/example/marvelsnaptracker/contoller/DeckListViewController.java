package com.example.marvelsnaptracker.contoller;

import com.example.marvelsnaptracker.MainApplication;
import com.example.marvelsnaptracker.deck.Deck;
import com.example.marvelsnaptracker.deck.DeckManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Iterator;

public class DeckListViewController {

    // 사용자 덱 정보들을 담을 컨테이너
    @FXML
    private VBox deckContainer;

    /**
     * 메인 화면, 사용자의 모든 덱 정보를 초기화 함.
     */
    @FXML
    public void initialize() {
        try {
            Iterator<Deck> decks = DeckManager.getInstance().getAllDeckInfo();

            while (decks.hasNext()) {
                Deck deck = decks.next();

                // deck-view 를 로드 함.
                FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("deck-view.fxml"));
                VBox childNode = loader.load();

                // 현재 deck-view에 deck 정보를 넘김
                DeckViewController controller = loader.getController();
                controller.setDeck(deck);

                deckContainer.getChildren().add(childNode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}