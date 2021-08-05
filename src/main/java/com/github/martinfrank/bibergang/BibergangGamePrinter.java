package com.github.martinfrank.bibergang;

import java.io.PrintStream;

public class BibergangGamePrinter {

    private BibergangGamePrinter() {

    }

    public static void printGame(PrintStream out, BibergangBoard board) {
        out.println();
//        printDeck(out, board);
        for(BibergangPlayer player: board.getPlayers()){
            printPlayer(out, player);
        }
        printDeck(out, board);
    }

    private static void printPlayer(PrintStream out, BibergangPlayer player) {
        out.println();
        out.println("+----------+---------------------------------+");
        out.println("| Spieler: | "+twelveDigit(player.getName())+"                    |"); //FIXME score
        out.println("+----------+---------------------------------+");
        StringBuilder topRowBuilder = new StringBuilder();
        StringBuilder separatorBuilder = new StringBuilder();
        StringBuilder bottomRowBuilder = new StringBuilder();
        for (int index = 0; index < BibergangGame.AMOUNT_CARD_COLUMNS; index ++){
            String topId = BibergangCard.mapIndex(index*2);
            String bottomId = BibergangCard.mapIndex(index*2+1);
            topRowBuilder.append("| Index").append(threeDigit(""+topId)).append(":").append(threeDigit(player.getCardById(topId).getDisplay())).append(" ");
            bottomRowBuilder.append("| Index").append(threeDigit(""+bottomId)).append(":").append(threeDigit(player.getCardById(bottomId).getDisplay())).append(" ");
            separatorBuilder.append("+--------------");
        }
        out.println(separatorBuilder+"+");
        out.println(topRowBuilder+"|");
        out.println(separatorBuilder+"+");
        out.println(bottomRowBuilder+"|");
        out.println(separatorBuilder+"+");
    }


    private static void printDeck(PrintStream out, BibergangBoard board) {
        out.println();
        out.println("+----------------------+--------------------+------------------------------------+");
        out.println("| Nachzieh-Stapel: "+threeDigit(""+board.getClosedStack().size())+
                " | Ablage-Stapel: "+threeDigit(""+cardOptional(board.getOpenStack().getTopCard()))+
                " | Wer ist gerade dran?: "+anyDigit(board.getCurrentPlayer().getName(), 5)+
                " | gezogene Karte: "+threeDigit(cardOptional(board.getCurrentDrawnCard())+" |"));
        out.println("+----------------------+--------------------+------------------------------------+");
    }

    private static String threeDigit(String value ){
        return anyDigit(value, 3);
    }

//    private static String threeDigit(int value ){
//        if ( value <= 9 ){
//            return " "+value+" ";
//        }
//        if ( value <= 99 ){
//            return " "+value;
//        }
//        return ""+value;
//    }

    private static String twelveDigit(String content) {
//        int remaining = 12 - content.length();
//        StringBuilder sb = new StringBuilder();
//        IntStream.range(0,remaining).forEach(i -> sb.append(" "));
//        return content+sb;
        return anyDigit(content,12);
    }

    private static String anyDigit(String content, int length) {
        int remaining = length - content.length();
        String result = content;
        for(int i = 0; i < remaining; i ++){
            if (i % 2 == 0){
                result = result + " ";
            }else{
                result = " " + result;
            }
        }
        return result;
    }

    private static String cardOptional(BibergangCard card){
        if (card == null) {
            return "-";
        }
        else{
            return card.getDisplay();
        }
    }

}
