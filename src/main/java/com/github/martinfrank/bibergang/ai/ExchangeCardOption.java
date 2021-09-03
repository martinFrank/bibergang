package com.github.martinfrank.bibergang.ai;

import com.github.martinfrank.bibergang.BibergangCard;

public class ExchangeCardOption implements Comparable<ExchangeCardOption> {

    public final String cardSlotId;
    public final int diff;
    public final BibergangCard card;

    public ExchangeCardOption(String cardSlotId, int diff, BibergangCard card) {
        this.cardSlotId = cardSlotId;
        this.diff = diff;
        this.card = card;
    }

    @Override
    public String toString() {
        return "ExchangeCardOption{" +
                "cardSlotId='" + cardSlotId + '\'' +
                ", diff=" + diff +
                ", card=" + card +
                '}';
    }

    @Override
    public int compareTo(ExchangeCardOption o) {
        return -1 * Integer.compare(diff, o.diff);
    }
}
