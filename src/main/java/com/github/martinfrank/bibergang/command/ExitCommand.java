package com.github.martinfrank.bibergang.command;

import com.github.martinfrank.cli.Command;
import com.github.martinfrank.cli.Response;
import com.github.martinfrank.bibergang.BibergangBoard;

import java.util.List;

public class ExitCommand extends Command<BibergangBoard> {

    public ExitCommand(BibergangBoard board) {
        super(board, "exit");
    }

    @Override
    public Response execute(List<String> parameter) {
        getApplication().getCommandInterpreterProvider().getCommandInterpreter().stop();
        return Response.success();
    }
}
