package com.example.marvelsnaptracker.decks;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 카드 객체
 */
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // card 클래스에 매핑되지 않는 필드는 무시
public class Card {
    @JsonProperty("defId")
    private String name;

    @JsonProperty("cost")
    private int cost;

    @JsonProperty("power")
    private int power;

    @Override
    public String toString() {
        return "Card{" +
                "name='" + name + '\'' +
                ", cost=" + cost +
                ", power=" + power +
                '}';
    }
}
