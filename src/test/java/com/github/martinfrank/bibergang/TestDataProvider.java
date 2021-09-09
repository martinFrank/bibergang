package com.github.martinfrank.bibergang;

import java.util.ArrayList;
import java.util.List;

public class TestDataProvider {

    public final BibergangPlayer player;
    public final BibergangBoard bibergangBoard;

    public TestDataProvider() {
        player = new BibergangPlayer("test", 0, false);
//        player.revealAll();

        bibergangBoard = new BibergangBoard(null);
        bibergangBoard.setup(getSetup());
//        player.setGame(bibergangBoard);

    }

    public void setPlayerCards(List<BibergangCard> handCards) {
        if (handCards.size() != BibergangGame.AMOUNT_CARD_COLUMNS * 2) {
            throw new IllegalArgumentException("handCards size must be the same as 2 * columns");
        }
        for (int i = 0; i < handCards.size(); i++) {
            player.setCardById(handCards.get(i), BibergangCard.mapIndex(i));
        }
    }

    public void drawCard(BibergangCard card) {
        bibergangBoard.getOpenStack().addOnTop(card);
        bibergangBoard.drawCurrentCardFromOpen();
        //player holds now open card in hand
    }


//    public BibergangPlayer createTestAiPlayer() {
//        BibergangPlayer player = new BibergangPlayer("test", 0, false);
////        for (int i = 0; i < BibergangGame.AMOUNT_CARD_COLUMNS; i++) {
////            player.addStartCard(BibergangCard.newValueCard(9));
////            player.addStartCard(BibergangCard.newValueCard(8));
////        }
//        return player;
//    }

    public static BibergangCard card(int cardValue, boolean isRevealed) {
        BibergangCard card = BibergangCard.newValueCard(cardValue);
        if (isRevealed) {
            card.reveal();
        }
        return card;
    }
    public static BibergangCard biber(boolean isRevealed) {
        BibergangCard card = BibergangCard.newBiberCard();
        if (isRevealed) {
            card.reveal();
        }
        return card;
    }


    public void printHandCards() {
        for (int i = 0; i < BibergangGame.AMOUNT_CARD_COLUMNS * 2; i++) {
            String id = BibergangCard.mapIndex(i);
            System.out.println("hand card at " + id + " = " + player.getCardById(id));
        }
    }

    public void printOpenStack() {
        System.out.println("Open card: " + bibergangBoard.getOpenStack().getTopCard());
    }

    public BibergangGameSetup getSetup() {
        return new BibergangGameSetup() {

            @Override
            public List<BibergangPlayer> getPlayers() {
                ArrayList<BibergangPlayer> players = new ArrayList<>();
                players.add(player);
                return players;
            }

//            @Override
//            public int getMaximumRounds() {
//                return 0;
//            }
        };

    }

}
