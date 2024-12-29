package com.example.marvelsnaptracker.decks;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;

/**
 * 사용자 덱 객체
 */
public class Deck {
    // 덱 고유 ID
    String id;

    // 덱 이름
    String name;

    // 덱 카드 리스트 (카드 이름)
    ArrayList<String> cardList;

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
