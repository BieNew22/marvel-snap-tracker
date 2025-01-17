package com.example.marvelsnaptracker.controller.normal;

import com.example.marvelsnaptracker.MainApplication;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Setter;

import java.io.IOException;

public class RootViewController {

    @FXML private VBox contentBox;

    @FXML private Button minimizeButton;

    @FXML private Button closeButton;

    // 화면 전환용 관련 button 하단 hover 적용할 vbox
    @FXML private Button mainButton;
    @FXML private Label mainLabel;
    private VBox mainWindow;

    @FXML private Button deckButton;
    @FXML private Label deckLabel;
    private VBox deckWindow;

    // 기능을 정했지만 이름은 미정
    @FXML private Button testButton;
    @FXML private Label testLabel;
    
    // 현재 활성화된 화면에 관련된 Button 및 Label 추가할 class
    private final String BUTTON_ACTIVE = "now-active";
    private final String LABEL_ACTIVE = "under-line-active";
    
    @Setter
    private Stage primaryStage;

    @FXML
    public void initialize() {
        // taskbar 버튼에 이벤트 추가
        // 창 최소화 버튼 이벤트 추가
        minimizeButton.setOnAction(e -> {
            if (primaryStage != null)
                primaryStage.setIconified(true);
        });

        // 창 닫기 버튼 이벤트 추가
        closeButton.setOnAction(e -> {
            if (primaryStage != null)
                primaryStage.close();
        });

        // 화면 전환 번튼에 css 추가
        windowChangeButtonCSS(mainButton, mainLabel);
        windowChangeButtonOnClick(mainButton, mainLabel);

        windowChangeButtonCSS(deckButton, deckLabel);
        windowChangeButtonOnClick(deckButton, deckLabel);

        windowChangeButtonCSS(testButton, testLabel);
        windowChangeButtonOnClick(testButton, testLabel);

        // 시작 화면 로드 : 현재는 decks-view.fxml
        loadAllScreen();

        contentBox.getChildren().setAll(mainWindow);
    }

    /**
     * 모든 view를 미리 로드 함.
     */
    private void loadAllScreen() {
        FXMLLoader loader;
        try {
            // 메인 뷰 로딩
//            String mainFile = "normal-view/main-view.fxml";

            String deckFile = "normal-view/decks-view.fxml";
            loader = new FXMLLoader(MainApplication.class.getResource(deckFile));
            deckWindow = loader.load();

            // TODO : 임시 메인 화면
            String mainFile = "normal-view/test-view.fxml";
            loader = new FXMLLoader(MainApplication.class.getResource(mainFile));
            mainWindow = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 화면 전환용 버튼에 hover 효과를 추가 함.
     *
     * @param button 해당 버튼
     * @param label 해당 버튼의 하단에 존재하는 Label
     */
    private void windowChangeButtonCSS(Button button, Label label) {
        int sSize = 0;
        int eSize = 75;

        // hover 했을 때
        button.setOnMouseEntered(event -> {

            // 현재 화면의 버튼인 경우 hover 동작 X
            if (button.getStyleClass().contains(BUTTON_ACTIVE)) {
                return;
            }

            Timeline expand = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(label.prefWidthProperty(), sSize)),
                    new KeyFrame(Duration.seconds(0.5),
                            new KeyValue(label.prefWidthProperty(), eSize))
            );
            expand.play();
        });

        // hover 벗어 났을 때
        button.setOnMouseExited(event -> {

            // 현재 화면의 버튼인 경우 hover 취소
            // 현재 화면의 버튼인 경우 hover 동작 X
            if (button.getStyleClass().contains(BUTTON_ACTIVE)) {
                return;
            }

            Timeline collapse = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(label.prefWidthProperty(), label.getPrefWidth())),
                    new KeyFrame(Duration.seconds(0.5),
                            new KeyValue(label.prefWidthProperty(), sSize))

            );
            collapse.play();
        });
    }

    /**
     * 화면 전환용 버튼에 클릭 효과 추가
     *
     * @param button 해당 버튼
     * @param label 해당 버튼 라벨               
     */
    @FXML
    private void windowChangeButtonOnClick(Button button, Label label) {
        
        button.setOnAction(event -> {
            // 1. 현재 display 중인 화면 관련 버튼인 경우 아무 것도 동작 X
            if (button.getStyleClass().contains(BUTTON_ACTIVE)){
                return;
            }

            // 2. 다른 버튼의 now-active를 없애기
            mainButton.getStyleClass().remove(BUTTON_ACTIVE);
            mainLabel.getStyleClass().remove(LABEL_ACTIVE);
            
            deckButton.getStyleClass().remove(BUTTON_ACTIVE);
            deckLabel.getStyleClass().remove(LABEL_ACTIVE);
            
            testButton.getStyleClass().remove(BUTTON_ACTIVE);
            testLabel.getStyleClass().remove(LABEL_ACTIVE);
            
            // 3. 현재 클릭한 버튼에 active 주기
            button.getStyleClass().add(BUTTON_ACTIVE);
            label.getStyleClass().add(LABEL_ACTIVE);

            // 4. 화면 전환 하기.
            switch (button.getText().toLowerCase()) {
                case "deck":
                    contentBox.getChildren().setAll(deckWindow);
                    break;
                case "main":
                    contentBox.getChildren().setAll(mainWindow);
                    break;
                default:
                    break;
            }
        });
    }
}
