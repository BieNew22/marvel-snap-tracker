package com.example.marvelsnaptracker.contoller;

import com.example.marvelsnaptracker.deck.Deck;
import com.example.marvelsnaptracker.utils.WebPToPNGConverter;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

public class DeckController {

    @FXML
    private Label title;

    // 각 줄에는 6개의 카드를 보여줌.
    @FXML
    private AnchorPane cardList;

    @FXML Label copyDeckLabel;

    /**
     * deck-view 에서 초기 뷰 설정
     * @param deck deck-view에 보여질 deck
     */
    public void setDeck(Deck deck) {
        // deck 이름 가져오기
        title.setText(deck.getName());

        // HBox에 카드 리스트 추가하기
        ArrayList<String> cards = deck.getCardNameList();

        // 초기 위치
        int posX = 0, posY = 0;

        // 다음 사진의 위치
        int gabX = 75, gabY = 100;

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
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
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

        // copy deck label에 이벤트 추가
        setCopyDeckLabelEvent();
    }

    /**
     * copy deck label에 evnet 추가
     */
    private void setCopyDeckLabelEvent() {
        String formatCSS = "-fx-background-color:" +
                "linear-gradient(from 0%% 0%% to 100%% 100%%, " +
                "rgba(99, 33, 163, 0.70) %.2f%%, rgba(99, 33, 163, 0.5) 50%%);";

        double basicPos = 75.0;
        double hoverPos = 0.0;

        // 기본 배경 색상 설정
        copyDeckLabel.setStyle(String.format(formatCSS, basicPos));

        // hover 이벤트
        copyDeckLabel.setOnMouseEntered(e -> animateBackground(formatCSS, basicPos, hoverPos));
        copyDeckLabel.setOnMouseExited(e -> animateBackground(formatCSS, hoverPos, basicPos));
    }

    /**
     * copy deck label 의 백경색 애니메이션
     * @param formatString copy deck label 의 기본 배경 format
     * @param fromPercent 시작 퍼센트
     * @param toPercent 종료 퍼센트
     */
    private void animateBackground(String formatString, double fromPercent, double toPercent) {
        final double[] currentPercent = {fromPercent};
        double animationDuration = 300; // 밀리초
        double frameRate = 60; // 초당 프레임

        double totalFrames = animationDuration / (1000 / frameRate);
        double step = (toPercent - fromPercent) / totalFrames;

        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (lastUpdate == 0 || now - lastUpdate >= 1_000_000_000 / frameRate) {
                    currentPercent[0] += step;

                    // 스타일 업데이트
                    copyDeckLabel.setStyle(String.format(formatString , currentPercent[0]));

                    lastUpdate = now;

                    // 애니메이션 종료 조건
                    if ((step > 0 && currentPercent[0] >= toPercent) || (step < 0 && currentPercent[0] <= toPercent)) {
                        stop();
                    }
                }
            }
        };
        timer.start();
    }
}
