package com.github.martinfrank.bibergang.command;

import com.github.martinfrank.cli.Command;
import com.github.martinfrank.cli.Response;
import com.github.martinfrank.bibergang.BibergangBoard;
import com.github.martinfrank.bibergang.BibergangGamePrinter;

import java.io.PrintStream;
import java.util.List;

public class ShowBoardCommand extends Command<BibergangBoard> {

    public ShowBoardCommand(BibergangBoard board) {
        super(board, "show");
    }

    void printGame(BibergangBoard board) {
        getApplication().getPrinter().printGame(new PrintStream(System.out), board);//NOSONAR - it's a console app
    }

    @Override
    public Response execute(List<String> list) {
        //FIXME print game or print endgame
        printGame(getApplication());
        return Response.success();
    }
}
