package com.example.marvelsnaptracker.card;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 카드 객체
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // card 클래스에 매핑되지 않는 필드는 무시
public class Card implements Comparable<Card> {
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

    /**
     * 기준1 : cost에 대하여 오름차순
     * 기준2 : power에 대하여 오름차순
     *
     * @param o the object to be compared.
     * @return result
     */
    @Override
    public int compareTo(Card o) {
        if (this.cost == o.getCost())
            return Integer.compare(this.power, o.getPower());
        return Integer.compare(this.cost, o.getCost());
    }
}
