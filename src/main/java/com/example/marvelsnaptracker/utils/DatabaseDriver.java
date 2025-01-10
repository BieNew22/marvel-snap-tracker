package com.example.marvelsnaptracker.utils;

import com.example.marvelsnaptracker.decks.Deck;
import lombok.Getter;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

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
     * init.sql 을 통하여 DB 초기화
     */
    public void initDB() {

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
        } catch (IOException e) {
            System.out.println("DatabaseDriver.initDB : " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("DatabaseDriver.initDB : " + e.getMessage());     // 오류 메시지 출력
            System.out.println("SQL State: " + e.getSQLState());    // SQL 상태 코드 출력
            System.out.println("Error Code: " + e.getErrorCode());  // 오류 코드 출력
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
}
