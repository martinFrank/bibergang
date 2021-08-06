package com.github.martinfrank.bibergang;

import com.github.martinfrank.bibergang.command.*;
import com.github.martinfrank.cli.CommandList;
import com.github.martinfrank.cli.CommandProvider;
import com.github.martinfrank.cli.DefaultCommandList;

public class BibergangGameCommandProvider implements CommandProvider {

    private final BibergangBoard board;
//    private final EndTurnCommand endTurnCommand;
    private final ShowBoardCommand showBoardCommand;
    private final RestartCommand restartCommand;
    private final OptionCommand optionCommand;
    private final HelpCommand helpCommand;
    private final ExitCommand exitCommand;
    private final DrawCardCommand drawCardCommand;
    private final TossCardCommand tossCardCommand;
    private final ExchangeCardCommand exchangeCommand;

    BibergangGameCommandProvider(BibergangBoard board) {
        super();
        this.board = board;
//        endTurnCommand = new EndTurnCommand(board);
        showBoardCommand = new ShowBoardCommand(board);
        restartCommand = new RestartCommand(board);
        helpCommand = new HelpCommand(board);
        exitCommand = new ExitCommand(board);
        optionCommand = new OptionCommand(board);
        drawCardCommand = new DrawCardCommand(board);
        tossCardCommand = new TossCardCommand(board);
        exchangeCommand = new ExchangeCardCommand(board);
    }

    @Override
    public CommandList getCommands() {
        final DefaultCommandList commandMapping = new DefaultCommandList();
        commandMapping.add(showBoardCommand);
        commandMapping.add(restartCommand);
        commandMapping.add(helpCommand);
        commandMapping.add(exitCommand);
        commandMapping.add(optionCommand);
        if(!board.getCurrentPlayer().hasDrawn() ){
            commandMapping.add(drawCardCommand);
        }
        if(board.getCurrentDrawnCard() != null){
            commandMapping.add(tossCardCommand);
            commandMapping.add(exchangeCommand);
        }
//        if(board.getCurrentDrawnCard() == null && board.getCurrentPlayer().hasDrawn()){
//            commandMapping.add(endTurnCommand);
//        }
        return commandMapping;
    }
}
