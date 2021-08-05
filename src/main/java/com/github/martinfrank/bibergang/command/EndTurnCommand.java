package com.github.martinfrank.bibergang.command;

import com.github.martinfrank.bibergang.BibergangGamePrinter;
import com.github.martinfrank.cli.Command;
import com.github.martinfrank.cli.Response;
import com.github.martinfrank.bibergang.BibergangBoard;
import com.github.martinfrank.bibergang.BibergangPlayer;

import java.util.List;

public class EndTurnCommand extends Command<BibergangBoard> {

    public EndTurnCommand(BibergangBoard board) {
        super(board, "done");
    }

    @Override
    public Response execute(List<String> list) {
        BibergangBoard board = getApplication();
        board.endPlayersTurn();
        BibergangGamePrinter.printGame(System.out, getApplication());
        return Response.success();
    }
}
