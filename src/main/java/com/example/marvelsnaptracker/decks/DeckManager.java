package com.example.marvelsnaptracker.decks;

import com.example.marvelsnaptracker.utils.DatabaseDriver;
import com.example.marvelsnaptracker.utils.EnvManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
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
     */
    public void initDeck() {

        // json 통하여 덱 정보를 조회 (현재 사용자가 보유하고 있는 덱)
        ArrayList<Deck> deckFromJson = initDeckJson();

        // DB 통하여 덱 정보를 조회
        ArrayList<Deck> deckFromDB = DatabaseDriver.getInstance().getAllDecks();

        // DB와 Json 동기화 시키기.

        // Json에 있는 덱 id 가져오기
        HashSet<String> jsonIDs = new HashSet<>();
        for (Deck d: deckFromJson)
            jsonIDs.add(d.getId());

        // DB에 있는 덱 id 가져오기
        HashSet<String> dbIDs = new HashSet<>();
        for (Deck d: deckFromDB)
            dbIDs.add(d.getId());

        // json -> DB로 동기화 진행
        for (Deck deck: deckFromJson) {
            if (dbIDs.contains(deck.getId())) {
                /* Json에 있고 DB에도 있는 경우
                 * 둘 데이터 동일 여부 확인
                 * 동일 O : continue
                 * 동일 X : 종류 (이름, 카드 내용)
                 *  -> 이름이 다른 경우 : 이름만 업데이트
                 *  -> 카드가 다른 경우 : 새로운 덱으로 판단, 기존 덱을 새로운 덱으로 저장 후 지금 덱을 새로 저장
                 */

            } else {
                // json에 있고 DB에 없는 경우 -> 새로운 덱을 생성
                DatabaseDriver.getInstance().insertDeck(deck);
            }
        }
        
        deckList = DatabaseDriver.getInstance().getAllDecks();
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
