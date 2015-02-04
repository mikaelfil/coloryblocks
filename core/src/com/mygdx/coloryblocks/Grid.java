package com.mygdx.coloryblocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Grid {
    final AssetProvider assetProvider;
    final List<AssetProvider.TextureAsset> textures = Arrays.asList(AssetProvider.TextureAsset.TILE_EMPTY,
            AssetProvider.TextureAsset.TILE_RED,
            AssetProvider.TextureAsset.TILE_ORANGE,
            AssetProvider.TextureAsset.TILE_YELLOW,
            AssetProvider.TextureAsset.TILE_GREEN,
            AssetProvider.TextureAsset.TILE_CYAN,
            AssetProvider.TextureAsset.TILE_BLUE,
            AssetProvider.TextureAsset.TILE_VIOLET,
            AssetProvider.TextureAsset.TILE_PURPLE,
            AssetProvider.TextureAsset.TILE_PINK,
            AssetProvider.TextureAsset.TILE_WHITE,
            AssetProvider.TextureAsset.TILE_GRAY,
            AssetProvider.TextureAsset.TILE_BLACK,
            AssetProvider.TextureAsset.TILE_DOWN,
            AssetProvider.TextureAsset.TILE_SIDES);
    final int[][] tiles;
    int highest;

    public Grid(final AssetProvider assetProvider, final int columns, final int rows) {
        this.assetProvider = assetProvider;
        this.tiles = new int[columns][rows];
        highest = 4;

        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                this.tiles[x][y] = 0;
            }
        }

        Random newTile = new Random();
        this.tiles[this.tiles.length / 2][this.tiles[this.tiles.length / 2].length - 2] = newTile.nextInt((highest - 2) + 1) + 1;
        this.tiles[this.tiles.length / 2][this.tiles[this.tiles.length / 2].length - 1] = newTile.nextInt((highest - 2) + 1) + 1;
    }

    public boolean dropTiles() {
        boolean isDropped = false;
        for (int x = 0; x < this.tiles.length; x++) {
            for (int y = 1; y < this.tiles[x].length; y++) { //Bottom tiles do not drop.
                if (this.tiles[x][y - 1] == 0 && this.tiles[x][y] != 0) {
                    this.tiles[x][y - 1] = this.tiles[x][y];
                    this.tiles[x][y] = 0;
                    isDropped = true;
                }
            }
        }
        return isDropped;
    }

    private void removeAndUpgrade(List<RemoveTiles> remove, final List<UpgradeTiles> upgrade) {
        int i = 0;
        while (i < remove.size()) {
            tiles[remove.get(i).getX()][remove.get(i).getY()] = 0;
            i++;
        }
        i = 0;
        while (i < upgrade.size()) {
            tiles[upgrade.get(i).getX()][upgrade.get(i).getY()] = upgrade.get(i).getValue() + 1;
            if (upgrade.get(i).getValue() + 1 > highest){
                highest = upgrade.get(i).getValue() + 1;
            }
            i++;
        }
    }

    public void render(final SpriteBatch spriteBatch) {
        for (int x = 0; x < this.tiles.length; x++) {
            for (int y = 0; y < this.tiles[x].length; y++) {
                float screenWidth = Gdx.graphics.getWidth();
                float screenHeight = Gdx.graphics.getHeight();
                final Texture texture = assetProvider.get(this.textures.get(this.tiles[x][y]));
                spriteBatch.draw(texture, x*(screenWidth/(this.tiles.length)), y*(screenHeight/(this.tiles[x].length)),
                        screenWidth/(this.tiles.length),screenHeight/(this.tiles[x].length));
            }
        }
    }

    public boolean checkGameOver() {
        for (int x = 0; x < this.tiles.length; x++) {
            if (tiles[x][this.tiles[x].length - 2] != 0) {
                return true;
            }
        }
        return false;
    }

    public boolean findGroups() {
        final List<RemoveTiles> remove = new ArrayList<>();
        final List<UpgradeTiles> upgrade = new ArrayList<>();
        boolean tilesDeleted = false;

        for (int x = 0; x < this.tiles.length; x++) {
            for (int y = 0; y < this.tiles[x].length; y++) { //Let's check all the tiles
                if (this.tiles[x][y] != 0 && this.tiles[x][y] < 12) {
                    if (remove.size() != 0) { //If there are removed tiles...
                        int i = 0;
                        while (i < remove.size()) { //... check if the current tile is already to-be-removed.
                            if (remove.get(i).getX() == x && remove.get(i).getY() == y) {
                                break;
                            }
                            i++;
                        }
                        if (i == remove.size()) { //The current tile hasn't been removed.
                            int sum = 1; // Sum tells how many same type tiles are adjanced to this tile.
                            if (x > 0) { // If not in the left side of the grid
                                if (this.tiles[x - 1][y] == this.tiles[x][y]) {
                                    sum++;
                                }
                            }
                            if (x < this.tiles.length - 1) { // If not in the right side of the grid
                                if (this.tiles[x + 1][y] == this.tiles[x][y]) {
                                    sum++;
                                }
                            }
                            if (y > 0) { // If not at the bottom of the grid
                                if (this.tiles[x][y - 1] == this.tiles[x][y]) {
                                    sum++;
                                }
                            }
                            if (y < this.tiles[x].length - 1) {
                                if (this.tiles[x][y + 1] == this.tiles[x][y]) {
                                    sum++;
                                }
                            }
                            if (sum > 2) {
                                tilesDeleted = true;
                                List<RemoveTiles> marked = MarkAdjanced(x, y, new ArrayList<RemoveTiles>());
                                upgrade.add(FindUpgrade(marked));
                                remove.addAll(marked);

                            }
                        }
                    } else {
                        int sum = 1; // Sum tells how many same type tiles are adjanced to this tile.
                        if (x > 0) { // If not in the left side of the grid
                            if (this.tiles[x - 1][y] == this.tiles[x][y]) {
                                sum++;
                            }
                        }
                        if (x < this.tiles.length - 1) { // If not in the right side of the grid
                            if (this.tiles[x + 1][y] == this.tiles[x][y]) {
                                sum++;
                            }
                        }
                        if (y > 0) { // If not at the bottom of the grid
                            if (this.tiles[x][y - 1] == this.tiles[x][y]) {
                                sum++;
                            }
                        }
                        if (y < this.tiles[x].length - 1) {
                            if (this.tiles[x][y + 1] == this.tiles[x][y]) {
                                sum++;
                            }
                        }
                        if (sum > 2) {
                            tilesDeleted = true;
                            List<RemoveTiles> marked = MarkAdjanced(x, y, new ArrayList<RemoveTiles>());
                            upgrade.add(FindUpgrade(marked));
                            remove.addAll(marked);
                        }
                    }
                }
                if (this.tiles[x][y] == 13) {
                    tilesDeleted = true;
                    RemoveColumn(x);
                }
                if (this.tiles[x][y] == 14){
                    tilesDeleted = true;
                    RemoveRow(y);
                }
            }
        }

        if (tilesDeleted == true) {
            removeAndUpgrade(remove, upgrade);
        }
        return tilesDeleted;
    }

    private void RemoveRow(final int y) {
        for (int x = 0; x < this.tiles.length; x++) {
            this.tiles[x][y] = 0;
        }
    }

    private void RemoveColumn(final int x) {
        for (int y = 0; y < this.tiles[x].length; y++) {
            this.tiles[x][y] = 0;
        }
    }

    private UpgradeTiles FindUpgrade(List<RemoveTiles> marked) {
        UpgradeTiles toBeUpgraded = new UpgradeTiles(marked.get(0).getX(), marked.get(0).getY(),
                this.tiles[marked.get(0).getX()][marked.get(0).getY()]);
        for (int i = 1; i < marked.size(); i++)
            if (toBeUpgraded.getY() >= marked.get(i).getY()) {  // If there's a tile that's below or at the same level than the current
                if (toBeUpgraded.getX() > marked.get(i).getX() && toBeUpgraded.getY() == marked.get(i).getY()) { // If there's a tile that's more to the left than current
                    toBeUpgraded = new UpgradeTiles(marked.get(i).getX(), marked.get(i).getY(),
                            this.tiles[marked.get(i).getX()][marked.get(i).getY()]);
                }
                if (toBeUpgraded.getY() > marked.get(i).getY()) {
                    toBeUpgraded = new UpgradeTiles(marked.get(i).getX(), marked.get(i).getY(),
                            this.tiles[marked.get(i).getX()][marked.get(i).getY()]);
                }
            }
        return toBeUpgraded;
    }

    private List<RemoveTiles> MarkAdjanced(int x, int y, final List<RemoveTiles> marked2) {
        List<RemoveTiles> marked = marked2;
        marked.add(new RemoveTiles(x, y));

        if (x > 0) { // If the tile is not in the left side
            if (this.tiles[x - 1][y] == this.tiles[x][y]) { // If the tile is the same value as the tile on the left
                int i = 0;
                while (i < marked.size()) { //Checks if the left tile is already to-be-removed.
                    if (marked.get(i).getX() == x - 1 && marked.get(i).getY() == y) {
                        break;
                    }
                    i++;
                }
                if (i == marked.size()) { // If not, mark it and all adjanced tiles to it.
                    marked.addAll(MarkAdjanced(x - 1, y, marked));
                }
            }
        }
        if (x < this.tiles.length - 1) { // If the tile is not on the right side
            if (this.tiles[x + 1][y] == this.tiles[x][y]) { // If the tile is the same value as the tile on the right
                int i = 0;
                while (i < marked.size()) { //... check if the right tile is already to-be-removed.
                    if (marked.get(i).getX() == x + 1 && marked.get(i).getY() == y) {
                        break;
                    }
                    i++;
                }
                if (i == marked.size()) {
                    marked.addAll(MarkAdjanced(x + 1, y, marked));
                }
            }
        }
        if (y > 0) {
            if (this.tiles[x][y - 1] == this.tiles[x][y]) {
                int i = 0;
                while (i < marked.size()) { //... check if the current tile is already to-be-removed.
                    if (marked.get(i).getX() == x && marked.get(i).getY() == y - 1) {
                        break;
                    }
                    i++;
                }
                if (i == marked.size()) {
                    marked.addAll(MarkAdjanced(x, y - 1, marked));
                }
            }
        }
        if (y < this.tiles[x].length - 1) {
            if (this.tiles[x][y + 1] == this.tiles[x][y]) {
                int i = 0;
                while (i < marked.size()) { //... check if the current tile is already to-be-removed.
                    if (marked.get(i).getX() == x && marked.get(i).getY() == y + 1) {
                        break;
                    }
                    i++;
                }
                if (i == marked.size()) {
                    marked.addAll(MarkAdjanced(x, y + 1, marked));
                }
            }
        }
        return marked;
    }

    public void reset() {
        for (int x = 0; x < this.tiles.length; x++) {
            for (int y = 0; y < this.tiles[x].length; y++) {
                this.tiles[x][y] = 0;
            }
        }
        highest = 4;
    }

    public boolean spin() {
        for (int x = 0; x < this.tiles.length; x++) {
            if (this.tiles[x][this.tiles[x].length - 2] != 0) {
                if (x == this.tiles.length - 1) {
                    if (this.tiles[x-1][this.tiles[x].length - 2] == 0 && this.tiles[x][this.tiles[x].length - 1] == 0){
                        return true; // It was a single block.
                    } else {
                        this.tiles[x - 1][this.tiles[x].length - 2] = this.tiles[x][this.tiles[x].length - 2];
                        this.tiles[x][this.tiles[x].length - 2] = this.tiles[x][this.tiles[x].length - 1];
                        this.tiles[x][this.tiles[x].length - 1] = 0;
                        return true;
                    }
                } else if (this.tiles[x][this.tiles[x].length - 1] != 0) {
                    this.tiles[x + 1][this.tiles[x].length - 2] = this.tiles[x][this.tiles[x].length - 1];
                    this.tiles[x][this.tiles[x].length - 1] = 0;
                    return true;
                } else {
                    if (x == 0) {
                        if (this.tiles[x + 1][this.tiles[x].length - 2] == 0 && this.tiles[x][this.tiles[x].length - 1] == 0) {
                            return true; // It was a single block.
                        } else {
                            this.tiles[x][this.tiles[x].length - 1] = this.tiles[x][this.tiles[x].length - 2];
                            this.tiles[x][this.tiles[x].length - 2] = this.tiles[x + 1][this.tiles[x].length - 2];
                            this.tiles[x + 1][this.tiles[x].length - 2] = 0;
                            return true;
                        }
                    } else {
                        if (this.tiles[x-1][this.tiles[x].length - 2] == 0 && this.tiles[x][this.tiles[x].length - 1] == 0
                                && this.tiles[x+1][this.tiles[x].length-2] == 0){
                            return true; // It was a single block.
                        }
                        else {
                            this.tiles[x][this.tiles[x].length - 1] = this.tiles[x][this.tiles[x].length - 2];
                            this.tiles[x][this.tiles[x].length - 2] = this.tiles[x + 1][this.tiles[x].length - 2];
                            this.tiles[x + 1][this.tiles[x].length - 2] = 0;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean moveLeft() {
        for (int x = 1; x < this.tiles.length; x++) {
            if (this.tiles[x][this.tiles[x].length - 2] != 0) { //If a tile is not blank.
                if (this.tiles[x - 1][this.tiles[x].length - 2] == 0) { // If there is free space on the left
                    if (this.tiles[x][this.tiles[x].length - 1] != 0) { // If there's a block above
                        this.tiles[x - 1][this.tiles[x].length - 1] = this.tiles[x][this.tiles[x].length - 1];
                        this.tiles[x][this.tiles[x].length - 1] = 0;
                        this.tiles[x - 1][this.tiles[x].length - 2] = this.tiles[x][this.tiles[x].length - 2];
                        this.tiles[x][this.tiles[x].length - 2] = 0;
                        return true;
                    } else {
                        if (x != this.tiles.length-1){
                            if (this.tiles[x+1][this.tiles[x].length - 2] != 0) { // If there is block on the right
                                this.tiles[x - 1][this.tiles[x].length - 2] = this.tiles[x][this.tiles[x].length - 2];
                                this.tiles[x][this.tiles[x].length - 2] = this.tiles[x + 1][this.tiles[x].length - 2];
                                this.tiles[x + 1][this.tiles[x].length - 2] = 0;
                                return true;
                            } else {
                                this.tiles[x - 1][this.tiles[x].length - 2] = this.tiles[x][this.tiles[x].length - 2];
                                this.tiles[x][this.tiles[x].length - 2] = 0;
                                return true;
                            }
                        } else {
                                this.tiles[x - 1][this.tiles[x].length - 2] = this.tiles[x][this.tiles[x].length - 2];
                                this.tiles[x][this.tiles[x].length - 2] = 0;
                                return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    public boolean moveRight() {
        for (int x = 0; x < this.tiles.length-1; x++) {
            if (this.tiles[x][this.tiles[x].length - 2] != 0) { //If a tile is not blank.
                if (this.tiles[x + 1][this.tiles[x].length - 2] == 0) { // If there is free space on the right
                    if (this.tiles[x][this.tiles[x].length - 1] != 0) { // If there's a block above
                        this.tiles[x + 1][this.tiles[x].length - 1] = this.tiles[x][this.tiles[x].length - 1];
                        this.tiles[x][this.tiles[x].length - 1] = 0;
                        this.tiles[x + 1][this.tiles[x].length - 2] = this.tiles[x][this.tiles[x].length - 2];
                        this.tiles[x][this.tiles[x].length - 2] = 0;
                        return true;
                    } else { // If there is blocks sideways or only one block
                        if (x != 0 && x < this.tiles.length-1){ // The block is on neither side.
                            if (this.tiles[x-1][this.tiles[x].length - 2] != 0) { // If there's tile on the left
                                this.tiles[x + 1][this.tiles[x].length - 2] = this.tiles[x][this.tiles[x].length - 2];
                                this.tiles[x][this.tiles[x].length - 2] = this.tiles[x-1][this.tiles[x].length - 2];
                                this.tiles[x-1][this.tiles[x].length - 2] = 0;
                                return true;
                            } else { //It is a solo block.
                                this.tiles[x + 1][this.tiles[x].length - 2] = this.tiles[x][this.tiles[x].length - 2];
                                this.tiles[x][this.tiles[x].length - 2] = 0;
                                return true;
                            }
                        }
                        else if (x == 0){ //Block is on the left side.
                            if (this.tiles[x+1][this.tiles[x].length - 2] != 0) { // If there's tile on the right
                                return true; // Then the block pair is already on the right side.
                            } else { //It is a solo block.
                                this.tiles[x + 1][this.tiles[x].length - 2] = this.tiles[x][this.tiles[x].length - 2];
                                this.tiles[x][this.tiles[x].length - 2] = 0;
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public void newBlock() {
        Random choice = new Random();
        if(choice.nextInt(100)+1 < 98) {
            Random newTile = new Random();
            this.tiles[this.tiles.length / 2][this.tiles[this.tiles.length / 2].length - 2] = newTile.nextInt((highest - 2) + 1) + 1;
            this.tiles[this.tiles.length / 2][this.tiles[this.tiles.length / 2].length - 1] = newTile.nextInt((highest - 2) + 1) + 1;
        }
        else {
            Random specialTile = new Random();
            this.tiles[this.tiles.length / 2][this.tiles[this.tiles.length / 2].length - 2] = specialTile.nextInt(2) + 13;
        }
    }
}

