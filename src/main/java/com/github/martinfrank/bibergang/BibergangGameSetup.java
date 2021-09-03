package com.github.martinfrank.bibergang;

import com.github.martinfrank.boardgamelib.BoardGameSetup;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class BibergangGameSetup implements BoardGameSetup<BibergangPlayer>  {

    private final int amountPlayers;

    public BibergangGameSetup(int amountPlayers) {
        this.amountPlayers = amountPlayers;
    }

    @Override
    public List<BibergangPlayer> getPlayers() {
        ArrayList<BibergangPlayer> player = new ArrayList<>();
        player.add(new BibergangPlayer("YOU", 0xFFFF00, true));
        for(int i = 1; i < amountPlayers; i ++) {
//            player.add(new BibergangPlayer("CPU"+i, 0x0000FF, false));
            player.add(new BibergangPlayer("CPU"+i, 0x0000FF, false));
        }
        return player;
    }

    @Override
    public int getMaximumRounds() {
        return 0;
    }
}
