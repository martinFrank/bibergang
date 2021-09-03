package com.github.martinfrank.bibergang.ai;

import com.github.martinfrank.bibergang.BibergangCard;
import com.github.martinfrank.bibergang.BibergangCardColumn;

public class PairCardColumn {

    public final BibergangCardColumn column;
    public final BibergangCard pairingCard;
    public final BibergangCard exchangingCard;
    public final String exchangingId;

    public PairCardColumn(BibergangCardColumn column, BibergangCard pairingCard, BibergangCard exchangingCard, String exchangingId) {
        this.column = column;
        this.pairingCard = pairingCard;
        this.exchangingCard = exchangingCard;
        this.exchangingId = exchangingId;
    }
}
