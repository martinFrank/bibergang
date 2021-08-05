package com.github.martinfrank.bibergang;

public class BibergangCard {

    private final Integer value;
    private final int number;
    private final CardType type;
    private boolean isRevealed;
    private static int cardNumber = 0;


    public static BibergangCard newValueCard(Integer value) {
        return new BibergangCard(CardType.VALUE, value);
    }

    public static BibergangCard newBiberCard() {
        return new BibergangCard(CardType.BIBER, 0);
    }

    private BibergangCard(CardType type, Integer value) {
        this.value = value;
        this.type = type;
        isRevealed = false;
        number = cardNumber;
        cardNumber++;
    }

    public static String mapIndex(int index) {
        return ""+((char)('A'+index));
    }

    public void reveal() {
        isRevealed = true;
    }


    @Override
    public String toString() {
        return "value: " + value + ", Type " + type + ", card#:" + number;
    }

    public int getValue() {
        return value;
    }

    public String getDisplay() {
        if (isRevealed) {
            if (type == CardType.BIBER) {
                return "*B*";
            }
            return "" + value;
        } else {
            return "?";
        }
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public boolean isBiber() {
        return type == CardType.BIBER;
    }

    public enum CardType {VALUE, BIBER}
}
