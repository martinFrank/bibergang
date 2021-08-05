package com.github.martinfrank.bibergang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class BibergangCardStack {

    private final List<BibergangCard> stack = new ArrayList<>();

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public int size() {
        return stack.size();
    }

    public void addOnTop(BibergangCard card) {
        stack.add(card);
    }

    public void clear() {
        stack.clear();
    }

    public void shuffle() {
        Collections.shuffle(stack);
    }

    public void addAll(BibergangCardStack other) {
        stack.addAll(other.stack);
    }


    @Override
    public String toString() {
        return stack.toString();
    }


    public List<BibergangCard> getCards() {
        return stack;
    }

    public BibergangCard getTopCard() {
        if(stack.size() == 0){
            return null;
        }
        return stack.get(stack.size()-1);
    }

    public void newBiberCardDeck() {
        stack.clear();
        IntStream.range(0, 104).forEach(value -> stack.add(BibergangCard.newValueCard(value%13)));
        IntStream.range(0,4).forEach(v -> stack.add(BibergangCard.newBiberCard()));
        Collections.shuffle(stack);
    }

    public BibergangCard drawTopCard() {
        return stack.remove(stack.size()-1);
    }
}
