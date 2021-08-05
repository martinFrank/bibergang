package com.github.martinfrank.bibergang;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BibergangCardStackTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BibergangCardStackTest.class);

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
    public void allCardTest(){
        BibergangCardStack cardDeck = new BibergangCardStack();
        cardDeck.newBiberCardDeck();
        cardDeck.getCards().forEach(c -> LOGGER.debug("Card: {}",c));
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
