package com.example.marvelsnaptracker.utils.db;

import com.example.marvelsnaptracker.card.Card;
import com.example.marvelsnaptracker.card.CardManager;
import com.example.marvelsnaptracker.deck.Deck;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.util.ArrayList;

/**
 * db deck table CURD 관련 서비스 제공
 * singleton
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeckService {
    @Getter
    private static final DeckService instance = new DeckService();

    /**
     * DB에 덱 정보를 추가.
     *
     * @param deck 추가할 덱 정보
     */
    public void insertDeck(Deck deck) {

        Connection connection = DatabaseDriver.getInstance().getConnection();

        // 1 : deck.id, 2 : deck.name, 3 : deck.win_rate, 4 : deck.game_count, 5 : deck.last_play
        String insertDeckQuery = "INSERT INTO deck VALUES(?, ?, ?, ?, ?)";
        String insertCardQuery = "INSERT INTO deck_card (owner, name) VALUES(? ,?)";
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
                for (Card card: deck.getCardList()) {
                    pstmtCard.setString(1, deck.getId());
                    pstmtCard.setString(2, card.getName());
                    pstmtCard.addBatch();
                }

                pstmtCard.executeBatch();
            }

        } catch (SQLException e) {
            System.out.println("DeckService.insertDeck : " + e.getMessage());
            System.out.println(deck);
        }
    }

    /**
     * DB에 있는 모든 덱을 조회하여 리턴
     *
     * @return Deck List
     */
    public ArrayList<Deck> selectAllDecks() {
        Connection connection = DatabaseDriver.getInstance().getConnection();

        String query = "SELECT * FROM deck";
        try (Statement statement = connection.createStatement()) {
            // DB 모든 덱 조회
            ResultSet decks = statement.executeQuery(query);

            ArrayList<Deck> result = new ArrayList<>();
            CardService cs = CardService.getInstance();
            while (decks.next()) {
                // 덱 ID로 덱에 있는 모든 카드 조회
                String ID = decks.getString("id");

                // 덱 조립
                Deck now = new Deck();
                now.setId(ID);
                now.setName(decks.getString("name"));
                now.setWinRate(decks.getDouble("win_rate"));
                now.setPlayCount(decks.getInt("game_count"));
                now.setLastPlay(decks.getString("last_play"));
                now.setCardList(cs.selectDeckAllCard(ID));

                result.add(now);
            }

            return result;
        } catch (Exception e) {
            System.out.println("DeckService.getAllDecks : " + e.getMessage());
            return new ArrayList<>();
        }
    }

}
