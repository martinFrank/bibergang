package com.github.martinfrank.bibergang;

import com.github.martinfrank.cli.CommandInterpreter;
import com.github.martinfrank.cli.CommandInterpreterProvider;
import com.github.martinfrank.cli.CommandList;
import com.github.martinfrank.cli.CommandProvider;

public class BibergangGame implements CommandProvider, CommandInterpreterProvider {

    public static final int MAX_AMOUNT_PLAYER = 4;
    public static final int AMOUNT_CARD_COLUMNS = 4;

    private final BibergangGameCommandProvider bibergangGameCommandProvider;
    private final CommandInterpreter commandInterpreter;
    private BibergangBoard board;

    BibergangGame() {
        board = new BibergangBoard(this);
        bibergangGameCommandProvider = new BibergangGameCommandProvider(board);
        commandInterpreter = new CommandInterpreter(bibergangGameCommandProvider);

        board.setup(new BibergangGameSetup(2));
//        board.initGame();
    }



    public BibergangBoard getBoard() {
        return board;
    }

    @Override
    public CommandList getCommands() {
        return bibergangGameCommandProvider.getCommands();
    }

    @Override
    public CommandInterpreter getCommandInterpreter() {
        return commandInterpreter;
    }
}