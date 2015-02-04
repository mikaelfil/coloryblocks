package com.mygdx.game;

public class RemoveTiles {

    final int coordinateX;
    final int coordinateY;

    public RemoveTiles(final int coordinateX, final int coordinateY){
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    public int getX(){
        return this.coordinateX;
    }

    public int getY(){
        return this.coordinateY;
    }
}
