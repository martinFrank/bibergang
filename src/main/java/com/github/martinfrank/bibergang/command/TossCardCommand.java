package com.github.martinfrank.bibergang.command;

import com.github.martinfrank.bibergang.BibergangBoard;
import com.github.martinfrank.bibergang.BibergangCard;
import com.github.martinfrank.cli.Command;
import com.github.martinfrank.cli.Response;

import java.util.List;

public class TossCardCommand extends Command<BibergangBoard>  {

    public TossCardCommand(BibergangBoard board) {
        super(board, "toss");
    }

    @Override
    public Response execute(List<String> parameter) {
        if (parameter.isEmpty() && getApplication().getCurrentPlayer().hasOptionFour()) {
            getApplication().tossCard();
            getApplication().endPlayersTurn();
            return Response.success();
        }

        if(!getApplication().getCurrentPlayer().hasOptionFour() && (parameter.size() != 1)){
            return Response.fail("you must provide an unrevealad card id!");
        }

        BibergangCard card = getApplication().getCurrentPlayer().getCardById(parameter.get(0));
        if (card == null || card.isRevealed()){
           return Response.fail("you must provide an unrevealad card id!");
        }
        card.reveal();
        getApplication().tossCard();
        getApplication().endPlayersTurn();
        return Response.success();
    }
}
