package com.github.martinfrank.bibergang;

import com.github.martinfrank.bibergang.ai.ExchangeOrTossCardDecsion;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.github.martinfrank.bibergang.TestDataProvider.biber;
import static com.github.martinfrank.bibergang.TestDataProvider.card;

public class PlayerAiFinalExchangeTest {

    private static final TestDataProvider testDataProvider = new TestDataProvider();

    @Test
    public void testMakePairWithUnrevealed() {
        List<BibergangCard> handCards = Arrays.asList(
                card(1, true),
                card(1, true),
                card(3, true),
                card(3, true),
                card(5, true),
                card(5, true),
                card(7, false),
                card(12, true)
        );
        testDataProvider.setPlayerCards(handCards);

        testDataProvider.drawCard(card(1, true));
        ExchangeOrTossCardDecsion decisionReplace = testDataProvider.player.decideExchangeOrTossCard();
        Assert.assertEquals(ExchangeOrTossCardDecsion.DecisionType.EXCHANGE, decisionReplace.type);
        Assert.assertEquals(BibergangCard.mapIndex(7), decisionReplace.getExchangeId());

        testDataProvider.drawCard(card(12, true));
        ExchangeOrTossCardDecsion decisionPairFinish = testDataProvider.player.decideExchangeOrTossCard();
        Assert.assertEquals(ExchangeOrTossCardDecsion.DecisionType.EXCHANGE, decisionPairFinish.type);
        Assert.assertEquals(BibergangCard.mapIndex(6), decisionPairFinish.getExchangeId());
    }

    @Test
    public void testTossBecauseScore() {
        List<BibergangCard> handCards = Arrays.asList(
                card(1, true),
                card(2, true),
                card(3, true),
                card(4, true),
                card(5, true),
                card(6, true),
                card(7, false),
                card(12, true)
        );
        testDataProvider.setPlayerCards(handCards);

        testDataProvider.drawCard(card(12, true));
        ExchangeOrTossCardDecsion decisionPairFinish = testDataProvider.player.decideExchangeOrTossCard();
        Assert.assertEquals(ExchangeOrTossCardDecsion.DecisionType.TOSS, decisionPairFinish.type);
        Assert.assertNull(decisionPairFinish.getExchangeId());
    }

    @Test
    public void testBiberPairExchange () {
        List<BibergangCard> handCards = Arrays.asList(
                card(1, true),
                card(1, true),
                biber(true),
                card(2, true),
                card(8, false),
                card(1, true),
                biber(true),
                card(12, true)
        );
        testDataProvider.setPlayerCards(handCards);

        testDataProvider.drawCard(card(0,true));
        ExchangeOrTossCardDecsion decisionExchangeOnUnknown = testDataProvider.player.decideExchangeOrTossCard();
        Assert.assertEquals(ExchangeOrTossCardDecsion.DecisionType.EXCHANGE, decisionExchangeOnUnknown.type);
        Assert.assertEquals(BibergangCard.mapIndex(4), decisionExchangeOnUnknown.getExchangeId());
    }

}
