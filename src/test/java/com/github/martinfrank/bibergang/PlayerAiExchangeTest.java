package com.github.martinfrank.bibergang;

import com.github.martinfrank.bibergang.ai.ExchangeOrTossCardDecsion;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.github.martinfrank.bibergang.TestDataProvider.card;
import static com.github.martinfrank.bibergang.TestDataProvider.biber;

public class PlayerAiExchangeTest {

    private static final TestDataProvider testDataProvider = new TestDataProvider();

    @Test
    public void testMakePairWithUnrevealed() {
        List<BibergangCard> handCards = Arrays.asList(
                card(1, true),
                card(2, false),
                card(3, false),
                card(4, false),
                card(5, false),
                card(6, false),
                card(7, false),
                card(12, true)
        );
        testDataProvider.setPlayerCards(handCards);

        testDataProvider.drawCard(card(12, true));
        ExchangeOrTossCardDecsion decision = testDataProvider.player.decideExchangeOrTossCard();
        Assert.assertEquals(ExchangeOrTossCardDecsion.DecisionType.EXCHANGE, decision.type);
        Assert.assertEquals(BibergangCard.mapIndex(6), decision.getExchangeId());
    }

    @Test
    public void testMakePairWithRevealed() {
        List<BibergangCard> handCards = Arrays.asList(
                card(1, true),
                card(2, false),
                card(3, false),
                card(4, false),
                card(5, false),
                card(6, false),
                card(11, true),
                card(12, true)
        );
        testDataProvider.setPlayerCards(handCards);

        testDataProvider.drawCard(card(12,true));
        ExchangeOrTossCardDecsion decisionOnTop = testDataProvider.player.decideExchangeOrTossCard();
        Assert.assertEquals(ExchangeOrTossCardDecsion.DecisionType.EXCHANGE, decisionOnTop.type);
        Assert.assertEquals(BibergangCard.mapIndex(6), decisionOnTop.getExchangeId());

        testDataProvider.drawCard(card(11,true));
        ExchangeOrTossCardDecsion decisionOnBottom = testDataProvider.player.decideExchangeOrTossCard();
        Assert.assertEquals(ExchangeOrTossCardDecsion.DecisionType.EXCHANGE, decisionOnBottom.type);
        Assert.assertEquals(BibergangCard.mapIndex(7), decisionOnBottom.getExchangeId());
    }

    @Test
    public void testAvoidExistingPair() {
        List<BibergangCard> handCards = Arrays.asList(
                card(1, true),
                card(2, false),
                card(3, false),
                card(4, false),
                card(5, false),
                card(6, false),
                card(12, true),
                card(12, true)
        );
        testDataProvider.setPlayerCards(handCards);

        testDataProvider.drawCard(card(12,true));
        ExchangeOrTossCardDecsion decision = testDataProvider.player.decideExchangeOrTossCard();
        Assert.assertEquals(ExchangeOrTossCardDecsion.DecisionType.TOSS, decision.type);
        List<String> indexes = Arrays.asList("C", "D", "E", "F");
        Assert.assertTrue(indexes.contains( decision.getExchangeId()));
    }

    @Test
    public void testReplaceHighValueCard() {
        List<BibergangCard> handCards = Arrays.asList(
                card(1, true),
                card(2, false),
                card(3, false),
                card(4, false),
                card(5, false),
                card(6, false),
                card(7, false),
                card(12, true)
        );
        testDataProvider.setPlayerCards(handCards);

        testDataProvider.drawCard(card(2,true));
        ExchangeOrTossCardDecsion decisionReplace = testDataProvider.player.decideExchangeOrTossCard();
        Assert.assertEquals(ExchangeOrTossCardDecsion.DecisionType.EXCHANGE, decisionReplace.type);
        Assert.assertEquals(BibergangCard.mapIndex(7), decisionReplace.getExchangeId());

        //trotztdem würde ich lieber ein paar bilden als eine hohe zu ersetzen
        testDataProvider.drawCard(card(1,true));
        ExchangeOrTossCardDecsion decisionBuildPair = testDataProvider.player.decideExchangeOrTossCard();
        Assert.assertEquals(ExchangeOrTossCardDecsion.DecisionType.EXCHANGE, decisionBuildPair.type);
        Assert.assertEquals(BibergangCard.mapIndex(1), decisionBuildPair.getExchangeId());
    }

    @Test
    public void testTossHighCard() {
        List<BibergangCard> handCards = Arrays.asList(
                card(1, true),
                card(2, false),
                card(1, false),
                card(2, false),
                card(1, false),
                card(2, false),
                card(1, false),
                card(2, true)
        );
        testDataProvider.setPlayerCards(handCards);

        testDataProvider.drawCard(card(5,true));
        ExchangeOrTossCardDecsion decisionToss = testDataProvider.player.decideExchangeOrTossCard();
        Assert.assertEquals(ExchangeOrTossCardDecsion.DecisionType.TOSS, decisionToss.type);
        List<String> indexes = Arrays.asList("C", "D", "E", "F");
        Assert.assertTrue(indexes.contains( decisionToss.getExchangeId()));
    }

    @Test
    public void testBiberOnLowCards() {
        List<BibergangCard> handCards = Arrays.asList(
                card(1, true),
                card(2, true),
                biber(true),
                card(2, false),
                card(1, false),
                card(2, false),
                card(1, true),
                card(2, true)
        );
        testDataProvider.setPlayerCards(handCards);

        testDataProvider.drawCard(biber(true));
        ExchangeOrTossCardDecsion decisionExchangeOnUnknown = testDataProvider.player.decideExchangeOrTossCard();
        Assert.assertEquals(ExchangeOrTossCardDecsion.DecisionType.EXCHANGE, decisionExchangeOnUnknown.type);
        List<String> indexes = Arrays.asList("E", "F");
        Assert.assertTrue(indexes.contains( decisionExchangeOnUnknown.getExchangeId()));
    }

    @Test
    public void testBiberOnHighCards() {
        List<BibergangCard> handCards = Arrays.asList(
                card(1, true),
                card(2, true),
                biber(true),
                card(2, false),
                card(1, false),
                card(2, false),
                card(1, true),
                card(12, true)
        );
        testDataProvider.setPlayerCards(handCards);

        testDataProvider.drawCard(biber(true));
        ExchangeOrTossCardDecsion decisionExchangeOnUnknown = testDataProvider.player.decideExchangeOrTossCard();
        Assert.assertEquals(ExchangeOrTossCardDecsion.DecisionType.EXCHANGE, decisionExchangeOnUnknown.type);
        Assert.assertEquals(BibergangCard.mapIndex(6), decisionExchangeOnUnknown.getExchangeId());
    }


}
