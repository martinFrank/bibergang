package com.github.martinfrank.bibergang;

class App {

    public static void main(String[] args){
        BibergangGame bibergangGame = new BibergangGame();
        bibergangGame.getCommandInterpreter().start();
    }

}
