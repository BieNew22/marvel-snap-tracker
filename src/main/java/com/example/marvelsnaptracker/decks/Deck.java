package com.example.marvelsnaptracker.decks;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * 사용자 덱 객체
 */
@Getter
@Setter
public class Deck {
    // 덱 고유 ID
    private String id;

    // 덱 이름
    private String name;

    // 덱 승률
    private double winRate;

    // 덱 플레이 횟수
    private int playCount;

    // 덱 최종 플레이한 일자
    private String lastPlay = "0000-00-00 00:00";

    // 덱 카드 리스트 (카드 이름)
    private ArrayList<String> cardList;

    public Deck() {}

    public Deck(JsonNode data) {
        // JsonNode 에서 파시앟여 저장.
        id = data.get("Id").asText();
        name = data.get("Name").asText();

        cardList = new ArrayList<>();
        for (JsonNode card: data.get("Cards")) {
            cardList.add(card.get("CardDefId").asText());
        }
    }

    // compare 함수가 리턴할 상수들.
    public static int DECK_DIFF_NAME = 1;
    public static int DECK_DIFF_CARD = -1;
    public static int DECK_EQUAL = 0;

    /**
     * 다른 deck과 비교함.
     *
     * @param deck 비교할 덱
     * @return 0 : 동일한 경우, -1
     */
    public int compare(Deck deck) {

        // 카드에 변경 상황이 존재할 때
        if (!cardList.containsAll(deck.getCardList())) {
            return DECK_DIFF_CARD;
        }

        // 카드는 동일 제목이 같을 때
        if (deck.getName().equalsIgnoreCase(name)) {
            return DECK_EQUAL;
        }

        // 카드 제목이 다를 때
        return DECK_DIFF_NAME;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("======================\n");
        sb.append(name).append("(").append(id).append(")\n");
        for (String c: cardList)
            sb.append(c).append(" | ");
        sb.append("\n======================\n");

        return sb.toString();
    }
}
