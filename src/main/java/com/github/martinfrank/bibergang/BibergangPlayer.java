package com.github.martinfrank.bibergang;

import com.github.martinfrank.bibergang.ai.DrawFromStackDecision;
import com.github.martinfrank.bibergang.ai.ExchangeCardOption;
import com.github.martinfrank.bibergang.ai.ExchangeOrTossCardDecsion;
import com.github.martinfrank.bibergang.ai.PairCardColumn;
import com.github.martinfrank.boardgamelib.BasePlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BibergangPlayer extends BasePlayer<BibergangBoard> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasePlayer.class);

//    private final BibergangCardColumn[] columns;
    private boolean hasDrawn = false;
    private boolean lastTurnPlayed = false;
    public final BibergangCardColumns cols;

    BibergangPlayer(String name, int color, boolean isHuman) {
        super(name, color, isHuman);
//        columns = new BibergangCardColumn[BibergangGame.AMOUNT_CARD_COLUMNS];
//        IntStream.range(0, BibergangGame.AMOUNT_CARD_COLUMNS).forEach(i -> columns[i] = new BibergangCardColumn(i));
        cols = new BibergangCardColumns();
    }


    @Override
    public void performAiTurn() {
        LOGGER.debug("{} is working on its bibergang AI move - open card is {}", getName(), getBoardGame().getOpenStack().getTopCard().getValue());
        BibergangBoard board = getBoardGame();
//        board.startPlayersTurn();
        DrawFromStackDecision exchangeCardOption = decideDrawStack();
        LOGGER.debug("{} draws from {}", getName(), exchangeCardOption);
        BibergangCard card = drawCard(exchangeCardOption);
        LOGGER.debug("{} now holds this card: {}", getName(), card);
//        exchangeOrToss(card);
        ExchangeOrTossCardDecsion decision = decideExchangeOrTossCard();
        exchangeOrToss(decision);
        board.endPlayersTurn();
    }

    private void exchangeOrToss(ExchangeOrTossCardDecsion decision) {
        if(decision.type == ExchangeOrTossCardDecsion.DecisionType.EXCHANGE){
            getBoardGame().exchangeCard(decision.getExchangeId());
        }
        if(decision.type == ExchangeOrTossCardDecsion.DecisionType.TOSS){
            if(decision.getExchangeId() != null){
                cols.getCardById(decision.getExchangeId()).reveal();
            }
        }
        getBoardGame().tossCard();
    }

//    private void exchangeOrToss(BibergangCard card) {
//        if (hasOptionFour()) {
//            exchangeOrTossWithOptionFour(card);
//            return;
//        }
//
//        if (card.isBiber()) {
//            String biberSlotId = findBiberSlotId();
//            LOGGER.debug("{} wants to take a biber card ({})into the dec ({})", getName(), card.getValue(), biberSlotId);
//            getBoardGame().exchangeCard(biberSlotId);
//            getBoardGame().tossCard();
//            return;
//        }
//
//        Optional<PairCardColumn> pairColumn = findPairColumnFor(card);
//        if (pairColumn.isPresent()) {
//            exchangeToPair(pairColumn.get());
//            return;
//        }
//
//        //eine hohe ersetzen
//        List<ExchangeCardOption> exchangeCardOptions = getCardExchangeOptions(card);
//        if (!exchangeCardOptions.isEmpty() && exchangeCardOptions.stream().anyMatch(e -> e.diff >= 7)) {
//            exchangeHighValueCard(exchangeCardOptions);
//            return;
//        }
//
//        String slotId = findUnrevealedSlotId();
//        //eine nette karte (0...3(4?)) ins Deck nehmen
//        if (card.getValue() <= 4) {
//            LOGGER.debug("{} wants to take a sweet card ({})into the dec ({})", getName(), card.getValue(), slotId);
//            getBoardGame().exchangeCard(slotId);
//            getBoardGame().tossCard();
//            return;
//        }
//
//        //you must first reveal one card of your hand - rules are rules
//        LOGGER.debug("{} tosses a card ({}) back on the stack and reveals slot ({})", getName(), card.getValue(), slotId);
//        getCardById(slotId).reveal();
//        getBoardGame().tossCard();
//    }

//    private String findBiberSlotId() {
//        int value = -1;
//        String slotId = null;
//        for (BibergangCardColumn column : columns) {
//            if (!column.isVisiblePair()) {
//                if (column.topCard.isRevealed() && !column.topCard.isBiber()) {
//                    if (column.bottomCard.isRevealed() && !column.bottomCard.isBiber()) {
//                        if (value < column.topCard.getValue()) {
//                            value = column.topCard.getValue();
//                            slotId = column.getBottomCardId();
//                        }
//                    }
//                    if (!column.bottomCard.isRevealed()) {
//                        if (value < column.topCard.getValue()) {
//                            value = column.topCard.getValue();
//                            slotId = column.getBottomCardId();
//                        }
//                    }
//                }
//
//                if (column.bottomCard.isRevealed() && !column.bottomCard.isBiber()) {
//                    if (column.topCard.isRevealed() && !column.topCard.isBiber()) {
//                        if (value < column.bottomCard.getValue()) {
//                            value = column.bottomCard.getValue();
//                            slotId = column.getBottomCardId();
//                        }
//                    }
//                    if (!column.topCard.isRevealed()) {
//                        if (value < column.bottomCard.getValue()) {
//                            value = column.bottomCard.getValue();
//                            slotId = column.getBottomCardId();
//                        }
//                    }
//                }
//            }
//        }
//        if (slotId == null) {
//            throw new RuntimeException("slotId could not be determined");
//        }
//        return slotId;
//    }

    private void exchangeHighValueCard(List<ExchangeCardOption> exchangeCardOptions) {
        Collections.sort(exchangeCardOptions);
        ExchangeCardOption exchangeCardOption = exchangeCardOptions.get(0);
        LOGGER.debug("{} wants to exchange a high value {} card. (-{})", getName(), exchangeCardOption.cardSlotId, exchangeCardOption.diff);
        getBoardGame().exchangeCard(exchangeCardOption.cardSlotId);
        getBoardGame().tossCard();


    }

    private void exchangeToPair(PairCardColumn pairColumn) {
        LOGGER.debug("{} wants to make a pair with: {}", getName(), pairColumn.exchangingId);
        getBoardGame().exchangeCard(pairColumn.exchangingId);
        getBoardGame().tossCard();
    }

//    private String findUnrevealedSlotId() {
//        for (BibergangCardColumn column : columns) {
//            if (!column.topCard.isRevealed() && !column.bottomCard.isRevealed()) {
//                return Math.random() < 0.5 ? column.getTopCardId() : column.getBottomCardId();
//            }
//            if (!column.topCard.isRevealed()) {
//                return column.getTopCardId();
//            }
//            if (!column.bottomCard.isRevealed()) {
//                return column.getBottomCardId();
//            }
//        }
//        throw new RuntimeException("id is still null!");
//    }

//    private void exchangeOrTossWithOptionFour(BibergangCard card) {
//        if (card.isBiber()) {
//            //FIXME - prüfen ob ich damit das spiel beende und ggf punkte checken - bei niedrigen punkten abbruch!!
//            String biberSlotId = findBiberSlotId();
//            if (getTotalOfVisibleScore() > 0 && !getCardById(biberSlotId).isRevealed()) {
//                //1-2,2-3,3-4,12-? -> h ist falscher index
//            }
//            LOGGER.debug("{} wants to take a biber card ({})into the dec ({})", getName(), card.getValue(), biberSlotId);
//            getBoardGame().exchangeCard(biberSlotId);
//            getBoardGame().tossCard();
//            return;
//        }
//
//        //FIXME - prüfen ob ich damit das spiel beende und ggf punkte checken - bei niedrigen punkten abbruch!!
//        Optional<PairCardColumn> pairColumn = findPairColumnFor(card);
//        if (pairColumn.isPresent()) {
//            exchangeToPair(pairColumn.get());
//            return;
//        }
//
//        //FIXME - prüfen ob ich damit das spiel beende und ggf punkte checken - bei niedrigen punkten abbruch!!
//        //teure karte ersetzen
//        List<ExchangeCardOption> exchangeCardOptions = getCardExchangeOptions(card);
//        if (!exchangeCardOptions.isEmpty() && exchangeCardOptions.stream().anyMatch(e -> e.diff >= 7)) {
//            exchangeHighValueCard(exchangeCardOptions);
//            return;
//        }
//
//        //you must first reveal one card of your hand - rules are rules
//        LOGGER.debug("{} tosses a card ({}) back on the without revealing last card.", getName(), card.getValue());
//        getBoardGame().tossCard();
//    }
//
//
    private BibergangCard drawCard(DrawFromStackDecision exchangeCardOption) {
        if (exchangeCardOption == DrawFromStackDecision.FROM_OPEN_STACK) {
            getBoardGame().drawCurrentCardFromOpen();
        }
        if (exchangeCardOption == DrawFromStackDecision.FROM_CLOSED_STACK) {
            getBoardGame().drawCurrentCardFromClosed();
        }
        return getBoardGame().getCurrentDrawnCard();
    }


    //visibleForTest
    DrawFromStackDecision decideDrawStack() {
        BibergangCard openCard = getBoardGame().getOpenStack().getTopCard();
        LOGGER.debug("Open Card is {} --> which stack shall i draw from?", openCard.getDisplay());
        if (openCard.isBiber()) {
            LOGGER.debug("Open Card is Biber --> i draw from open stack");
            return DrawFromStackDecision.FROM_OPEN_STACK;
        }

        //FIXME
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

    //visibleForTesting
//    List<ExchangeCardOption> getCardExchangeOptions(BibergangCard openCard) {
//        List<ExchangeCardOption> exchangeCardOptions = new ArrayList<>();
//        for (BibergangCardColumn column : columns) {
//            if (!column.isVisiblePair()) {
//                addExchangeCard(column.topCard, openCard, exchangeCardOptions, column.getTopCardId());
//                addExchangeCard(column.bottomCard, openCard, exchangeCardOptions, column.getBottomCardId());
//            }
//        }
//        return exchangeCardOptions;
//    }

//    private void addExchangeCard(BibergangCard candidate, BibergangCard card, List<ExchangeCardOption> options, String id) {
//        if (candidate.isRevealed() && !candidate.isBiber()) { //biber has value 0 -> diff would never be > 0... doppeltgemoppelt
//            int diff = candidate.getValue() - card.getValue();
//            if (diff > 0) {
//                options.add(new ExchangeCardOption(id, diff, candidate));
//            }
//        }
//    }


    //suche eine column, in der ein paar gebildet werden könnte
//    private Optional<PairCardColumn> findPairColumnFor(BibergangCard openCard) {
//        for (BibergangCardColumn column : columns) {
//            if (column.topCard.isRevealed() && column.bottomCard.isRevealed() && column.topCard.isPair(column.bottomCard)) {
//                continue;
//            }
//
//            if (column.topCard.isPair(openCard) && !column.bottomCard.isRevealed()) {
//                return Optional.of(new PairCardColumn(column, column.topCard, column.bottomCard, column.getBottomCardId()));
//            }
//            if (column.topCard.isPair(openCard) && !column.bottomCard.isPair(openCard)) {
//                return Optional.of(new PairCardColumn(column, column.topCard, column.bottomCard, column.getBottomCardId()));
//            }
//            if (column.bottomCard.isPair(openCard) && !column.topCard.isRevealed()) {
//                return Optional.of(new PairCardColumn(column, column.bottomCard, column.topCard, column.getTopCardId()));
//            }
//            if (column.bottomCard.isPair(openCard) && !column.topCard.isPair(openCard)) {
//                return Optional.of(new PairCardColumn(column, column.bottomCard, column.topCard, column.getTopCardId()));
//            }
//        }
//        return Optional.empty();
//    }

    public void addStartCard(BibergangCard card) {
        cols.addStartCard(card);
//        for (BibergangCardColumn column : columns) {
//            if (column.topCard == null) {
//                column.topCard = card;
//                return;
//            }
//            if (column.bottomCard == null) {
//                column.bottomCard = card;
//                return;
//            }
//        }
//        throw new IllegalArgumentException("card columns are full");
    }

    public BibergangCard getCardById(String id) {
        return cols.getCardById(id);
//        for (BibergangCardColumn column : columns) {
//            BibergangCard card = column.getCard(id);
//            if (card != null) {
//                return card;
//            }
//        }
//        return null;
    }

    public void revealStartCards() {
        cols.revealStartCards();
//        columns[0].topCard.reveal();
//        columns[BibergangGame.AMOUNT_CARD_COLUMNS - 1].bottomCard.reveal();
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
//        return getAmountRevealedCards() == BibergangGame.AMOUNT_CARD_COLUMNS * 2 - 1;
    }

    public void setCardById(BibergangCard card, String id) {
//        for (BibergangCardColumn column : columns) {
//            BibergangCard candidate = column.getCard(id);
//            if (candidate != null) {
//                column.setCard(card, id);
//            }
//        }
        cols.setCardById(card, id);
    }

    public boolean hasKnocked() {
        return cols.getAmountRevealedCards() == 2 * BibergangGame.AMOUNT_CARD_COLUMNS;
    }

//    private int getAmountRevealedCards() {
//        return (int) columnCardStream().filter(BibergangCard::isRevealed).count();
//    }

    public BibergangCard moveBiber(BibergangCard biber) {
        return cols.moveBiber(biber);
//        for (BibergangCardColumn column : columns) {
//            if (!column.topCard.isRevealed()) {
//                BibergangCard card = column.topCard;
//                column.topCard = biber;
//                return card;
//            }
//            if (!column.bottomCard.isRevealed()) {
//                BibergangCard card = column.bottomCard;
//                column.bottomCard = biber;
//                return card;
//            }
//        }
//        return biber; //falls er nicht wandern kann wird er abgeworfen
    }

    public void setLastTurnPlayed() {
        lastTurnPlayed = true;
    }

    public boolean hasLastTurnPlayed() {
        return lastTurnPlayed;
    }

    public void revealAll() {
//        columnCardStream().forEach(BibergangCard::reveal);
        cols.revealAll();
    }

//    public Stream<BibergangCard> columnCardStream() {
//        BibergangCard[] cards = new BibergangCard[BibergangGame.AMOUNT_CARD_COLUMNS * 2];
//        for (int i = 0; i < BibergangGame.AMOUNT_CARD_COLUMNS; i++) {
//            BibergangCardColumn column = columns[i];
//            cards[2 * i] = column.topCard;
//            cards[2 * i + 1] = column.bottomCard;
//        }
//        return Arrays.stream(cards);
//    }

    public int getTotalScore() {
        return cols.getTotalScore();
//        int total = 0;
//        for (BibergangCardColumn column : columns) {
//            if (column.isPair()) {
//                total = total - 5;
//            } else {
//                total = total + column.topCard.getValue();
//                total = total + column.bottomCard.getValue();
//            }
//        }
//        return total;
    }

    public int getTotalOfVisibleScore() {
        return cols.getTotalOfVisibleScore();
//        int total = 0;
//        for (BibergangCardColumn column : columns) {
//            if (column.isVisiblePair()) {
//                total = total - 5;
//            } else {
//                if (column.topCard.isRevealed()) {
//                    total = total + column.topCard.getValue();
//                }
//                if (column.bottomCard.isRevealed()) {
//                    total = total + column.bottomCard.getValue();
//                }
//            }
//        }
//        return total;
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
        BibergangCard drawnCard = getBoardGame().getCurrentDrawnCard();
        LOGGER.debug("now i hold {} in my hand what shall i do with it?", drawnCard.getDisplay());

        Optional<PairCardColumn> biberPairColumn = cols.findBiberPairColumnFor(drawnCard);
        if (biberPairColumn.isPresent()) {
            LOGGER.debug("i found a card to complete my biber pair, i exchange slot " + biberPairColumn.get().exchangingId);
            return ExchangeOrTossCardDecsion.exchange(biberPairColumn.get().exchangingId);
        }

        List<ExchangeCardOption> exchangeBiberCardOptions = cols.getBiberCardExchangeOptions();
        if (drawnCard.isBiber() && !exchangeBiberCardOptions.isEmpty()) {
            Collections.sort(exchangeBiberCardOptions);
            ExchangeCardOption option = exchangeBiberCardOptions.get(0);
            LOGGER.debug("i found a place for my biber: " + option.cardSlotId);
            return ExchangeOrTossCardDecsion.exchange(option.cardSlotId);
        }

        Optional<PairCardColumn> pairColumn = cols.findPairColumnFor(drawnCard);
        if (pairColumn.isPresent()) {
            LOGGER.debug("i found a card to complete my pair, i exchange slot " + pairColumn.get().exchangingId);
            return ExchangeOrTossCardDecsion.exchange(pairColumn.get().exchangingId);
        }

        //eine hohe ersetzen
        List<ExchangeCardOption> exchangeCardOptions = cols.getCardExchangeOptions(drawnCard);
        //FIXME Magic Numbers
        if (!exchangeCardOptions.isEmpty() && exchangeCardOptions.stream().anyMatch(e -> e.diff >= 7)) {
            Collections.sort(exchangeCardOptions);
            ExchangeCardOption option = exchangeCardOptions.get(0);
            LOGGER.debug("i found a low value card to replace a high value card, i exchange slot " + option.cardSlotId);
            return ExchangeOrTossCardDecsion.exchange(option.cardSlotId);
        }

        String revealingId = cols.findUnrevealedSlotId();
        //falls es eine nette niedrige Karte is suche ich ein Plätzchen für sie
        //FIXME Magic numbers
        if(drawnCard.getValue() <= 4){
            LOGGER.debug("this low value (sweet) card gets into an unrevealed slot " + revealingId);
            return ExchangeOrTossCardDecsion.exchange(revealingId);
        }

        //FIXME - does not meet yet
        LOGGER.debug("i have no use for my drawn card, i toss it and reveal " + revealingId);
        return ExchangeOrTossCardDecsion.toss(revealingId);
    }
}
