package com.github.martinfrank.bibergang.command;

import com.github.martinfrank.bibergang.BibergangBoard;
import com.github.martinfrank.cli.Command;
import com.github.martinfrank.cli.Response;

import java.util.List;

public class ShowBoardCommand extends Command<BibergangBoard> {

    public ShowBoardCommand(BibergangBoard board) {
        super(board, "show");
    }

    void printGame(BibergangBoard board) {
        getApplication().getPrinter().printGame(board);//NOSONAR - it's a console app
    }

    @Override
    public Response execute(List<String> list) {
        printGame(getApplication());
        return Response.success();
    }
}
