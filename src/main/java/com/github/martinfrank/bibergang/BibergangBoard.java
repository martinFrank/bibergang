package com.github.martinfrank.bibergang;

import com.github.martinfrank.cli.CommandInterpreterProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class BibergangBoard {

    private final List<BibergangPlayer> players = new ArrayList<>();
    private int currentPlayerIndex = 0;

    private static final Logger LOGGER = LoggerFactory.getLogger(BibergangBoard.class);
    private final CommandInterpreterProvider commandInterpreterProvider;
    private final BibergangCardStack closedStack = new BibergangCardStack();
    private final BibergangCardStack openStack = new BibergangCardStack();
    private BibergangCard currentPlayersDrawnCard = null;
    private final BibergangGamePrinter printer = new BibergangGamePrinter(System.out);

    public BibergangBoard(CommandInterpreterProvider commandInterpreterProvider) {
        this.commandInterpreterProvider = commandInterpreterProvider;
    }

    public void endPlayersTurn() {
        if (getPlayers().stream().anyMatch(BibergangPlayer::hasKnocked)) {
            getCurrentPlayer().setLastTurnPlayed();
            getCurrentPlayer().revealAll();
        }

        //switch to next Player
        currentPlayerIndex = currentPlayerIndex + 1;
        if (currentPlayerIndex == players.size()) {
            currentPlayerIndex = 0;
        }
        startPlayersTurn();
    }

    public void setup(BibergangGameSetup setup) {
        players.clear();
        players.addAll(setup.getPlayers());
        players.forEach(player -> player.setGame(this));
        initGame();
    }

    public void initGame() {
        LOGGER.debug("init Game");
        openStack.clear();
        closedStack.newBiberCardDeck();
        addCardsToPlayers();
        BibergangCard drawnCards = closedStack.drawTopCard();
        drawnCards.reveal();
        openStack.addOnTop(drawnCards);
        getPlayers().forEach(BibergangPlayer::revealStartCards);
    }

    private void addCardsToPlayers() {
        IntStream.range(0, BibergangGame.AMOUNT_CARD_COLUMNS * 2).
                forEach(i -> getPlayers().forEach(p -> p.cols.addStartCard(closedStack.drawTopCard())));
    }

    public void startPlayersTurn() {
        if (haveAllFinishLastTurn()) {
            System.out.println("GAME OVER!!!");
            printer.printGame(this);
            return;
        }
        getCurrentPlayer().resetDrawn();
        printer.printGame(this);
        if (getPlayers().stream().anyMatch(BibergangPlayer::hasKnocked)) {
            System.out.println("WARNING - last round!");
        }
        if (getCurrentPlayer().isAi()) {
            getCurrentPlayer().performAiTurn();
        }
    }

    public boolean haveAllFinishLastTurn() {
        return getPlayers().stream().allMatch(BibergangPlayer::hasLastTurnPlayed);
    }

    BibergangCardStack getOpenStack() {
        return openStack;
    }

    BibergangCardStack getClosedStack() {
        return closedStack;
    }

    public CommandInterpreterProvider getCommandInterpreterProvider() {
        return commandInterpreterProvider;
    }

    public void drawCurrentCardFromClosed() {
        getCurrentPlayer().setDrawn();
        currentPlayersDrawnCard = closedStack.drawTopCard();
        currentPlayersDrawnCard.reveal();
    }

    public void drawCurrentCardFromOpen() {
        getCurrentPlayer().setDrawn();
        currentPlayersDrawnCard = openStack.drawTopCard();
        currentPlayersDrawnCard.reveal();
    }

    public void tossCard() {
        openStack.addOnTop(currentPlayersDrawnCard);
        currentPlayersDrawnCard = null;
        if (getCurrentPlayer().hasKnocked()) {
            System.out.println("I KNOCKED!");
            getCurrentPlayer().setLastTurnPlayed();
        }
    }

    public BibergangCard getCurrentDrawnCard() {
        return currentPlayersDrawnCard;
    }

    public void exchangeCard(String id) {
        BibergangCard from = getCurrentPlayer().getCardById(id);
        getCurrentPlayer().setCardById(currentPlayersDrawnCard, id);
        if (from.isBiber() && getCurrentPlayer().hasHiddenCards()) {
            from = getCurrentPlayer().moveBiber(from);
        }
        currentPlayersDrawnCard = from;
        currentPlayersDrawnCard.reveal();
        if (getCurrentPlayer().hasKnocked()) {
            getCurrentPlayer().setLastTurnPlayed();
        }
    }


    public BibergangGamePrinter getPrinter() {
        return printer;
    }

    public List<BibergangPlayer> getPlayers() {
        return players;
    }

    public BibergangPlayer getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }
}
