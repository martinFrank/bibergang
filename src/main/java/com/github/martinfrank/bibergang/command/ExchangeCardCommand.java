package com.github.martinfrank.bibergang.command;

import com.github.martinfrank.bibergang.BibergangBoard;
import com.github.martinfrank.bibergang.BibergangCard;
import com.github.martinfrank.bibergang.BibergangGamePrinter;
import com.github.martinfrank.cli.Command;
import com.github.martinfrank.cli.Response;

import java.util.List;

public class ExchangeCardCommand extends Command<BibergangBoard>  {

    public ExchangeCardCommand(BibergangBoard board) {
        super(board, "exchange");
    }

    @Override
    public Response execute(List<String> parameter) {
        if(parameter.size() != 1){
            return Response.fail("you must provide a valid card id");
        }

        BibergangCard card = getApplication().getCurrentPlayer().getCardById(parameter.get(0));
        if(card == null){
            return Response.fail("you must provide a valid card id");
        }

        getApplication().exchangeCard(parameter.get(0));
        getApplication().tossCard();
        getApplication().endPlayersTurn();
//        getApplication().getPrinter().printGame(System.out, getApplication());
        return Response.success();
    }
}
