package com.github.martinfrank.bibergang;

import com.github.martinfrank.bibergang.ai.DrawFromStackDecision;
import com.github.martinfrank.bibergang.ai.ExchangeCardOption;
import com.github.martinfrank.bibergang.ai.ExchangeOrTossCardDecsion;
import com.github.martinfrank.bibergang.ai.PairCardColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BibergangPlayer {

    private static final Logger LOGGER = LoggerFactory.getLogger(BibergangPlayer.class);

    private boolean hasDrawn = false;
    private boolean lastTurnPlayed = false;
    public final BibergangCardColumns cols;
    private BibergangBoard board;
    public final String name;
    public final int color;
    public final boolean isHuman;

    BibergangPlayer(String name, int color, boolean isHuman) {
        this.name = name;
        this.color = color;
        this.isHuman = isHuman;
        cols = new BibergangCardColumns();
    }

    public void performAiTurn() {
        LOGGER.debug("{} is working on its bibergang AI move - open card is {}", getName(), board.getOpenStack().getTopCard().getValue());
        DrawFromStackDecision exchangeCardOption = decideDrawStack();
        LOGGER.debug("{} draws from {}", getName(), exchangeCardOption);
        BibergangCard card = drawCard(exchangeCardOption);
        LOGGER.debug("{} now holds this card: {}", getName(), card);
        ExchangeOrTossCardDecsion decision = decideExchangeOrTossCard();
        exchangeOrToss(decision);
        board.endPlayersTurn();
    }

    public void exchangeOrToss(ExchangeOrTossCardDecsion decision) {
        if(decision.type == ExchangeOrTossCardDecsion.DecisionType.EXCHANGE){
            board.exchangeCard(decision.getExchangeId());
        }
        if(decision.type == ExchangeOrTossCardDecsion.DecisionType.TOSS){
            if(decision.getExchangeId() != null){
                cols.getCardById(decision.getExchangeId()).reveal();
            }
        }
        board.tossCard();
    }

    private BibergangCard drawCard(DrawFromStackDecision exchangeCardOption) {
        if (exchangeCardOption == DrawFromStackDecision.FROM_OPEN_STACK) {
            board.drawCurrentCardFromOpen();
        }
        if (exchangeCardOption == DrawFromStackDecision.FROM_CLOSED_STACK) {
            board.drawCurrentCardFromClosed();
        }
        return board.getCurrentDrawnCard();
    }

    public DrawFromStackDecision decideDrawStack() {
        BibergangCard openCard = board.getOpenStack().getTopCard();
        LOGGER.debug("Open Card is {} --> which stack shall i draw from?", openCard.getDisplay());
        if (openCard.isBiber()) {
            LOGGER.debug("Open Card is Biber --> i draw from open stack");
            return DrawFromStackDecision.FROM_OPEN_STACK;
        }

        Optional<PairCardColumn> biberPairColumn = cols.findBiberPairColumnFor(openCard);
        if (biberPairColumn.isPresent()) {
            LOGGER.debug("Open Card breaks up a Biber pair --> i draw from open stack");
            return DrawFromStackDecision.FROM_OPEN_STACK;
        }

        Optional<PairCardColumn> pairColumn = cols.findPairColumnFor(openCard);
        if (pairColumn.isPresent()) {
            LOGGER.debug("Open Card forms a pair --> i draw from open stack");
            return DrawFromStackDecision.FROM_OPEN_STACK;
        }

        List<ExchangeCardOption> exchangeCardOptions = cols.getCardExchangeOptions(openCard);
        if (exchangeCardOptions.stream().anyMatch(o -> o.diff >= 7)) {
            LOGGER.debug("Open Card can make a diff over 7 --> i draw from open stack");
            return DrawFromStackDecision.FROM_OPEN_STACK;
        }
        if (openCard.getValue() <= 4) {
            LOGGER.debug("Open Card looks too sweet (<=4) to resist --> i draw from open stack");
            return DrawFromStackDecision.FROM_OPEN_STACK;
        }
        LOGGER.debug("Open Card is not an option, i draw from closed");
        return DrawFromStackDecision.FROM_CLOSED_STACK;
    }

    public void addStartCard(BibergangCard card) {
        cols.addStartCard(card);
    }

    public BibergangCard getCardById(String id) {
        return cols.getCardById(id);
    }

    public void revealStartCards() {
        cols.revealStartCards();
    }

    public void resetDrawn() {
        this.hasDrawn = false;
    }

    public boolean hasDrawn() {
        return hasDrawn;
    }

    public void setDrawn() {
        hasDrawn = true;
    }

    //FIXME better name
    public boolean hasOptionFour() {
        return cols.getAmountRevealedCards() == BibergangGame.AMOUNT_CARD_COLUMNS * 2 - 1;
    }

    public void setCardById(BibergangCard card, String id) {
        cols.setCardById(card, id);
    }

    public boolean hasKnocked() {
        return cols.getAmountRevealedCards() == 2 * BibergangGame.AMOUNT_CARD_COLUMNS;
    }

    public BibergangCard moveBiber(BibergangCard biber) {
        return cols.moveBiber(biber);
    }

    public void setLastTurnPlayed() {
        lastTurnPlayed = true;
    }

    public boolean hasLastTurnPlayed() {
        return lastTurnPlayed;
    }

    public void revealAll() {
        cols.revealAll();
    }

    public int getTotalScore() {
        return cols.getTotalScore();
    }

    public int getTotalOfVisibleScore() {
        return cols.getTotalOfVisibleScore();
    }

    public boolean hasHiddenCards() {
        return cols.getAmountRevealedCards() > 0;
    }

    public boolean isAi() {
        return !isHuman();
    }

    public ExchangeOrTossCardDecsion decideExchangeOrTossCard() {
        ExchangeOrTossCardDecsion decision = defaultExchangeOrTossDecison();

        if(hasOptionFour() ){
            int score = getTotalOfVisibleScore();
            String lastUnrevealedSlot = cols.getLastUnrevealedSlot();
            if(score > 0 && decision.type == ExchangeOrTossCardDecsion.DecisionType.EXCHANGE && decision.getExchangeId().equalsIgnoreCase(lastUnrevealedSlot)){
                //ich würde das spiel beenden, aber das würde mich nur viel punkte kosten, daher tosse ich die Karte
                return ExchangeOrTossCardDecsion.toss(null);
            }
            if(decision.type == ExchangeOrTossCardDecsion.DecisionType.TOSS){
                //toss ohne austausch!
                return ExchangeOrTossCardDecsion.toss(null);
            }
        }

        return decision;
    }

    private ExchangeOrTossCardDecsion defaultExchangeOrTossDecison() {
        BibergangCard drawnCard = board.getCurrentDrawnCard();
        LOGGER.debug("now i hold {} in my hand what shall i do with it?", drawnCard.getDisplay());

        Optional<PairCardColumn> biberPairColumn = cols.findBiberPairColumnFor(drawnCard);
        if (biberPairColumn.isPresent()) {
            LOGGER.debug("i found a card to complete my biber pair, i exchange slot {}", biberPairColumn.get().exchangingId);
            return ExchangeOrTossCardDecsion.exchange(biberPairColumn.get().exchangingId);
        }

        List<ExchangeCardOption> exchangeBiberCardOptions = cols.getBiberCardExchangeOptions();
        if (drawnCard.isBiber() && !exchangeBiberCardOptions.isEmpty()) {
            Collections.sort(exchangeBiberCardOptions);
            ExchangeCardOption option = exchangeBiberCardOptions.get(0);
            LOGGER.debug("i found a place for my biber: {}", option.cardSlotId);
            return ExchangeOrTossCardDecsion.exchange(option.cardSlotId);
        }

        Optional<PairCardColumn> pairColumn = cols.findPairColumnFor(drawnCard);
        if (pairColumn.isPresent()) {
            LOGGER.debug("i found a card to complete my pair, i exchange slot {}", pairColumn.get().exchangingId);
            return ExchangeOrTossCardDecsion.exchange(pairColumn.get().exchangingId);
        }

        //eine hohe ersetzen
        List<ExchangeCardOption> exchangeCardOptions = cols.getCardExchangeOptions(drawnCard);
        //FIXME Magic Numbers
        if (!exchangeCardOptions.isEmpty() && exchangeCardOptions.stream().anyMatch(e -> e.diff >= 7)) {
            Collections.sort(exchangeCardOptions);
            ExchangeCardOption option = exchangeCardOptions.get(0);
            LOGGER.debug("i found a low value card to replace a high value card, i exchange slot {}", option.cardSlotId);
            return ExchangeOrTossCardDecsion.exchange(option.cardSlotId);
        }

        String revealingId = cols.findUnrevealedSlotId();
        //falls es eine nette niedrige Karte is suche ich ein Plätzchen für sie
        //FIXME Magic numbers
        if (drawnCard.getValue() <= 4) {
            LOGGER.debug("this low value (sweet) card gets into an unrevealed slot {}", revealingId);
            return ExchangeOrTossCardDecsion.exchange(revealingId);
        }

        //FIXME - does not meet yet
        LOGGER.debug("i have no use for my drawn card, i toss it and reveal {}", revealingId);
        return ExchangeOrTossCardDecsion.toss(revealingId);
    }


    public void setGame(BibergangBoard board) {
        this.board = board;
    }

    public boolean isHuman() {
        return isHuman;
    }

    public String getName() {
        return name;
    }
}
