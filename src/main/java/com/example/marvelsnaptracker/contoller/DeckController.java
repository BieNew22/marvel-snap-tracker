package com.example.marvelsnaptracker.contoller;

import com.example.marvelsnaptracker.MainApplication;
import com.example.marvelsnaptracker.decks.Deck;
import com.example.marvelsnaptracker.utils.WebPToPNGConverter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Objects;

public class DeckController {

    @FXML
    private Label title;

    // 각 줄에는 6개의 카드를 보여줌.
    @FXML
    private HBox deckLine1;

    @FXML
    private HBox deckLine2;

    public void setDeck(Deck deck) {
        // deck 이름 가져오기
        title.setText(deck.getName());

        // HBox에 카드 리스트 추가하기
        ArrayList<String> cards = deck.getCardList();

        for (int i = 0; i < cards.size(); i++) {
            // 이미지 로드 : png 파일이 없으면 webp 를 다운 받아서 png로 변환
            String name = cards.get(i);
            boolean res = WebPToPNGConverter.getInstance().convert(name);

            String cardPath;
            if (res) {
                cardPath = System.getProperty("user.dir") + "\\images\\" + name + ".png";
            } else {
                cardPath = System.getProperty("user.dir") + "\\images\\.png";
            }

            Image image = new Image(cardPath);

            // ImageView에 이미지 설정
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(150);
            imageView.setFitWidth(150);
            imageView.setPreserveRatio(true);


            if (i < 6) {
                deckLine1.getChildren().add(imageView);
            } else {
                deckLine2.getChildren().add(imageView);
            }
        }
    }
}
