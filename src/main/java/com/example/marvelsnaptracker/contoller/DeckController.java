package com.example.marvelsnaptracker.contoller;

import com.example.marvelsnaptracker.MainApplication;
import com.example.marvelsnaptracker.decks.Deck;
import com.example.marvelsnaptracker.utils.WebPToPNGConverter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Objects;

public class DeckController {

    @FXML
    private Label title;

    // 각 줄에는 6개의 카드를 보여줌.
    @FXML
    private AnchorPane cardList;

    public void setDeck(Deck deck) {
        // deck 이름 가져오기
        title.setText(deck.getName());

        // HBox에 카드 리스트 추가하기
        ArrayList<String> cards = deck.getCardList();

        // 초기 위치
        int posX = 0, posY = 0;

        // 다음 사진의 위치
        int gabX = 65, gabY = 85;

        for (int i = 0; i < cards.size(); i++) {
            // 이미지 로드 : png 파일이 없으면 webp 를 다운 받아서 png로 변환
            String name = cards.get(i);
            boolean res = WebPToPNGConverter.getInstance().convert(name);

            String cardPath;
            if (res) {
                cardPath = System.getProperty("user.dir") + "\\images\\" + name + ".png";
            } else {
                cardPath = System.getProperty("user.dir") + "\\images\\Hulk.png";
            }

            Image image = new Image(cardPath);

            // ImageView에 이미지 설정
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(80);
            imageView.setFitWidth(80);
            imageView.getStyleClass().add("no-padding");
            imageView.setPreserveRatio(true);

            // 이미지 위치 설정
            imageView.setLayoutX(posX);
            imageView.setLayoutY(posY);

            // 6장 보여주고 나서 시작 위치를 다음 줄로 옮김.
            if (i == 5) {
                posY += gabY;
                posX = -gabX;
            }
            posX += gabX;

            cardList.getChildren().add(imageView);
        }
    }
}
