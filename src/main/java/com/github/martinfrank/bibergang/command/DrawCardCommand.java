package com.github.martinfrank.bibergang.command;

import com.github.martinfrank.bibergang.BibergangBoard;
import com.github.martinfrank.bibergang.BibergangGamePrinter;
import com.github.martinfrank.cli.Command;
import com.github.martinfrank.cli.Response;

import java.util.List;

public class DrawCardCommand extends Command<BibergangBoard>  {

    public DrawCardCommand(BibergangBoard board) {
        super(board, "draw");
    }

    @Override
    public Response execute(List<String> parameter) {
        if(parameter.size() == 1){
            return handleInput(parameter.get(0));
        }
        return Response.fail("you must either DRAW OPEN or DRAW CLOSED to choose the proper stack");
    }

    private Response handleInput(String parameter) {
        if("open".equalsIgnoreCase(parameter)){
            return drawOpen();
        }
        if("closed".equalsIgnoreCase(parameter)){
            return drawClosed();
        }
        return Response.fail("you must either DRAW OPEN or DRAW CLOSED to choose the proper stack");
    }

    private Response drawClosed() {
        getApplication().drawCurrentCardFromClosed();
        getApplication().getPrinter().printGame(System.out, getApplication());
        return Response.success();
    }

    private Response drawOpen() {
        getApplication().drawCurrentCardFromOpen();
        getApplication().getPrinter().printGame(System.out, getApplication());
        return Response.success();
    }
}
