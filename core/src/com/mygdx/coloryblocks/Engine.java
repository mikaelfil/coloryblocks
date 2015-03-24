package com.mygdx.coloryblocks;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Engine {
    private static final int ROWS = 10;
    private static final int COLUMNS = 6;

    private final AssetProvider assetProvider;
    private final Grid grid;

    private boolean areWeMoving;

    public Engine(Preferences prefs) {
        this.assetProvider = new AssetProvider();
        this.grid = new Grid(this.assetProvider, prefs, COLUMNS, ROWS);

        areWeMoving = true;
    }

    void render(final SpriteBatch spriteBatch, BitmapFont font) {
        grid.render(spriteBatch, font);
    }

    public void commitCommand(int currentCommand) {
        if (currentCommand == 1 && areWeMoving == true){
            grid.spin();
        }
        if (currentCommand == 3 && areWeMoving == true){
            grid.moveLeft();
        }
        if (currentCommand == 4 && areWeMoving == true){
            grid.moveRight();
        }
        if (currentCommand == 2 && areWeMoving == true){
            areWeMoving = false;
            boolean dropped = true;
            while(dropped){
                dropped = grid.dropTiles();
            }
            boolean deleted = true;
            while(deleted = grid.findGroups()){
                dropped = true;
                while(dropped){
                    dropped = grid.dropTiles();
                }
            }
            if (grid.checkGameOver()) {
                grid.reset();
            }
            grid.newBlock();
            areWeMoving = true;
        }
    }
}
