package com.github.martinfrank.bibergang;

import org.junit.Assert;
import org.junit.Test;

public class BibergangBoardTest {

    @Test
    public void endGameTest(){
        BibergangGame bibergangGame = new BibergangGame();
        BibergangBoard board = bibergangGame.getBoard();

        BibergangPlayer playerYou = board.getCurrentPlayer();
        for(int i = 1; i < 6; i ++){
            BibergangCard card = BibergangCard.newValueCard(i);
            card.reveal();
            playerYou.setCardById(card, BibergangCard.mapIndex(i));
        }
        board.getPrinter().printGame(System.out, board);
        Assert.assertFalse(playerYou.hasKnocked());
        Assert.assertFalse(board.haveAllFinishLastTurn());

        board.endPlayersTurn();//now is cpu
        BibergangPlayer playerCpu = board.getCurrentPlayer();
        Assert.assertFalse(playerCpu.hasKnocked());
        board.endPlayersTurn();//now is you again

        int lastClosedCardIndex = 6;
        BibergangCard card = BibergangCard.newValueCard(lastClosedCardIndex);
        card.reveal();
        playerYou.setCardById(card, BibergangCard.mapIndex(lastClosedCardIndex));
        Assert.assertTrue(playerYou.hasKnocked());
        Assert.assertFalse(board.haveAllFinishLastTurn());

        board.endPlayersTurn();//now is cpu
        Assert.assertTrue(playerYou.hasKnocked());
        Assert.assertFalse(playerCpu.hasKnocked());
        Assert.assertFalse(board.haveAllFinishLastTurn());

        //last player finishes turn
        board.endPlayersTurn();//now is cpu
        Assert.assertTrue(playerYou.hasKnocked());
        Assert.assertTrue(playerCpu.hasKnocked());
        Assert.assertTrue(board.haveAllFinishLastTurn());
    }
}
