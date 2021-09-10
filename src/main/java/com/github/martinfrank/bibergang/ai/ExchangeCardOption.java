package com.github.martinfrank.bibergang.ai;

import com.github.martinfrank.bibergang.BibergangCard;

public class ExchangeCardOption implements Comparable<ExchangeCardOption> {

    public final String exchangingId; //FIXME rename to exchaningId
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
}
