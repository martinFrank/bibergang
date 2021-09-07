package com.github.martinfrank.bibergang;

import com.github.martinfrank.bibergang.ai.DrawFromStackDecision;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.github.martinfrank.bibergang.TestDataProvider.card;
import static com.github.martinfrank.bibergang.TestDataProvider.biber;

public class PlayerAiDrawTest {

    private static final TestDataProvider testDataProvider = new TestDataProvider();

    @Test
    public void testWithOneHighInHand() {
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

        //openstack provides a high value card without a pair-option --> draw from closed
        testDataProvider.bibergangBoard.getOpenStack().addOnTop(card(11, true));
        Assert.assertEquals(DrawFromStackDecision.FROM_CLOSED_STACK, testDataProvider.player.decideDrawStack());

        //openstack provides a card, that could exchange players high value card --> draw from open
        testDataProvider.bibergangBoard.getOpenStack().addOnTop(card(5, true));
        Assert.assertEquals(DrawFromStackDecision.FROM_OPEN_STACK, testDataProvider.player.decideDrawStack());

        //openstack provides a high value card with a pair-option --> draw from open
        testDataProvider.bibergangBoard.getOpenStack().addOnTop(card(12, true));
        Assert.assertEquals(DrawFromStackDecision.FROM_OPEN_STACK, testDataProvider.player.decideDrawStack());

        //openstack provides a biber --> draw from open
        testDataProvider.bibergangBoard.getOpenStack().addOnTop(biber(true));
        Assert.assertEquals(DrawFromStackDecision.FROM_OPEN_STACK, testDataProvider.player.decideDrawStack());
    }
    @Test
    public void testWithPairInHand() {
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

        //openstack provides a high value card without a pair-option --> draw from closed
        testDataProvider.bibergangBoard.getOpenStack().addOnTop(card(12, true));
        Assert.assertEquals(DrawFromStackDecision.FROM_CLOSED_STACK, testDataProvider.player.decideDrawStack());

        //openstack provides a low card, that could exchange players high value card --> draw from closed, because high are in pair
        testDataProvider.bibergangBoard.getOpenStack().addOnTop(card(5, true));
        Assert.assertEquals(DrawFromStackDecision.FROM_CLOSED_STACK, testDataProvider.player.decideDrawStack());
    }

    @Test
    public void testWithLowValueCardsButUnrevealed() {
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

        //openstack provides a low value card without a pair-option --> draw from open
        testDataProvider.bibergangBoard.getOpenStack().addOnTop(card(3, true));
        Assert.assertEquals(DrawFromStackDecision.FROM_OPEN_STACK, testDataProvider.player.decideDrawStack());

    }

    @Test
    public void testWithBiberPair() {
        List<BibergangCard> handCards = Arrays.asList(
                card(1, true),
                card(2, false),
                card(9, true),
                card(9, true),
                card(1, false),
                card(11, true),
                biber(true),
                card(12, true)
        );
        testDataProvider.setPlayerCards(handCards);

        //openstack provides a high value card without a pair-option --> draw from closed
        testDataProvider.bibergangBoard.getOpenStack().addOnTop(card(9, true));
        Assert.assertEquals(DrawFromStackDecision.FROM_CLOSED_STACK, testDataProvider.player.decideDrawStack());

        //openstack provides a high value card without a pair-option --> draw from closed
        testDataProvider.bibergangBoard.getOpenStack().addOnTop(card(10, true));
        Assert.assertEquals(DrawFromStackDecision.FROM_CLOSED_STACK, testDataProvider.player.decideDrawStack());

        //openstack provides a high value card with a pair-option --> draw from open
        testDataProvider.bibergangBoard.getOpenStack().addOnTop(card(11, true));
        Assert.assertEquals(DrawFromStackDecision.FROM_OPEN_STACK, testDataProvider.player.decideDrawStack());

        //openstack provides a high value card with a biber pair-option --> draw from open
        testDataProvider.bibergangBoard.getOpenStack().addOnTop(card(12, true));
        Assert.assertEquals(DrawFromStackDecision.FROM_OPEN_STACK, testDataProvider.player.decideDrawStack());

    }
    @Test
    public void testWithSingleBiber() {
        List<BibergangCard> handCards = Arrays.asList(
                card(1, true),
                card(2, false),
                card(9, true),
                card(9, true),
                card(1, false),
                card(11, true),
                biber(true),
                card(12, false)
        );
        testDataProvider.setPlayerCards(handCards);

        //openstack provides a high value card with a biber pair-option --> draw from open
        testDataProvider.bibergangBoard.getOpenStack().addOnTop(card(12, true));
        Assert.assertEquals(DrawFromStackDecision.FROM_CLOSED_STACK, testDataProvider.player.decideDrawStack());
    }


}
