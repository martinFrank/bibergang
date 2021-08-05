package com.github.martinfrank.bibergang.command;

import com.github.martinfrank.cli.Command;
import com.github.martinfrank.cli.Response;
import com.github.martinfrank.bibergang.BibergangBoard;
import com.github.martinfrank.bibergang.BibergangGamePrinter;

import java.io.PrintStream;
import java.util.List;

public class ShowResultCommand extends Command<BibergangBoard> {

    public ShowResultCommand(BibergangBoard board) {
        super(board, "result");
    }

    @Override
    public Response execute(List<String> list) {
        return Response.success();
    }
}
