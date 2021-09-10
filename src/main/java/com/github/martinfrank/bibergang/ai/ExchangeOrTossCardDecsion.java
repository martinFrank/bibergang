package com.github.martinfrank.bibergang.ai;

public class ExchangeOrTossCardDecsion {

    public enum DecisionType {EXCHANGE, TOSS}

    public final DecisionType type;
    private final String exchangingId;

    private ExchangeOrTossCardDecsion(DecisionType type, String exchangingId) {
        this.type = type;
        this.exchangingId = exchangingId;
    }

    public static ExchangeOrTossCardDecsion exchange(String exchangingId) {
        return new ExchangeOrTossCardDecsion(DecisionType.EXCHANGE, exchangingId);
    }

    public static ExchangeOrTossCardDecsion toss(String exchangingId) {
        return new ExchangeOrTossCardDecsion(DecisionType.TOSS, exchangingId);
    }

    public String getExchangeId() {
        return exchangingId;
    }
}
