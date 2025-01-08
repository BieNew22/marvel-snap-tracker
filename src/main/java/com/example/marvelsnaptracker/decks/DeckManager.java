package com.example.marvelsnaptracker.decks;

import com.example.marvelsnaptracker.utils.EnvManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 사용자 덱을 관리하는 클래스, Singleton
 */
public class DeckManager {
    private static final DeckManager instance = new DeckManager();

    private DeckManager() {}

    public static DeckManager getInstance() { return instance; }

    ArrayList<Deck> deckList;

    /**
     * 사용자 덱 정보를 초기화 함.
     * - 2024.12.29 기준 : 로컬에서만 가져옴.
     */
    public void initDeck() {

        if (deckList != null)
            return;

        deckList = new ArrayList<>();

        // root -> ServerState -> Decks : 사용자 덱 정보
        String fileName = "CollectionState.json";

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String totalPath = EnvManager.getInstance().BASE_DIR + fileName;

            JsonNode rootNode = objectMapper.readTree(new File(totalPath));

            JsonNode decks = rootNode.get("ServerState").get("Decks");

            // 사용자 덱 리스트 추가
            for (JsonNode deck: decks) {
                deckList.add(new Deck(deck));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 사용자의 모든 덱 정보 리턴
     * @return iterator<Deck>
     */
    public Iterator<Deck> getAllDeckInfo() {
        return deckList.iterator();
    }

    /**
     * 현재 사용자의 덱 리스트를 콘솔에 출력하는 함수.
     */
    public void showDeckList() {
        for (Deck deck: deckList)
            System.out.println(deck);
    }
}
