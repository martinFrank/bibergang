package com.github.martinfrank.bibergang.command;

import com.github.martinfrank.bibergang.BibergangBoard;
import com.github.martinfrank.bibergang.BibergangGame;
import com.github.martinfrank.bibergang.BibergangGameSetup;
import com.github.martinfrank.cli.Command;
import com.github.martinfrank.cli.Response;

import java.util.List;

public class RestartCommand extends Command<BibergangBoard> {

    public RestartCommand(BibergangBoard board) {
        super(board, "restart");
    }

    @Override
    public Response execute(List<String> list) {
        try {
            BibergangBoard board = getApplication();
            int amountPlayers = getAmountPlayers(list);
            board.setup(new BibergangGameSetup(amountPlayers));
//            getApplication().getPrinter().printGame(getApplication());
            board.startPlayersTurn();
            return Response.success();
        } catch (IllegalArgumentException e) { //also handles NumberformatException
            return Response.fail("restart command requires amount of players (2.."+BibergangGame.MAX_AMOUNT_PLAYER+") as parameter!");
        }
    }

    private int getAmountPlayers(List<String> list) {
        if (list.isEmpty()) {
            return BibergangGame.MAX_AMOUNT_PLAYER;
        }
        int amount = Integer.parseInt(list.get(0));
        if(amount < 2 || amount > BibergangGame.MAX_AMOUNT_PLAYER){
            throw new IllegalArgumentException();
        }
        return amount;
    }

}
