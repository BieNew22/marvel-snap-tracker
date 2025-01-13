package com.example.marvelsnaptracker.utils;

import com.example.marvelsnaptracker.decks.Card;
import com.example.marvelsnaptracker.decks.Deck;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
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
 * db 와 통신 하는 부분 관리 하는 클래스, singleton
 */
public class DatabaseDriver {
    @Getter
    private static final DatabaseDriver instance = new DatabaseDriver();
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
        try (Statement stmt = connection.createStatement()) {
            // init.sql 파일 읽기
            BufferedReader br = new BufferedReader(new FileReader(initSqlFile));

            String line;
            StringBuilder sb = new StringBuilder();

            ArrayList<String> queryList = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                // init.sql 에서 주석 제거하기
                if (!line.startsWith("--") && !line.isEmpty())
                    sb.append(line).append("\n");

                // 쿼리 구분하기
                if (line.endsWith(";")) {
                    queryList.add(sb.toString());
                    sb = new StringBuilder();
                }
            }

            // init.sql 의 모든 쿼리 추가
            for (String sql: queryList) {
                stmt.addBatch(sql);
            }

            // 실행.
            stmt.executeBatch();
        } catch (IOException | SQLException e) {
            System.out.println("DatabaseDriver.initTable : " + e.getMessage());
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
                    insertOrUpdateCards(cardList);
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
     * 카드 데이터를 DB에 저장 함.
     * 이미 있는 경우 데이터를 업데이트 수행
     *
     * @param cards DB에 저장할 카드 데이터
     */
    private void insertOrUpdateCards(List<Card> cards) {
        String sql = """
        INSERT INTO card (name, cost, power)
        VALUES (?, ?, ?)
        ON CONFLICT(name) DO UPDATE SET
            cost = excluded.cost,
            power = excluded.power;
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            connection.setAutoCommit(false);

            for (Card now: cards) {
                pstmt.setString(1, now.getName());
                pstmt.setInt(2, now.getCost());
                pstmt.setInt(3, now.getPower());
                pstmt.addBatch();
            }

            pstmt.executeBatch();

            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println("DatabaseDriver.insertOrUpdateCards : " + e.getMessage());
            System.exit(-1);
        }

    }

    /**
     * DB에 있는 모든 덱을 조회하여 리턴
     *
     * @return Deck List
     */
    public ArrayList<Deck> getAllDecks() {
        String deckQuery = "SELECT * FROM deck";
        String cardQuery = "SELECT name FROM cards WHERE owner = ?";
        try (Statement statement = connection.createStatement()) {
            // DB 모든 덱 조회
            ResultSet decks = statement.executeQuery(deckQuery);

            ArrayList<Deck> result = new ArrayList<>();
            while (decks.next()) {
                // 덱 ID로 덱에 있는 모든 카드 조회
                String ID = decks.getString("id");

                try (PreparedStatement pstmt = connection.prepareStatement(cardQuery)) {
                    pstmt.setString(1, ID);

                    ResultSet cards = pstmt.executeQuery();

                    ArrayList<String> cardList = new ArrayList<>();
                    while (cards.next()) {
                        cardList.add(cards.getString("name"));
                    }

                    // 덱 조립
                    Deck now = new Deck();
                    now.setId(ID);
                    now.setName(decks.getString("name"));
                    now.setWinRate(decks.getDouble("win_rate"));
                    now.setPlayCount(decks.getInt("game_count"));
                    now.setLastPlay(decks.getString("last_play"));
                    now.setCardList(cardList);

                    result.add(now);
                } catch (SQLException e) {
                    System.out.println("DatabaseDriver.getAllDecks : " + e.getMessage());
                    System.out.println("Deck ID : " + ID);
                }
            }

            return result;
        } catch (Exception e) {
            System.out.println("DatabaseDriver.getAllDecks : " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * DB에 덱 정보를 추가.
     *
     * @param deck 추가할 덱 정보
     */
    public void insertDeck(Deck deck) {
        // 1 : deck.id, 2 : deck.name, 3 : deck.win_rate, 4 : deck.game_count, 5 : deck.last_play
        String insertDeckQuery = "INSERT INTO deck VALUES(?, ?, ?, ?, ?)";
        String insertCardQuery = "INSERT INTO cards (owner, name) VALUES(? ,?)";
        try (PreparedStatement pstmtDeck = connection.prepareStatement(insertDeckQuery);
            PreparedStatement pstmtCard = connection.prepareStatement(insertCardQuery)) {

            // db에 덱 넣기
            pstmtDeck.setString(1, deck.getId());
            pstmtDeck.setString(2, deck.getName());
            pstmtDeck.setDouble(3, deck.getWinRate());
            pstmtDeck.setInt(4, deck.getPlayCount());
            pstmtDeck.setString(5, deck.getLastPlay());

            int result = pstmtDeck.executeUpdate();

            // 덱 삽입 성공
            if (result > 0) {
                // 덱 카드 삽입
                for (String card: deck.getCardList()) {
                    pstmtCard.setString(1, deck.getId());
                    pstmtCard.setString(2, card);
                    pstmtCard.addBatch();
                }

                pstmtCard.executeBatch();
            }

        } catch (SQLException e) {
            System.out.println("DatabaseDriver.insertDeck : " + e.getMessage());
            System.out.println(deck);
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
