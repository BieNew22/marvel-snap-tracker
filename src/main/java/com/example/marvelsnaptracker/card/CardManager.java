package com.example.marvelsnaptracker.card;


import com.example.marvelsnaptracker.utils.db.CardService;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;

/**
 * 모든 카드를 관리하는 객체, singleton pattern
 * 카드 객체는 modify 가 발생X -> flyweight pattern
 */
@NoArgsConstructor
public class CardManager {
    @Getter
    private static final CardManager instance = new CardManager();

    private HashMap<String, Card> totalData = new HashMap<>();

    /**
     * 이미 생성한 카드 객체 중에서 이름에 해당하는 객체를 반환
     * 이미 생성하지 않았다면 생성하여 저장 후 반환
     *
     * @param name 받아갈 카드 이름
     * @return Card
     */
    public Card getCard(String name) {
        Card res = totalData.getOrDefault(name, null);

        if (res == null) {
            res = CardService.getInstance().selectCard(name);

            totalData.put(res.getName(), res);
        }

        return res;
    }
}
