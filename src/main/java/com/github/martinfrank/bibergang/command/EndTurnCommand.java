package com.github.martinfrank.bibergang.command;

import com.github.martinfrank.bibergang.BibergangBoard;
import com.github.martinfrank.cli.Command;
import com.github.martinfrank.cli.Response;

import java.util.List;

public class EndTurnCommand extends Command<BibergangBoard> {

    public EndTurnCommand(BibergangBoard board) {
        super(board, "done");
    }

    @Override
    public Response execute(List<String> list) {
        BibergangBoard board = getApplication();
        board.endPlayersTurn();
        getApplication().getPrinter().printGame(getApplication());
        return Response.success();
    }
}
