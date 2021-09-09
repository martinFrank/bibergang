package com.github.martinfrank.bibergang;

import java.util.ArrayList;
import java.util.List;

public class BibergangGameSetup {//implements BoardGameSetup<BibergangPlayer>  {

    private final int amountPlayers = 2;

    public BibergangGameSetup() {
//    public BibergangGameSetup(int amountPlayers) {
//        this.amountPlayers = amountPlayers;
    }

    //    @Override
    public List<BibergangPlayer> getPlayers() {
        ArrayList<BibergangPlayer> player = new ArrayList<>();
        player.add(new BibergangPlayer("YOU", 0xFFFF00, true));
        for (int i = 1; i < amountPlayers; i++) {
//            player.add(new BibergangPlayer("CPU"+i, 0x0000FF, false));
            player.add(new BibergangPlayer("CPU" + i, 0x0000FF, false));
        }
        return player;
    }

//    @Override
//    public int getMaximumRounds() {
//        return 0;
//    }
}
