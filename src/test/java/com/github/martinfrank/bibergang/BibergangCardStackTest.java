package com.github.martinfrank.bibergang;

import org.junit.Assert;
import org.junit.Test;

public class BibergangCardStackTest {

    @Test
    public void getTopCardTest(){
        BibergangCardStack cardDeck = new BibergangCardStack();
        BibergangCard first = BibergangCard.newValueCard(1);
        BibergangCard second = BibergangCard.newValueCard(2);
        cardDeck.addOnTop(first);
        cardDeck.addOnTop(second);
        Assert.assertEquals(second, cardDeck.getTopCard());
    }

    @Test
    public void drawTopCardTest(){
        BibergangCardStack cardDeck = new BibergangCardStack();
        BibergangCard first = BibergangCard.newValueCard(1);
        BibergangCard second = BibergangCard.newValueCard(2);
        cardDeck.addOnTop(first);
        cardDeck.addOnTop(second);
        int size = cardDeck.size();
        BibergangCard drawn = cardDeck.drawTopCard();
        Assert.assertEquals(second, drawn);
        Assert.assertEquals(size-1, cardDeck.size());
    }

}
