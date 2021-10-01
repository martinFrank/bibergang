package com.github.martinfrank.bibergang;

class App {

    public static void main(String[] args){
        BibergangGame bibergangGame = new BibergangGame();
        bibergangGame.getBoard().startPlayersTurn();
        bibergangGame.getCommandInterpreter().start();

        //Test comment
    }

}
