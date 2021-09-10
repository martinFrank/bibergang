package com.github.martinfrank.bibergang.ai;

import com.github.martinfrank.bibergang.BibergangCard;

import java.util.Objects;

public class ExchangeCardOption implements Comparable<ExchangeCardOption> {

    public final String exchangingId;
    public final int diff;
    public final BibergangCard card; //cardInPlace

    public ExchangeCardOption(String exchangingId, int diff, BibergangCard card) {
        this.exchangingId = exchangingId;
        this.diff = diff;
        this.card = card;
    }

    @Override
    public String toString() {
        return "ExchangeCardOption{" +
                "exchangingId='" + exchangingId + '\'' +
                ", diff=" + diff +
                ", card=" + card +
                '}';
    }

    @Override
    public int compareTo(ExchangeCardOption o) {
        return -1 * Integer.compare(diff, o.diff);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExchangeCardOption option = (ExchangeCardOption) o;
        return diff == option.diff && Objects.equals(exchangingId, option.exchangingId) && Objects.equals(card, option.card);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exchangingId, diff, card);
    }
}
