package com.example.marvelsnaptracker.decks;

import com.example.marvelsnaptracker.utils.DatabaseDriver;
import com.example.marvelsnaptracker.utils.EnvManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 사용자 덱을 관리하는 클래스, Singleton
 */
public class DeckManager {
    @Getter
    private static final DeckManager instance = new DeckManager();

    private DeckManager() {}

    ArrayList<Deck> deckList = new ArrayList<>();

    /**
     * 사용자 덱 정보를 초기화 함.
     * - 2024.12.29 기준 : 로컬에서만 가져옴.
     */
    public void initDeck() {

        // json 통하여 덱 정보를 조회
        ArrayList<Deck> deckFromJson = initDeckJson();

        // DB 통하여 덱 정보를 조회
        ArrayList<Deck> deckFromDB = DatabaseDriver.getInstance().getAllDecks();

        for (Deck d: deckFromDB)
            System.out.println(d);
    }

    /**
     * 사용자 덱 정보 초기화 : CollectionState.json을 통하여 초기화.
     *
     * @return Deck List
     */
    private ArrayList<Deck> initDeckJson() {
        ArrayList<Deck> res = new ArrayList<>();

        // root -> ServerState -> Decks : 사용자 덱 정보
        String fileName = "CollectionState.json";

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String totalPath = EnvManager.getInstance().BASE_DIR + fileName;

            JsonNode rootNode = objectMapper.readTree(new File(totalPath));

            JsonNode decks = rootNode.get("ServerState").get("Decks");

            // 사용자 덱 리스트 추가
            for (JsonNode deck: decks) {
                res.add(new Deck(deck));
            }
        } catch (IOException e) {
            System.out.println("Can't find file or else");
            System.out.println("error message : " + e.getMessage());

            return new ArrayList<>();
        }

        return res;
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
