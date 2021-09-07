package com.github.martinfrank.bibergang;

import com.github.martinfrank.bibergang.ai.ExchangeCardOption;
import com.github.martinfrank.bibergang.ai.PairCardColumn;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BibergangCardColumns {

    private final BibergangCardColumn[] columns;

    public BibergangCardColumns() {
        columns = new BibergangCardColumn[BibergangGame.AMOUNT_CARD_COLUMNS];
        IntStream.range(0, BibergangGame.AMOUNT_CARD_COLUMNS).forEach(i -> columns[i] = new BibergangCardColumn(i));
    }

    public void setCardById(BibergangCard card, String id) {
        for (BibergangCardColumn column : columns) {
            BibergangCard candidate = column.getCard(id);
            if (candidate != null) {
                column.setCard(card, id);
            }
        }
    }

    public Optional<PairCardColumn> findPairColumnFor(BibergangCard openCard) {
        for (BibergangCardColumn column : columns) {
            if (column.topCard.isRevealed() && column.bottomCard.isRevealed() && column.topCard.isPair(column.bottomCard)) {
                continue;
            }
            if (column.topCard.isRevealed() && column.topCard.isBiber() && column.bottomCard.isRevealed()) {
                continue;
            }
            if (column.bottomCard.isRevealed() && column.bottomCard.isBiber() && column.topCard.isRevealed()) {
                continue;
            }

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

    public Optional<PairCardColumn> findBiberPairColumnFor(BibergangCard openCard) {
        for (BibergangCardColumn column : columns) {
            if (column.topCard.isRevealed() && column.topCard.isBiber() && column.bottomCard.isRevealed() && column.bottomCard.isPair(openCard)) {
                return Optional.of(new PairCardColumn(column, column.bottomCard, column.topCard, column.getTopCardId()));
            }
            if (column.bottomCard.isRevealed() && column.bottomCard.isBiber() && column.topCard.isRevealed() && column.topCard.isPair(openCard)) {
                return Optional.of(new PairCardColumn(column, column.topCard, column.bottomCard, column.getBottomCardId()));
            }
        }
        return Optional.empty();
    }

    public List<ExchangeCardOption> getCardExchangeOptions(BibergangCard openCard) {
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

    public void revealAll() {
        columnCardStream().forEach(BibergangCard::reveal);
    }

    public String findUnrevealedSlotId() {
        for (BibergangCardColumn column : randomColumns()) {
            if (!column.topCard.isRevealed() && !column.bottomCard.isRevealed()) {
                return Math.random() < 0.5 ? column.getTopCardId() : column.getBottomCardId();
            }
        }
        for (BibergangCardColumn column : columns) {
            if (!column.topCard.isRevealed()) {
                return column.getTopCardId();
            }
            if (!column.bottomCard.isRevealed()) {
                return column.getBottomCardId();
            }
        }
        throw new RuntimeException("id is still null!");
    }

    private List<BibergangCardColumn> randomColumns() {
        List<BibergangCardColumn> list = Arrays.asList(columns);
        Collections.shuffle(list);
        return list;
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

    public void revealStartCards() {
        columns[0].topCard.reveal();
        columns[BibergangGame.AMOUNT_CARD_COLUMNS - 1].bottomCard.reveal();
    }

    public int getAmountRevealedCards() {
        return (int) columnCardStream().filter(BibergangCard::isRevealed).count();
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

    public BibergangCard getCardById(String id) {
        for (BibergangCardColumn column : columns) {
            BibergangCard card = column.getCard(id);
            if (card != null) {
                return card;
            }
        }
        return null;
    }

    //wohin soll ich nur meinen Biber legen? hat die Antwort
    public List<ExchangeCardOption> getBiberCardExchangeOptions() {
        List<ExchangeCardOption> list = new ArrayList<>();
        for(BibergangCardColumn column: getColumnsWithoutPairs()){
            //first prio: biber to high value card
            if (column.topCard.isRevealed() && !column.topCard.isBiber()){
                list.add(new ExchangeCardOption(column.getTopCardId(), column.topCard.getValue(), column.topCard));
            }
            if (column.bottomCard.isRevealed() && !column.bottomCard.isBiber()){
                list.add(new ExchangeCardOption(column.getTopCardId(), column.bottomCard.getValue(), column.bottomCard));
            }
            if(!column.topCard.isRevealed() && !column.bottomCard.isRevealed()){
                //FIXME magic numbers
                list.add(new ExchangeCardOption(column.getTopCardId(), 7, column.topCard));
                list.add(new ExchangeCardOption(column.getTopCardId(), 7, column.topCard));
            }
        }
        Collections.shuffle(list);
        return list;
    }

    private List<BibergangCardColumn> getColumnsWithoutPairs() {
        List<BibergangCardColumn> list = new ArrayList<>();
        for(BibergangCardColumn column: columns){
            if (!column.isVisiblePair() ){
                list.add(column);
            }
        }
        return list;
    }

    public String getLastUnrevealedSlot() {
        for(BibergangCardColumn column: columns){
            if (!column.topCard.isRevealed() ){
                return column.getTopCardId();
            }
            if (!column.bottomCard.isRevealed() ){
                return column.getBottomCardId();
            }
        }
        throw new RuntimeException("requesting for unrevealed slot even though all cards are revealed");
    }

    public BibergangCard moveBiber(BibergangCard biber) {
        List<ExchangeCardOption> options = new ArrayList<>();
        for (BibergangCardColumn column : columns) {
            if (!column.topCard.isRevealed()) {
                options.add(new ExchangeCardOption(column.getTopCardId(), column.bottomCard.getValue(), biber));
            }
            if (!column.bottomCard.isRevealed()) {
                options.add(new ExchangeCardOption(column.getBottomCardId(), column.topCard.getValue(), biber));
            }
        }
        if(!options.isEmpty()){
            Collections.sort(options);
            ExchangeCardOption option = options.get(0);
            BibergangCard predecessor = getCardById(option.cardSlotId);
            setCardById(biber, option.cardSlotId);
            return predecessor;
        }
        return biber; //falls er nicht wandern kann wird er abgeworfen
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
}
