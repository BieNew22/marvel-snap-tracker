package com.example.marvelsnaptracker.controller.game;

import com.example.marvelsnaptracker.MainApplication;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.io.IOException;

public class GameViewController {

    @FXML private HBox taskbarContainer;
    @FXML private Label mainLabel;
    @FXML private Button expandButton;

    @FXML
    public void initialize() {
        // taskbar 초기화
        try {
            String taskbarFile = "util-view/taskbar.fxml";
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(taskbarFile));
            HBox content = loader.load();

            taskbarContainer.getChildren().add(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // expand 버튼 이벤트 추가 : mainView 으로 화면 전환
        expandButton.setOnAction(e -> {
            Stage stage = MainApplication.getPrimaryStage();

            stage.setScene(MainApplication.getMainView());
        });

        // 메인 라벨에 타이포 효과 추가하기.
        Timeline timeline = new Timeline();
        int initTime = 200;
        initTime += addTypoEffect(timeline, "Welcome!!", initTime, 200);
        initTime += addTypoEffect(timeline, "Good Day!!", initTime, 200);

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // 글자 반짝이는 효과 -> 미정.
//        Timeline timeline2 = new Timeline(
//                new KeyFrame(Duration.seconds(0.5), e -> mainLabel.setOpacity(0.3)),
//                new KeyFrame(Duration.seconds(1.0), e -> mainLabel.setOpacity(1.0))
//        );
//        timeline2.setCycleCount(Timeline.INDEFINITE);
//        timeline2.play();
    }

    /**
     * 라벨에 타이포 효과 추가 함수.
     *
     * @param timeline  타이포를 진행할 timeline
     * @param content   타이포할 문자열
     * @param initTime  애니메이션 시작 시간
     * @param timeGab   각 글자간 타이핑 시간 (밀리초)
     * @return          애니메이션 종료 시간.
     */
    int addTypoEffect(Timeline timeline, String content, int initTime, int timeGab) {
        for (int i = 0; i < content.length(); i++) {
            final int index = i;
            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.millis(initTime),
                            e -> mainLabel.setText(content.substring(0, index + 1)))
            );
            initTime += timeGab;
        }

        for (int i = content.length() - 1; i >= 0; i--) {
            final int index = i;
            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.millis(initTime),
                            e -> mainLabel.setText(content.substring(0, index)))
            );
            initTime += timeGab;
        }

        return initTime;
    }
}
