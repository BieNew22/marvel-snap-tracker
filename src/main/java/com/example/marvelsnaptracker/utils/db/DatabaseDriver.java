package com.example.marvelsnaptracker.utils.db;

import com.example.marvelsnaptracker.card.Card;
import com.example.marvelsnaptracker.utils.EnvManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * db connection 과 db 초기화 부분을 담당
 * singleton
 */
public class DatabaseDriver {
    @Getter
    private static final DatabaseDriver instance = new DatabaseDriver();
    @Getter
    private Connection connection;

    private DatabaseDriver() {
        try {
            String dbURL = "jdbc:sqlite:tracker.db";
            connection = DriverManager.getConnection(dbURL);
        } catch (SQLException e) {
            System.out.println("Conn error : " + e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * db를 초기화 함.
     */
    public void initDB() {
        initTable();
        initCardsData();
    }

    /**
     * init.sql을 통하여 DB 테이블 생성.
     */
    private void initTable() {
        String initSqlFile = "init.sql";

        // init.sql 파일에 실행할 쿼리 읽어 오기
        ArrayList<String> queryList = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(initSqlFile));

            String line;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                // init.sql 에서 주석 제거하기
                if (line.startsWith("--") || line.isEmpty())
                    continue;

                sb.append(line).append("\n");

                // 쿼리 구분하기
                if (line.endsWith(";")) {
                    queryList.add(sb.toString());
                    sb = new StringBuilder();
                }
            }
        } catch (IOException e) {
            System.out.println("DatabaseDriver.initTable (read init.sql) : " + e.getMessage());
            System.exit(-1);
        }

        // init.sql 쿼리들을 실행
        try (Statement stmt = connection.createStatement()) {

            // init.sql 의 모든 쿼리 추가
            for (String sql: queryList) {
                stmt.addBatch(sql);
            }

            // 실행.
            stmt.executeBatch();

        } catch (SQLException e) {
            System.out.println("DatabaseDriver.initTable (exe sql) : " + e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * cards.json을 통하여 card DB에 데이터 추가.
     */
    private void initCardsData() {
        try {
            // 연결 설정
            HttpURLConnection httpConnection =
                    (HttpURLConnection) new URL(EnvManager.getInstance().CARD_DATA_URL).openConnection();

            System.out.println(EnvManager.getInstance().CARD_DATA_URL);

            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty("Accept", "application/json");
            httpConnection.setInstanceFollowRedirects(true);    // 리다이렉트 허용

            int status = httpConnection.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                // 통신 성공 -> 데이터 잘 불러옴.

                // 데이터 처리하기
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(httpConnection.getInputStream()))) {

                    // 0. 데이터 읽어오기
                    StringBuilder responseJson = new StringBuilder();

                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        responseJson.append(inputLine);
                    }

                    // 1. Jackson으로 파싱
                    ObjectMapper objectMapper = new ObjectMapper();

                    List<Card> cardList =  objectMapper.readValue(
                            responseJson.toString(),
                            new TypeReference<>() {
                            }
                    );

                    // 2. db에 데이터 추가
                    CardService.getInstance().insertOrUpdateCards(cardList);
                } catch (IOException e) {
                    System.out.println("DatabaseDriver.initCardData : Parsing & Read Data error");
                    System.exit(-1);
                }
            } else {
                // 통신 실패 -> 종료
                System.out.println("DatabaseDriver.initCardData : Can't connect CARD_DATA_URL");
                System.exit(-1);
            }
        } catch (Exception e) {
            System.out.println("DatabaseDriver.initCardData : " + e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }


    /**
     * DB에 덱 이름 업데이트
     *
     * @param deckID 이름 변경할 덱 ID
     * @param newName 덱 새로운 이름
     */
    public void updateDeckName(String deckID, String newName) {
        String sql = "UPDATE deck SET name = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setString(2, deckID);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("DatabaseDriver.updateDeckName : " + e.getMessage());
        }
    }


    // DB 덱 ID 업데이트할 때 새로운 ID에 더할 salt 값.
    int salt = 0;

    /**
     * DB 덱 ID 업데이트
     *
     * @param deckID 업데이트할 덱 ID
     */
    public void updateDeckID(String deckID) {
        String sql = "UPDATE deck SET name = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd-HH-mm-ss");

            pstmt.setString(1, now.format(formatter) + salt);
            pstmt.setString(2, deckID);

            salt += 1;

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("DatabaseDriver.updateDeckName : " + e.getMessage());
        }
    }
}
