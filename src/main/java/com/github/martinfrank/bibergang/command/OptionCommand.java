package com.github.martinfrank.bibergang.command;

import com.github.martinfrank.bibergang.BibergangBoard;
import com.github.martinfrank.bibergang.BibergangGame;
import com.github.martinfrank.cli.Command;
import com.github.martinfrank.cli.CommandList;
import com.github.martinfrank.cli.Response;

import java.util.List;

public class OptionCommand extends Command<BibergangBoard> {

    public OptionCommand(BibergangBoard board) {
        super(board, "options");
    }

    @Override
    public Response execute(List<String> parameter) {
        CommandList commands = ((BibergangGame)getApplication().getCommandInterpreterProvider()).getCommands();
        commands.asList().forEach(System.out::println);
        System.out.println("current Player is: "+getApplication().getCurrentPlayer().getName());
        return Response.success();
    }
}
