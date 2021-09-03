package com.github.martinfrank.bibergang;

import com.github.martinfrank.bibergang.ai.ExchangeCardDecision;
import com.github.martinfrank.bibergang.ai.ExchangeCardOption;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class CardExchangeOptionTest {

    @Test
    public void testExchangeCardOption(){
        BibergangPlayer player = createTestAiPlayer();
        player.revealAll();

        BibergangBoard bibergangBoard = new BibergangBoard(null);
        player.setGame(bibergangBoard);

        BibergangCard goodOne = BibergangCard.newValueCard(0);

        List<ExchangeCardOption> exchangeCardOptions = player.getCardExchangeOptions(goodOne);
        Collections.sort(exchangeCardOptions);
        Assert.assertEquals(12, exchangeCardOptions.get(0).diff);
    }


    public BibergangPlayer createTestAiPlayer(){
        BibergangPlayer player = new BibergangPlayer("test", 0, false);
        for(int i = 0; i < BibergangGame.AMOUNT_CARD_COLUMNS; i ++){
            player.addStartCard(BibergangCard.newValueCard(5+2*i));
            player.addStartCard(BibergangCard.newValueCard(6+2*i));
        }
        return player;
    }

}
