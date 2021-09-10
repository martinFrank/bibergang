package com.github.martinfrank.bibergang;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class BibergangGamePrinter {

    private static final int PRINT_CARD_SIZE = 3;

    private final PrintStream stream;

    public BibergangGamePrinter(PrintStream stream) {
        this.stream = stream;
    }


    public void printGame(BibergangBoard board) {
        if (board.haveAllFinishLastTurn()) {
            printEndGame(board);
        } else {
            printInGame(board);
        }
    }

    private void printInGame(BibergangBoard board) {
        stream.println();
        printPlayer(board);
        List<String> deckLines = getDeck(board);
        deckLines.forEach(stream::println);
    }

    private void printPlayer(BibergangBoard board) {
        List<String> playerLines = new ArrayList<>();
        for (BibergangPlayer player : board.getPlayers()) {
            List<String> lines = getPlayerLines(player);
            for (int i = 0; i < lines.size(); i++) {
                if (playerLines.size() == i){
                    playerLines.add(lines.get(i));
                }else{
                    playerLines.set(i, playerLines.get(i)+" "+lines.get(i));
                }
            }
        }
        playerLines.forEach(stream::println);
    }

    private List<String> getDeck(BibergangBoard board) {
        List<String> lines = new ArrayList<>();
        lines.add("┌─────────────────────────┐");
        String cardCounterLine = "│ Closed: " + left("" + board.getClosedStack().size(), 3) +
                "   Open: " + left("" + board.getOpenStack().size(), 3) + " │";
        lines.add(cardCounterLine);
        lines.add("├─────────────────────────┤");
        lines.add("│ ┌C──┐   ┌O──┐     ┌D──┐ │");
        String cardValues = "│ │ ? │   │" + center("" + cardOptional(board.getOpenStack().getTopCard())) +
                "│     │" + center(cardOptional(board.getCurrentDrawnCard())) + "│ │";
        lines.add(cardValues);
        lines.add("│ └───┘   └───┘     └───┘ │");
        lines.add("├─────────────────────────┤");
        lines.add("│ Player: " + left(board.getCurrentPlayer().getName(), 15) + " │");
        lines.add("└─────────────────────────┘");
        return lines;

    }

    private String threeDigit(String value) {
        return center(value);
    }


    private String center(String content) {
        int remaining = PRINT_CARD_SIZE - content.length();
        StringBuilder result = new StringBuilder(content);
        for (int i = 0; i < remaining; i++) {
            if (i % 2 == 0) {
                result.append(" ");
            } else {
                result.insert(0, " ");
            }
        }
        return result.toString();
    }

    private String cardOptional(BibergangCard card){
        if (card == null) {
            return "-";
        }
        else{
            return card.getDisplay();
        }
    }

    private List<String> cardSlot(String index, String display){
        if (index.length() != 1){
            throw new IllegalArgumentException("index.length must be 1");
        }
        if (display.length() != 3){
            throw new IllegalArgumentException("display.length must be 3");
        }
        List<String> lines = new ArrayList<>();
        lines.add("┌"+index+"──┐");
        lines.add("│"+display+"│");
        lines.add("└───┘");
        return lines;
    }

    private List<String> getPlayerLines(BibergangPlayer player){
        List<String> lines = new ArrayList<>();
        int innerLineLength = 6 * BibergangGame.AMOUNT_CARD_COLUMNS + 1;
        lines.add("┌" + fill("─", innerLineLength) + "┐");
        lines.add("│ Player: " + left(player.getName(), innerLineLength - 10) + " │");
        lines.add("│ Points: " + left("" + player.getTotalOfVisibleScore(), innerLineLength - 10) + " │");
        lines.add("├" + fill("─", innerLineLength) + "┤");
        StringBuilder topTopCardLine = new StringBuilder("│ ");
        StringBuilder topMiddleCardLine = new StringBuilder("│ ");
        StringBuilder topBottomCardLine = new StringBuilder("│ ");
        for (int index = 0; index < BibergangGame.AMOUNT_CARD_COLUMNS; index++) {
            String id = BibergangCard.mapIndex(index * 2);
            List<String> cardSlot = cardSlot(id, threeDigit(player.getCardById(id).getDisplay()));
            topTopCardLine.append(cardSlot.get(0)).append(" ");
            topMiddleCardLine.append(cardSlot.get(1)).append(" ");
            topBottomCardLine.append(cardSlot.get(2)).append(" ");
        }
        topTopCardLine.append("│");
        topMiddleCardLine.append("│");
        topBottomCardLine.append("│");
        lines.add(topTopCardLine.toString());
        lines.add(topMiddleCardLine.toString());
        lines.add(topBottomCardLine.toString());


        StringBuilder bottomTopCardLine = new StringBuilder("│ ");
        StringBuilder bottomMiddleCardLine = new StringBuilder("│ ");
        StringBuilder bottomBottomCardLine = new StringBuilder("│ ");
        for (int index = 0; index < BibergangGame.AMOUNT_CARD_COLUMNS; index++) {
            String id = BibergangCard.mapIndex(index * 2 + 1);
            List<String> cardSlot = cardSlot(id, threeDigit(player.getCardById(id).getDisplay()));
            bottomTopCardLine.append(cardSlot.get(0)).append(" ");
            bottomMiddleCardLine.append(cardSlot.get(1)).append(" ");
            bottomBottomCardLine.append(cardSlot.get(2)).append(" ");
        }
        bottomTopCardLine.append("│");
        bottomMiddleCardLine.append("│");
        bottomBottomCardLine.append("│");
        lines.add(bottomTopCardLine.toString());
        lines.add(bottomMiddleCardLine.toString());
        lines.add(bottomBottomCardLine.toString());

        lines.add("└" + fill("─", innerLineLength) + "┘");
        return lines;
    }

    private String left(String content, int length) {
        if (content.length() > length){
            content = content.substring(0,length-1);
        }
        StringBuilder stringBuilder = new StringBuilder(content);
        while(stringBuilder.length() < length){
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    private String fill(String symbol, int length) {
        StringBuilder stringBuilder = new StringBuilder();
        IntStream.range(0,length).forEach(i -> stringBuilder.append(symbol));
        return stringBuilder.toString();
    }

    private void printEndGame(BibergangBoard board) {
        stream.println();
        printPlayer(board);
        StringBuilder topLine = new StringBuilder("┌───────");
        StringBuilder scoreLine = new StringBuilder("│ Score ");
        StringBuilder bottomLine = new StringBuilder("└───────");
        for (BibergangPlayer player: board.getPlayers()){
            topLine.append("┬─────────");
            topLine.append(fill("─", player.getName().length()));
            topLine.append("────────");
            topLine.append(fill("─", (""+player.getTotalScore()).length()+4));
            scoreLine.append("│ Player ");
            scoreLine.append(player.getName());
            scoreLine.append(" has ");
            scoreLine.append(player.getTotalScore() );
            scoreLine.append(" points ");
            bottomLine.append("┴─────────");
            bottomLine.append(fill("─", player.getName().length()));
            bottomLine.append("────────");
            bottomLine.append(fill("─", ("" + player.getTotalScore()).length() + 4));
        }
        topLine.append("┐");
        scoreLine.append("│");
        bottomLine.append("┘");

        stream.println(topLine);
        stream.println(scoreLine);
        stream.println(bottomLine);
    }
}
