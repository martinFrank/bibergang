package com.github.martinfrank.bibergang;

import java.util.ArrayList;
import java.util.List;

public class BibergangGameSetup {

    public final int amountPlayers;

    public BibergangGameSetup(int amountPlayers) {
        this.amountPlayers = amountPlayers;
    }

    public List<BibergangPlayer> getPlayers() {
        ArrayList<BibergangPlayer> player = new ArrayList<>();
        player.add(new BibergangPlayer("YOU", 0xFFFF00, true));
        for (int i = 1; i < amountPlayers; i++) {
            player.add(new BibergangPlayer("CPU" + i, 0x0000FF, false));
        }
        return player;
    }

}
