package com.example.marvelsnaptracker.decks;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * 사용자 덱 객체
 */
@Getter
public class Deck {
    // 덱 고유 ID
    private final String id;

    // 덱 이름
    private final String name;

    // 덱 승률
    private double winRate;

    // 덱 플레이 횟수
    private int playCount;

    // 덱 최종 플레이한 일자
    private String lastPlay;

    // 덱 카드 리스트 (카드 이름)
    private final ArrayList<String> cardList;

    public Deck(JsonNode data) {
        // JsonNode 에서 파시앟여 저장.
        id = data.get("Id").asText();
        name = data.get("Name").asText();

        cardList = new ArrayList<>();
        for (JsonNode card: data.get("Cards")) {
            cardList.add(card.get("CardDefId").asText());
        }
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
