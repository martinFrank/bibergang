package com.github.martinfrank.bibergang;

import com.github.martinfrank.boardgamelib.BaseBoardGame;
import com.github.martinfrank.cli.CommandInterpreterProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;
import java.util.stream.IntStream;

public class BibergangBoard extends BaseBoardGame<BibergangPlayer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BibergangGame.class);
    private final CommandInterpreterProvider commandInterpreterProvider;
    private final BibergangCardStack closedStack = new BibergangCardStack();
    private final BibergangCardStack openStack = new BibergangCardStack();
    private BibergangCard currentPlayersDrawnCard = null;

    public BibergangBoard(CommandInterpreterProvider commandInterpreterProvider) {
        this.commandInterpreterProvider = commandInterpreterProvider;
    }

    @Override
    public void endPlayersTurn() {
        super.endPlayersTurn();
        //FIXME check if all have (been) 'knocked'
        if(getPlayers().stream().anyMatch(BibergangPlayer::hasLastTurnPlayed)){
            getCurrentPlayer().setLastTurnPlayed();
            getCurrentPlayer().revealAll();
        }
        startPlayersTurn();
    }

    @Override
    public void initGame() {
        super.initGame();
        LOGGER.debug("init Game");
        openStack.clear();
        closedStack.newBiberCardDeck();
        IntStream.range(0,BibergangGame.AMOUNT_CARD_COLUMNS *2).
                forEach(i -> getPlayers().forEach(p -> p.addStartCard(closedStack.drawTopCard())) );
        BibergangCard drawnCards = closedStack.drawTopCard();
        drawnCards.reveal();
        openStack.addOnTop(drawnCards);
        getPlayers().forEach(BibergangPlayer::revealStartCards);
        startPlayersTurn();
    }

    @Override
    public void startPlayersTurn() {
        super.startPlayersTurn();
        if(haveAllFinishLastTurn()){
            System.out.println("GAME OVER!!!");
            //FIXME stop game here!
            return;
        }
        getCurrentPlayer().resetDrawn();

        if (getPlayers().stream().anyMatch(BibergangPlayer::hasKnocked) ){
            System.out.println("WARNING - last round!");
        }
        LOGGER.debug("start Players turn");
    }

    private boolean haveAllFinishLastTurn() {
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
    }

    public BibergangCard getCurrentDrawnCard() {
        return currentPlayersDrawnCard;
    }

    public void exchangeCard(String id) {
        BibergangCard from = getCurrentPlayer().getCardById(id);
        getCurrentPlayer().setCardById(currentPlayersDrawnCard, id);
        if(from.isBiber() && !getCurrentPlayer().hasKnocked()) {
            from = getCurrentPlayer().moveBiber(from);
        }
        currentPlayersDrawnCard = from;
        currentPlayersDrawnCard.reveal();
        if(getCurrentPlayer().hasKnocked() ){
            System.out.println("I KNOCKED!");
            getCurrentPlayer().setLastTurnPlayed();
        }
    }

}
