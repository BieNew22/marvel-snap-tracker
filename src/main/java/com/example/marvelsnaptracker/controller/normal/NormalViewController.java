package com.example.marvelsnaptracker.controller.normal;

import com.example.marvelsnaptracker.manager.normal.TaskbarViewManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Setter;

import java.util.HashMap;

public class NormalViewController {

    @FXML private VBox contentBox;

    @FXML private Button minimizeButton;

    @FXML private Button closeButton;

    // 화면 전환용 관련 button 하단 hover 적용할 vbox
    @FXML private Button mainButton;
    @FXML private Label mainLabel;

    @FXML private Button deckButton;
    @FXML private Label deckLabel;

    // 기능을 정했지만 이름은 미정 (임시 테스트 페이지로 사용)
    @FXML private Button testButton;
    @FXML private Label testLabel;

    private HashMap<String, TaskbarViewManager> managers;

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

        // 화면 전환 번튼에 대한 manager 추가
        managers = new HashMap<>();

        managers.put("MAIN", new TaskbarViewManager("normal-view/main-view.fxml", mainButton, mainLabel));
        managers.put("DECK", new TaskbarViewManager("normal-view/deck-view.fxml", deckButton, deckLabel));
        managers.put("TEST", new TaskbarViewManager("normal-view/test-view.fxml", testButton, testLabel));

        // 메인 화면 로드
        managers.get("MAIN").activeView(contentBox);
    }

    /**
     * 화면 전환용 버튼에 클릭 이벤트 추가
     */
    @FXML
    private void clickWindowChangeButton(ActionEvent event) {
        Button button = (Button) event.getSource();

        String clickKey = button.getText();

        for (String key: managers.keySet()) {
            if (clickKey.equalsIgnoreCase(key))
                managers.get(key).activeView(contentBox);
            else
                managers.get(key).deactivateView();
        }
    }

}
