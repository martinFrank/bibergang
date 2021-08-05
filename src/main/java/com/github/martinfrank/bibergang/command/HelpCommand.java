package com.github.martinfrank.bibergang.command;

import com.github.martinfrank.cli.Command;
import com.github.martinfrank.cli.Response;
import com.github.martinfrank.bibergang.BibergangBoard;

import java.util.List;

public class HelpCommand extends Command<BibergangBoard> {

    public HelpCommand(BibergangBoard board) {
        super(board, "help");
    }

    @Override
    public Response execute(List<String> parameter) {
        System.out.println("you can EXIT the game, or see your OPTIONS for a list of all commands");
        return Response.success();
    }
}
