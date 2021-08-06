package com.github.martinfrank.bibergang;

import org.junit.Assert;
import org.junit.Test;

public class BibergangPlayerTest {

    @Test
    public void testKnocked(){
        BibergangPlayer player = createTestPlayer();
        player.revealStartCards();
        Assert.assertFalse(player.hasKnocked());

        for(int i = 1; i < (BibergangGame.AMOUNT_CARD_COLUMNS-1)*2; i ++){
            BibergangCard card = BibergangCard.newValueCard(i);
            card.reveal();
            String id = BibergangCard.mapIndex(i);
            System.out.println("setting card at "+id);
            player.setCardById(card, id);
        }
        Assert.assertFalse(player.hasKnocked());

        int lastButOneIndex = (BibergangGame.AMOUNT_CARD_COLUMNS-1)*2;
        String lastButOneId = BibergangCard.mapIndex(lastButOneIndex);
        System.out.println("setting lastButOneId card at "+lastButOneId);
        BibergangCard lastButOneCard = BibergangCard.newValueCard(lastButOneIndex);
        lastButOneCard.reveal();
        player.setCardById(lastButOneCard, lastButOneId);

        Assert.assertTrue(player.hasKnocked());
    }

    @Test
    public void testToss(){
        BibergangPlayer player = createTestPlayer();
        player.revealStartCards();
        Assert.assertFalse(player.hasOptionFour());

        for(int i = 1; i < (BibergangGame.AMOUNT_CARD_COLUMNS-1)*2; i ++){
            BibergangCard card = BibergangCard.newValueCard(i);
            card.reveal();
            String id = BibergangCard.mapIndex(i);
            player.setCardById(card, id);
        }

        Assert.assertTrue(player.hasOptionFour());
    }

    public BibergangPlayer createTestPlayer(){
        BibergangPlayer player = new BibergangPlayer("test", 0, true);
        for(int i = 0; i < BibergangGame.AMOUNT_CARD_COLUMNS*2; i ++){
            BibergangCard card = BibergangCard.newValueCard(i);
            player.addStartCard(BibergangCard.newValueCard(i));
        }
        return player;
    }
}
