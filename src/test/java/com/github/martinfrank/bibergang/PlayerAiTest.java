package com.github.martinfrank.bibergang;

import com.github.martinfrank.bibergang.ai.ExchangeCardDecision;
import org.junit.Assert;
import org.junit.Test;

public class PlayerAiTest {

    @Test
    public void testDecideDrawStack(){
        BibergangPlayer player = createTestAiPlayer();
        player.revealAll();

        BibergangBoard bibergangBoard = new BibergangBoard(null);
        player.setGame(bibergangBoard);

        BibergangCard badCard = BibergangCard.newValueCard(12);
        badCard.reveal();
        bibergangBoard.getOpenStack().addOnTop(badCard);
        System.out.println("top card on open stack is : "+bibergangBoard.getOpenStack().getTopCard());
        Assert.assertEquals(ExchangeCardDecision.FROM_CLOSED_STACK, player.decideDrawCard());

        //player cards are from 1..8 - we exchange one with a 12
        String index = BibergangCard.mapIndex(BibergangGame.AMOUNT_CARD_COLUMNS*2-1);
        player.setCardById(badCard, index);

        BibergangCard mediumCard = BibergangCard.newValueCard(5); //12-5==7 -> should match
        mediumCard.reveal();
        bibergangBoard.getOpenStack().addOnTop(mediumCard);
        System.out.println("top card stack is : "+bibergangBoard.getOpenStack().getTopCard());
        Assert.assertEquals(ExchangeCardDecision.FROM_OPEN_STACK, player.decideDrawCard());


        //we set the bad card back to a better one:
        player.setCardById(mediumCard, index);
        BibergangCard goodCard = BibergangCard.newValueCard(4);
        goodCard.reveal();
        bibergangBoard.getOpenStack().addOnTop(goodCard);
        System.out.println("top card stack is : "+bibergangBoard.getOpenStack().getTopCard());
        Assert.assertEquals(ExchangeCardDecision.FROM_OPEN_STACK, player.decideDrawCard());

        BibergangCard biberCard = BibergangCard.newBiberCard();
        biberCard.reveal();
        bibergangBoard.getOpenStack().addOnTop(biberCard);
        System.out.println("top card stack is : "+bibergangBoard.getOpenStack().getTopCard());
        Assert.assertEquals(ExchangeCardDecision.FROM_OPEN_STACK, player.decideDrawCard());
    }

    @Test
    public void testDecideDrawStackPair(){
        BibergangPlayer player = createTestAiPlayer();
        player.revealAll();

        BibergangBoard bibergangBoard = new BibergangBoard(null);
        player.setGame(bibergangBoard);

        BibergangCard pairCard = BibergangCard.newValueCard(9);
        pairCard.reveal();
        bibergangBoard.getOpenStack().addOnTop(pairCard);
        System.out.println("top card on open stack is : "+bibergangBoard.getOpenStack().getTopCard());
        Assert.assertEquals(ExchangeCardDecision.FROM_OPEN_STACK, player.decideDrawCard());


        BibergangCard anotherPairCard = BibergangCard.newValueCard(2);
        anotherPairCard.reveal();
        bibergangBoard.getOpenStack().addOnTop(anotherPairCard);
        System.out.println("top card on open stack is : "+bibergangBoard.getOpenStack().getTopCard());
        Assert.assertEquals(ExchangeCardDecision.FROM_OPEN_STACK, player.decideDrawCard());

    }

    public BibergangPlayer createTestAiPlayer(){
        BibergangPlayer player = new BibergangPlayer("test", 0, false);
        for(int i = 0; i < BibergangGame.AMOUNT_CARD_COLUMNS; i ++){
            player.addStartCard(BibergangCard.newValueCard(9));
            player.addStartCard(BibergangCard.newValueCard(8));
        }
        return player;
    }
}
