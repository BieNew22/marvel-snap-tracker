package com.example.marvelsnaptracker.utils.db;

import com.example.marvelsnaptracker.card.Card;
import com.example.marvelsnaptracker.card.CardManager;
import com.example.marvelsnaptracker.deck.Deck;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * db card table CURD 관련 서비스 제공
 * singleton
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CardService {
    @Getter
    private static final CardService instance = new CardService();

    /**
     * 카드 데이터들를 DB에 저장 함.
     * 이미 있는 경우 데이터를 업데이트 수행
     *
     * @param cards DB에 저장할 카드 데이터
     */
    public void insertOrUpdateCards(List<Card> cards) {
        String sql = """
        INSERT INTO card (name, cost, power)
        VALUES (?, ?, ?)
        ON CONFLICT(name) DO UPDATE SET
            cost = excluded.cost,
            power = excluded.power;
        """;

        Connection connection = DatabaseDriver.getInstance().getConnection();

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
     * 이름에 해당하는 카드 데이터를 가져옴.
     *
     * @param name 카드 이름
     * @return Card
     */
    public Card selectCard(String name) {
        String sql = "SELECT * FROM card WHERE NAME = ?";
        Connection connection = DatabaseDriver.getInstance().getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);

            ResultSet rs = pstmt.executeQuery();

            // 선택 결과가 없는 경우 예외 발생
            if (!rs.next())
                throw new SQLException(String.format("No rows(%s) found!", name));

            String cardName = rs.getString("name");
            int cardCost = rs.getInt("cost");
            int cardPower = rs.getInt("power");

            return new Card(cardName, cardCost, cardPower);
        } catch (SQLException e) {
            System.out.println("CardService.selectCard : " + e.getMessage());
            System.out.println("card name : " + name);
            return new Card("None", 0, 0);
        }
    }

    /**
     * 덱에 속해 있는 모든 카드 반환
     *
     * @param deckID 탐색할 덱 ID
     * @return ArrayList<Card>
     */
    public ArrayList<Card> selectDeckAllCard(String deckID) {
        Connection connection = DatabaseDriver.getInstance().getConnection();

        String query = "SELECT name FROM deck_card WHERE owner = ?";

        CardManager cm = CardManager.getInstance();
        ArrayList<Card> cardList = new ArrayList<>();

        // 데이터 조회하기
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, deckID);

            ResultSet cards = pstmt.executeQuery();

            while (cards.next()) {
                cardList.add(cm.getCard(cards.getString("name")));
            }

        } catch (SQLException e) {
            System.out.println("CardService.selectDeckAllCard : " + e.getMessage());
            System.out.println("Deck ID : " + deckID);
        }

        return cardList;
    }
}
