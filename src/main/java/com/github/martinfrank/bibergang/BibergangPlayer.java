package com.github.martinfrank.bibergang;

import com.github.martinfrank.bibergang.ai.ExchangeCardDecision;
import com.github.martinfrank.bibergang.ai.ExchangeCardOption;
import com.github.martinfrank.bibergang.ai.PairCardColumn;
import com.github.martinfrank.boardgamelib.BasePlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BibergangPlayer extends BasePlayer<BibergangBoard> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasePlayer.class);

    private final BibergangCardColumn[] columns;
    private boolean hasDrawn = false;
    private boolean lastTurnPlayed = false;

    BibergangPlayer(String name, int color, boolean isHuman) {
        super(name, color, isHuman);
        columns = new BibergangCardColumn[BibergangGame.AMOUNT_CARD_COLUMNS];
        IntStream.range(0, BibergangGame.AMOUNT_CARD_COLUMNS).forEach(i -> columns[i] = new BibergangCardColumn(i));
    }


    @Override
    public void performAiTurn() {
        LOGGER.debug("{} is working on its bibergang AI move - open card is {}", getName(), getBoardGame().getOpenStack().getTopCard().getValue());
        BibergangBoard board = getBoardGame();
//        board.startPlayersTurn();
        ExchangeCardDecision exchangeCardOption = decideDrawCard();
        LOGGER.debug("{} draws from {}", getName(), exchangeCardOption);
        BibergangCard card = drawCard(exchangeCardOption);
        LOGGER.debug("{} now holds this card: {}", getName(), card);
        exchangeOrToss(card);
        board.endPlayersTurn();
    }

    private void exchangeOrToss(BibergangCard card) {
        if (hasOptionFour()) {
            exchangeOrTossWithOptionFour(card);
            return;
        }

        if (card.isBiber()) {
            String biberSlotId = findBiberSlotId();
            LOGGER.debug("{} wants to take a biber card ({})into the dec ({})", getName(), card.getValue(), biberSlotId);
            getBoardGame().exchangeCard(biberSlotId);
            getBoardGame().tossCard();
        }

        Optional<PairCardColumn> pairColumn = findPairColumnFor(card);
        if (pairColumn.isPresent()) {
            exchangeToPair(pairColumn.get());
            return;
        }

        //eine hohe ersetzen
        List<ExchangeCardOption> exchangeCardOptions = getCardExchangeOptions(card);
        if (!exchangeCardOptions.isEmpty() && exchangeCardOptions.stream().anyMatch(e -> e.diff >= 7)) {
            exchangeHighValueCard(exchangeCardOptions);
            return;
        }

        String slotId = findUnrevealedSlotId();
        //eine nette karte (0...3(4?)) ins Deck nehmen
        if (card.getValue() <= 4) {
            LOGGER.debug("{} wants to take a sweet card ({})into the dec ({})", getName(), card.getValue(), slotId);
            getBoardGame().exchangeCard(slotId);
            getBoardGame().tossCard();
            return;
        }

        //you must first reveal one card of your hand - rules are rules
        LOGGER.debug("{} tosses a card ({}) back on the stack and reveals slot ({})", getName(), card.getValue(), slotId);
        getCardById(slotId).reveal();
        getBoardGame().tossCard();
    }

    private String findBiberSlotId() {
        int value = -1;
        String slotId = null;
        for (BibergangCardColumn column : columns) {
            if (!column.isVisiblePair()) {
                if (column.topCard.isRevealed() && !column.topCard.isBiber()) {
                    if (column.bottomCard.isRevealed() && !column.bottomCard.isBiber()) {
                        if (value < column.topCard.getValue()) {
                            value = column.topCard.getValue();
                            slotId = column.getBottomCardId();
                        }
                    }
                    if (!column.bottomCard.isRevealed()) {
                        if (value < column.topCard.getValue()) {
                            value = column.topCard.getValue();
                            slotId = column.getBottomCardId();
                        }
                    }
                }

                if (column.bottomCard.isRevealed() && !column.bottomCard.isBiber()) {
                    if (column.topCard.isRevealed() && !column.topCard.isBiber()) {
                        if (value < column.bottomCard.getValue()) {
                            value = column.bottomCard.getValue();
                            slotId = column.getBottomCardId();
                        }
                    }
                    if (!column.topCard.isRevealed()) {
                        if (value < column.bottomCard.getValue()) {
                            value = column.bottomCard.getValue();
                            slotId = column.getBottomCardId();
                        }
                    }
                }
            }
        }
        if (slotId == null) {
            throw new RuntimeException("slotId could not be determined");
        }
        return slotId;
    }

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

    private String findUnrevealedSlotId() {
        for (BibergangCardColumn column : columns) {
            if (!column.topCard.isRevealed() && !column.bottomCard.isRevealed()) {
                return Math.random() < 0.5 ? column.getTopCardId() : column.getBottomCardId();
            }
            if (!column.topCard.isRevealed()) {
                return column.getTopCardId();
            }
            if (!column.bottomCard.isRevealed()) {
                return column.getBottomCardId();
            }
        }
        throw new RuntimeException("id is still null!");
    }

    private void exchangeOrTossWithOptionFour(BibergangCard card) {
        if (card.isBiber()) {
            //FIXME - prüfen ob ich damit das spiel beende und ggf punkte checken - bei niedrigen punkten abbruch!!
            String biberSlotId = findBiberSlotId();
            if(getTotalOfVisibleScore() > 0 && !getCardById(biberSlotId).isRevealed() ){
                //1-2,2-3,3-4,12-? -> h ist falscher index
            }
            LOGGER.debug("{} wants to take a biber card ({})into the dec ({})", getName(), card.getValue(), biberSlotId);
            getBoardGame().exchangeCard(biberSlotId);
            getBoardGame().tossCard();
        }

        Optional<PairCardColumn> pairColumn = findPairColumnFor(card);
        if (pairColumn.isPresent()) {

        }
    }


    private BibergangCard drawCard(ExchangeCardDecision exchangeCardOption) {
        if (exchangeCardOption == ExchangeCardDecision.FROM_OPEN_STACK) {
            getBoardGame().drawCurrentCardFromOpen();
        }
        if (exchangeCardOption == ExchangeCardDecision.FROM_CLOSED_STACK) {
            getBoardGame().drawCurrentCardFromClosed();
        }
        return getBoardGame().getCurrentDrawnCard();
    }


    //visibleForTest
    ExchangeCardDecision decideDrawCard() {
        BibergangCard openCard = getBoardGame().getOpenStack().getTopCard();
        if (openCard.isBiber()) {
            LOGGER.debug("Open Card is Biber --> i draw from open stack");
            return ExchangeCardDecision.FROM_OPEN_STACK;
        }
        Optional<PairCardColumn> pairColumn = findPairColumnFor(openCard);
        if (pairColumn.isPresent()) {
            LOGGER.debug("Open Card forms a pair --> i draw from open stack");
            return ExchangeCardDecision.FROM_OPEN_STACK;
        }

        List<ExchangeCardOption> exchangeCardOptions = getCardExchangeOptions(openCard);
        if (exchangeCardOptions.stream().anyMatch(o -> o.diff >= 7)) {
            LOGGER.debug("Open Card can make a diff over 7 --> i draw from open stack");
            return ExchangeCardDecision.FROM_OPEN_STACK;
        }
        if (openCard.getValue() <= 4) {
            LOGGER.debug("Open Card looks too sweet (<=4) to resist --> i draw from open stack");
            return ExchangeCardDecision.FROM_OPEN_STACK;
        }
        LOGGER.debug("Open Card is not an option, i draw from closed");
        return ExchangeCardDecision.FROM_CLOSED_STACK;
    }

    //visibleForTesting
    List<ExchangeCardOption> getCardExchangeOptions(BibergangCard openCard) {
        List<ExchangeCardOption> exchangeCardOptions = new ArrayList<>();
        for (BibergangCardColumn column : columns) {
            if (!column.isVisiblePair()) {
                addExchangeCard(column.topCard, openCard, exchangeCardOptions, column.getTopCardId());
                addExchangeCard(column.bottomCard, openCard, exchangeCardOptions, column.getBottomCardId());
            }
        }
        return exchangeCardOptions;
    }

    private void addExchangeCard(BibergangCard candidate, BibergangCard card, List<ExchangeCardOption> options, String id) {
        if (candidate.isRevealed() && !candidate.isBiber()) { //biber has value 0 -> diff would never be > 0... doppeltgemoppelt
            int diff = candidate.getValue() - card.getValue();
            if (diff > 0) {
                options.add(new ExchangeCardOption(id, diff, candidate));
            }
        }
    }


    //suche eine column, in der ein paar gebildet werden könnte
    private Optional<PairCardColumn> findPairColumnFor(BibergangCard openCard) {
        for (BibergangCardColumn column : columns) {
            if (column.topCard.isPair(openCard) && !column.bottomCard.isRevealed()) {
                return Optional.of(new PairCardColumn(column, column.topCard, column.bottomCard, column.getBottomCardId()));
            }
            if (column.topCard.isPair(openCard) && !column.bottomCard.isPair(openCard)) {
                return Optional.of(new PairCardColumn(column, column.topCard, column.bottomCard, column.getBottomCardId()));
            }
            if (column.bottomCard.isPair(openCard) && !column.topCard.isRevealed()) {
                return Optional.of(new PairCardColumn(column, column.bottomCard, column.topCard, column.getTopCardId()));
            }
            if (column.bottomCard.isPair(openCard) && !column.topCard.isPair(openCard)) {
                return Optional.of(new PairCardColumn(column, column.bottomCard, column.topCard, column.getTopCardId()));
            }
        }
        return Optional.empty();
    }

    public void addStartCard(BibergangCard card) {
        for (BibergangCardColumn column : columns) {
            if (column.topCard == null) {
                column.topCard = card;
                return;
            }
            if (column.bottomCard == null) {
                column.bottomCard = card;
                return;
            }
        }
        throw new IllegalArgumentException("card columns are full");
    }

    public BibergangCard getCardById(String id) {
        for (BibergangCardColumn column : columns) {
            BibergangCard card = column.getCard(id);
            if (card != null) {
                return card;
            }
        }
        return null;
    }

    public void revealStartCards() {
        columns[0].topCard.reveal();
        columns[BibergangGame.AMOUNT_CARD_COLUMNS - 1].bottomCard.reveal();
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
        return getAmountRevealedCards() == BibergangGame.AMOUNT_CARD_COLUMNS * 2 - 1;
    }

    public void setCardById(BibergangCard card, String id) {
        for (BibergangCardColumn column : columns) {
            BibergangCard candidate = column.getCard(id);
            if (candidate != null) {
                column.setCard(card, id);
            }
        }
    }

    public boolean hasKnocked() {
        return getAmountRevealedCards() == 2 * BibergangGame.AMOUNT_CARD_COLUMNS;
    }

    private int getAmountRevealedCards() {
        return (int) columnCardStream().filter(BibergangCard::isRevealed).count();
    }

    public BibergangCard moveBiber(BibergangCard biber) {
        for (BibergangCardColumn column : columns) {
            if (!column.topCard.isRevealed()) {
                BibergangCard card = column.topCard;
                column.topCard = biber;
                return card;
            }
            if (!column.bottomCard.isRevealed()) {
                BibergangCard card = column.bottomCard;
                column.bottomCard = biber;
                return card;
            }
        }
        return biber; //falls er nicht wandern kann wird er abgeworfen
    }

    public void setLastTurnPlayed() {
        lastTurnPlayed = true;
    }

    public boolean hasLastTurnPlayed() {
        return lastTurnPlayed;
    }

    public void revealAll() {
        columnCardStream().forEach(BibergangCard::reveal);
    }

    public Stream<BibergangCard> columnCardStream() {
        BibergangCard[] cards = new BibergangCard[BibergangGame.AMOUNT_CARD_COLUMNS * 2];
        for (int i = 0; i < BibergangGame.AMOUNT_CARD_COLUMNS; i++) {
            BibergangCardColumn column = columns[i];
            cards[2 * i] = column.topCard;
            cards[2 * i + 1] = column.bottomCard;
        }
        return Arrays.stream(cards);
    }

    public int getTotalScore() {
        int total = 0;
        for (BibergangCardColumn column : columns) {
            if (column.isPair()) {
                total = total - 5;
            } else {
                total = total + column.topCard.getValue();
                total = total + column.bottomCard.getValue();
            }
        }
        return total;
    }

    public int getTotalOfVisibleScore() {
        int total = 0;
        for (BibergangCardColumn column : columns) {
            if (column.isVisiblePair()) {
                total = total - 5;
            } else {
                if (column.topCard.isRevealed()) {
                    total = total + column.topCard.getValue();
                }
                if (column.bottomCard.isRevealed()) {
                    total = total + column.bottomCard.getValue();
                }
            }
        }
        return total;
    }

    public boolean hasHiddenCards() {
        return getAmountRevealedCards() > 0;
    }

    public boolean isAi() {
        return !isHuman();
    }
}
