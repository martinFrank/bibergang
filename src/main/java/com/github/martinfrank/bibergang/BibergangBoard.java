package com.github.martinfrank.bibergang;

import com.github.martinfrank.boardgamelib.BaseBoardGame;
import com.github.martinfrank.boardgamelib.BoardGameSetup;
import com.github.martinfrank.cli.CommandInterpreterProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

public class BibergangBoard extends BaseBoardGame<BibergangPlayer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BibergangGame.class);
    private final CommandInterpreterProvider commandInterpreterProvider;
    private final BibergangCardStack closedStack = new BibergangCardStack();
    private final BibergangCardStack openStack = new BibergangCardStack();
    private BibergangCard currentPlayersDrawnCard = null;
    private final BibergangGamePrinter printer = new BibergangGamePrinter();

    public BibergangBoard(CommandInterpreterProvider commandInterpreterProvider) {
        this.commandInterpreterProvider = commandInterpreterProvider;
    }

    @Override
    public void endPlayersTurn() {
        if (getPlayers().stream().anyMatch(BibergangPlayer::hasKnocked)) {
            getCurrentPlayer().setLastTurnPlayed();
            getCurrentPlayer().revealAll();
        }
        super.endPlayersTurn();//switches to next Player
        startPlayersTurn();
    }

    @Override
    public void setup(BoardGameSetup<BibergangPlayer> setup) {
        super.setup(setup);
        initGame();
//        startPlayersTurn();
    }

    @Override
    public void initGame() {
        super.initGame();
        LOGGER.debug("init Game");
        openStack.clear();
        closedStack.newBiberCardDeck();
        addCardsToPlayers();
        BibergangCard drawnCards = closedStack.drawTopCard();
        drawnCards.reveal();
        openStack.addOnTop(drawnCards);
        getPlayers().forEach(BibergangPlayer::revealStartCards);
//        startPlayersTurn();
//        printer.printGame(System.out, this);
    }

    private void addCardsToPlayers() {
//        IntStream.range(0, BibergangGame.AMOUNT_CARD_COLUMNS * 2).
//                forEach(i -> getPlayers().forEach(p -> p.addStartCard(closedStack.drawTopCard())));

                IntStream.range(0, BibergangGame.AMOUNT_CARD_COLUMNS * 2).
                forEach(i -> getPlayers().forEach(p -> p.cols.addStartCard(closedStack.drawTopCard())));
    }

    @Override
    public void startPlayersTurn() {
        super.startPlayersTurn();
        if (haveAllFinishLastTurn()) {
            System.out.println("GAME OVER!!!");
//            printer.printGame(System.out, this);
            return;
        }
        getCurrentPlayer().resetDrawn();
        printer.printGame(System.out, this);
        if (getPlayers().stream().anyMatch(BibergangPlayer::hasKnocked)) {
            System.out.println("WARNING - last round!");
        }
        if (getCurrentPlayer().isAi()) {
            getCurrentPlayer().performAiTurn();
        }else {
//        LOGGER.debug("start Players turn");
        }
    }

    /*visible for testing*/
    boolean haveAllFinishLastTurn() {
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
            System.out.println("I KNOCKED!");
            getCurrentPlayer().setLastTurnPlayed();
        }
    }


    public BibergangGamePrinter getPrinter() {
        return printer;
    }
}
