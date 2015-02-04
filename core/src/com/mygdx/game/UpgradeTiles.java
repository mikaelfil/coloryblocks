package com.mygdx.game;

public class UpgradeTiles {
    private final int x;
    private final int y;
    private final int value;

    public UpgradeTiles(final int x, final int y, final int value){
        this.x = x;
        this.y = y;
        this.value = value;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getValue() {
        return value;
    }
}
