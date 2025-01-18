package com.example.marvelsnaptracker.manager.normal;

import com.example.marvelsnaptracker.MainApplication;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import lombok.Getter;

import java.io.IOException;

/**
 * root view를 제외한 하위 view에 대하여 컨트롤러 역할을 수행하는 interface 역할
 * 이는 root view의 화면 전화 버튼 해당 view를 하나로 묶어서 사용하기 위함.
 * 일종의 composite 패턴 느낌.
 */
public class TaskbarViewManager {
    // 현재 활성화된 화면에 관련된 Button 및 Label 추가할 class
    private final String BUTTON_ACTIVE = "now-active";
    private final String LABEL_ACTIVE = "under-line-active";

    // 각 view의 loader 과 view data
    private FXMLLoader loader;

    @Getter
    private VBox view;

    // 각 view의 관리를 위한 button과 라벨
    @Getter
    private Button controlButton;
    @Getter
    private Label controlLabel;

    public TaskbarViewManager(String name, Button button, Label label) {
        controlButton = button;
        controlLabel = label;
        addButtonHoverCSS();

        try {
            loader = new FXMLLoader(MainApplication.class.getResource(name));
            view = loader.load();
        } catch (IOException e) {
            System.out.printf("load error: %s\n", name);
        }
    }

    /**
     * 버튼에 hover 효과를 추가 함.
     */
    private void addButtonHoverCSS() {
        int sSize = 0;
        int eSize = 75;

        // hover 했을 때
        controlButton.setOnMouseEntered(event -> {

            // 현재 화면의 버튼인 경우 hover 동작 X
            if (controlButton.getStyleClass().contains(BUTTON_ACTIVE)) {
                return;
            }

            Timeline expand = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(controlLabel.prefWidthProperty(), sSize)),
                    new KeyFrame(Duration.seconds(0.5),
                            new KeyValue(controlLabel.prefWidthProperty(), eSize))
            );
            expand.play();
        });

        // hover 벗어 났을 때
        controlButton.setOnMouseExited(event -> {

            // 현재 화면의 버튼인 경우 hover 취소
            // 현재 화면의 버튼인 경우 hover 동작 X
            if (controlButton.getStyleClass().contains(BUTTON_ACTIVE)) {
                return;
            }

            Timeline collapse = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(controlLabel.prefWidthProperty(), controlLabel.getPrefWidth())),
                    new KeyFrame(Duration.seconds(0.5),
                            new KeyValue(controlLabel.prefWidthProperty(), sSize))

            );
            collapse.play();
        });
    }

    /**
     * 현재 view를 root view에 보여주는 것을 의미.
     *
     * @param contentBox 현재를 view를 담을 부모 view.
     */
    public void activeView(VBox contentBox) {
        // 버튼에 active 추가

        // 해당 뷰가 이미 활성화 되어 있으면 패스.
        if (controlButton.getStyleClass().contains(BUTTON_ACTIVE))
            return;

        // 활성화 됨을 나타내는 css 추가
        controlButton.getStyleClass().add(BUTTON_ACTIVE);
        controlLabel.getStyleClass().add(LABEL_ACTIVE);

        // 현재 view를 부모 view에 담음.
        contentBox.getChildren().setAll(view);
    }

    /**
     * 현재 view를 비활성화 시킴.
     */
    public void deactivateView() {
        // 활성화 됨을 나타내는 css 삭제
        controlButton.getStyleClass().remove(BUTTON_ACTIVE);
        controlLabel.getStyleClass().remove(LABEL_ACTIVE);
    }
}
