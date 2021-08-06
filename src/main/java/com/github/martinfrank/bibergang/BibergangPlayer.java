package com.github.martinfrank.bibergang;

import com.github.martinfrank.boardgamelib.BasePlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BibergangPlayer extends BasePlayer<BibergangBoard> {

    private static final Logger LOG = LoggerFactory.getLogger(BasePlayer.class);

    private final BibergangCardColumn[] columns;
    private boolean hasDrawn = false;
    private boolean lastTurnPlayed = false;

    BibergangPlayer(String name, int color, boolean isHuman) {
        super(name, color, isHuman);
        columns = new BibergangCardColumn[BibergangGame.AMOUNT_CARD_COLUMNS];
        IntStream.range(0,BibergangGame.AMOUNT_CARD_COLUMNS).forEach(i -> columns[i] = new BibergangCardColumn(i));
    }



    @Override
    public void performAiTurn() {
        LOG.debug("{} is working on its bibergang move", getName());
        BibergangBoard board = getBoardGame();
//        board.startPlayersTurn();

        //logic here

        board.endPlayersTurn();
    }

    public void addStartCard(BibergangCard card) {
        for(BibergangCardColumn column: columns){
            if(column.topCard == null){
                column.topCard = card;
                return;
            }
            if(column.bottomCard == null){
                column.bottomCard = card;
                return;
            }
        }
        throw new IllegalArgumentException("card columns are full");
    }

    public BibergangCard getCardById(String id) {
        for(BibergangCardColumn column: columns){
            BibergangCard card = column.getCard(id);
            if (card != null){
                return card;
            }
        }
        return null;
    }

    public void revealStartCards() {
        columns[0].topCard.reveal();
        columns[BibergangGame.AMOUNT_CARD_COLUMNS-1].bottomCard.reveal();
    }

    public void resetDrawn() {
        this.hasDrawn = false;
    }
    public boolean hasDrawn(){
        return hasDrawn;
    }

    public void setDrawn() {
        hasDrawn = true;
    }

    //FIXME better name
    public boolean hasOptionFour() {
        return getAmountRevealedCards() == BibergangGame.AMOUNT_CARD_COLUMNS*2-1;
    }

    public void setCardById(BibergangCard card, String id) {
        for (BibergangCardColumn column: columns){
            BibergangCard candidate = column.getCard(id);
            if(candidate != null){
                column.setCard(card, id);
            }
        }
    }

    public boolean hasKnocked() {
        return getAmountRevealedCards() == 2 * BibergangGame.AMOUNT_CARD_COLUMNS;
    }

    private int getAmountRevealedCards(){
        return (int)columnCardStream().filter(BibergangCard::isRevealed).count();
    }

    public BibergangCard moveBiber(BibergangCard biber) {
        for(BibergangCardColumn column: columns){
            if(!column.topCard.isRevealed()){
                BibergangCard card = column.topCard;
                column.topCard = biber;
                return card;
            }
            if(!column.bottomCard.isRevealed()){
                BibergangCard card = column.bottomCard;
                column.bottomCard = biber;
                return card;
            }
        }
        return null;
    }

    public void setLastTurnPlayed() {
        lastTurnPlayed = true;
    }

    public  boolean hasLastTurnPlayed() {
        return lastTurnPlayed;
    }

    public void revealAll() {
        columnCardStream().forEach(BibergangCard::reveal);
    }

    public Stream<BibergangCard> columnCardStream(){
        BibergangCard[] cards = new BibergangCard[BibergangGame.AMOUNT_CARD_COLUMNS*2];
        for (int i = 0; i < BibergangGame.AMOUNT_CARD_COLUMNS;i++){
            BibergangCardColumn column = columns[i];
            cards[2*i] = column.topCard;
            cards[2*i+1] = column.bottomCard;
        }
        return Arrays.stream(cards);
    }

    public int getTotalScore(){
        int total = 0;
        for(BibergangCardColumn column: columns){
            if(column.isPair() ){
                total = total - 5;
            }else{
                total = total + column.topCard.getValue();
                total = total + column.bottomCard.getValue();
            }
        }
        return total;
    }

    public boolean hasHiddenCards() {
        return getAmountRevealedCards() > 0;
    }
}
