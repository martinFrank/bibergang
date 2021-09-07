package com.github.martinfrank.bibergang;

import com.github.martinfrank.boardgamelib.BoardGameSetup;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BibergangBoardTest {

    @Test
    public void endGameTest(){
        BibergangGame bibergangGame = new BibergangGame();
        BibergangBoard board = bibergangGame.getBoard();
        board.setup(getSetup());
        System.out.println("players: "+board.getPlayers());

        BibergangPlayer playerYou = board.getCurrentPlayer();
        for(int i = 1; i < 6; i ++){
            BibergangCard card = BibergangCard.newValueCard(i);
            card.reveal();
            playerYou.setCardById(card, BibergangCard.mapIndex(i));
        }
        board.getPrinter().printGame(board);
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

    public BoardGameSetup<BibergangPlayer> getSetup(){
        return new BoardGameSetup<BibergangPlayer>(){

            @Override
            public List<BibergangPlayer> getPlayers() {
                ArrayList<BibergangPlayer> player = new ArrayList<>();
                player.add(new BibergangPlayer("P_1", 0xFFFF00, true));
                player.add(new BibergangPlayer("P_2", 0x0000FF, true));
                return player;
            }

            @Override
            public int getMaximumRounds() {
                return 0;
            }
        };

    }
}
