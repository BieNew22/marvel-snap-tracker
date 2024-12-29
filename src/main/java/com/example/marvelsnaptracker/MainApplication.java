package com.example.marvelsnaptracker;

import com.example.marvelsnaptracker.utils.EnvManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.Iterator;

public class MainApplication { //extends Application {
//    @Override
//    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();
//    }

    public static void main(String[] args) {
//        launch();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String fileName = "CollectionState.json";
        // root -> ServerState -> Decks : 사용자 덱 정보
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String totalPath = EnvManager.getInstance().BASE_DIR + fileName;

            System.out.println(totalPath);
            JsonNode rootNode = objectMapper.readTree(new File(totalPath));

            while (true) {
                Iterator<String> fieldNames = rootNode.fieldNames();

                while (fieldNames.hasNext())
                    System.out.println(fieldNames.next());

                String cmd = br.readLine();

                if (cmd.equals("e"))
                    break;
                else
                    rootNode = rootNode.get(cmd);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}