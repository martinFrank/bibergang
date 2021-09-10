package com.github.martinfrank.bibergang;

import com.github.martinfrank.bibergang.ai.AiSettings;
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

    public static int amountCards() {
        return 2 * BibergangGame.AMOUNT_CARD_COLUMNS;
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
            if (column.isVisiblePair()) {
                continue;
            }

            if (column.getTopCard().isPair(openCard) && !column.getBottomCard().isRevealed()) {
                return Optional.of(new PairCardColumn(column, column.getTopCard(), column.getBottomCard(), column.getBottomCardId()));
            }
            if (column.getTopCard().isPair(openCard) && !column.getBottomCard().isPair(openCard)) {
                return Optional.of(new PairCardColumn(column, column.getTopCard(), column.getBottomCard(), column.getBottomCardId()));
            }
            if (column.getBottomCard().isPair(openCard) && !column.getTopCard().isRevealed()) {
                return Optional.of(new PairCardColumn(column, column.getBottomCard(), column.getTopCard(), column.getTopCardId()));
            }
            if (column.getBottomCard().isPair(openCard) && !column.getTopCard().isPair(openCard)) {
                return Optional.of(new PairCardColumn(column, column.getBottomCard(), column.getTopCard(), column.getTopCardId()));
            }
        }
        return Optional.empty();
    }

    public Optional<PairCardColumn> findBiberPairColumnFor(BibergangCard openCard) {
        for (BibergangCardColumn column : columns) {
            if (column.getTopCard().isRevealed() && column.getTopCard().isBiber() && column.getBottomCard().isRevealed() && column.getBottomCard().isPair(openCard)) {
                return Optional.of(new PairCardColumn(column, column.getBottomCard(), column.getTopCard(), column.getTopCardId()));
            }
            if (column.getBottomCard().isRevealed() && column.getBottomCard().isBiber() && column.getTopCard().isRevealed() && column.getTopCard().isPair(openCard)) {
                return Optional.of(new PairCardColumn(column, column.getTopCard(), column.getBottomCard(), column.getBottomCardId()));
            }
        }
        return Optional.empty();
    }

    public List<ExchangeCardOption> getCardExchangeOptions(BibergangCard openCard) {
        List<ExchangeCardOption> exchangeCardOptions = new ArrayList<>();
        for (BibergangCardColumn column : columns) {
            if (!column.isVisiblePair()) {
                addExchangeCard(column.getTopCard(), openCard, exchangeCardOptions, column.getTopCardId());
                addExchangeCard(column.getBottomCard(), openCard, exchangeCardOptions, column.getBottomCardId());
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
            if (column.getTopCard() == null) {
                column.setTopCard(card);
                return;
            }
            if (column.getBottomCard() == null) {
                column.setBottomCard(card);
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
            if (!column.getTopCard().isRevealed() && !column.getBottomCard().isRevealed()) {
                return Math.random() < 0.5 ? column.getTopCardId() : column.getBottomCardId();
            }
        }
        for (BibergangCardColumn column : columns) {
            if (!column.getTopCard().isRevealed()) {
                return column.getTopCardId();
            }
            if (!column.getBottomCard().isRevealed()) {
                return column.getBottomCardId();
            }
        }
        throw new IllegalStateException("id is still null!");
    }

    private List<BibergangCardColumn> randomColumns() {
        List<BibergangCardColumn> list = Arrays.asList(columns);
        Collections.shuffle(list);
        return list;
    }

    public Stream<BibergangCard> columnCardStream() {
        BibergangCard[] cards = new BibergangCard[amountCards()];
        for (int i = 0; i < BibergangGame.AMOUNT_CARD_COLUMNS; i++) {
            BibergangCardColumn column = columns[i];
            cards[2 * i] = column.getTopCard();
            cards[2 * i + 1] = column.getBottomCard();
        }
        return Arrays.stream(cards);
    }

    public void revealStartCards() {
        columns[0].getTopCard().reveal();
        columns[BibergangGame.AMOUNT_CARD_COLUMNS - 1].getBottomCard().reveal();
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
                if (column.getTopCard().isRevealed()) {
                    total = total + column.getTopCard().getValue();
                }
                if (column.getBottomCard().isRevealed()) {
                    total = total + column.getBottomCard().getValue();
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
        for(BibergangCardColumn column: getColumnsWithoutPairs()) {
            //first prio: biber to high value card
            if (column.getTopCard().isRevealed() && !column.getTopCard().isBiber()) {
                list.add(new ExchangeCardOption(column.getTopCardId(), column.getTopCard().getValue(), column.getTopCard()));
            }
            if (column.getBottomCard().isRevealed() && !column.getBottomCard().isBiber()) {
                list.add(new ExchangeCardOption(column.getTopCardId(), column.getBottomCard().getValue(), column.getBottomCard()));
            }
            if (!column.getTopCard().isRevealed() && !column.getBottomCard().isRevealed()) {
                list.add(new ExchangeCardOption(column.getTopCardId(), AiSettings.EXCHANGE_CARD_DIFF_THRESHOLD, column.getTopCard()));
                list.add(new ExchangeCardOption(column.getTopCardId(), AiSettings.EXCHANGE_CARD_DIFF_THRESHOLD, column.getTopCard()));
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
            if (!column.getTopCard().isRevealed()) {
                return column.getTopCardId();
            }
            if (!column.getBottomCard().isRevealed()) {
                return column.getBottomCardId();
            }
        }
        throw new IllegalStateException("requesting for unrevealed slot even though all cards are revealed");
    }

    public BibergangCard moveBiber(BibergangCard biber) {
        List<ExchangeCardOption> options = new ArrayList<>();
        for (BibergangCardColumn column : columns) {
            if (!column.getTopCard().isRevealed()) {
                options.add(new ExchangeCardOption(column.getTopCardId(), column.getBottomCard().getValue(), biber));
            }
            if (!column.getBottomCard().isRevealed()) {
                options.add(new ExchangeCardOption(column.getBottomCardId(), column.getTopCard().getValue(), biber));
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
                total = total + column.getTopCard().getValue();
                total = total + column.getBottomCard().getValue();
            }
        }
        return total;
    }
}
