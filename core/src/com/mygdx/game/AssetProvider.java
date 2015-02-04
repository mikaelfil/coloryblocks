package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

public class AssetProvider implements Disposable {
    private final AssetManager manager;

    public enum TextureAsset {
        TILE_EMPTY("empty.png"),
        TILE_RED("red.png"),
        TILE_ORANGE("orange.png"),
        TILE_YELLOW("yellow.png"),
        TILE_GREEN("green.png"),
        TILE_CYAN("cyan.png"),
        TILE_BLUE("blue.png"),
        TILE_VIOLET("violet.png"),
        TILE_PURPLE("purple.png"),
        TILE_PINK("pink.png"),
        TILE_WHITE("white.png"),
        TILE_GRAY("gray.png"),
        TILE_BLACK("black.png"),
        TILE_DOWN("ArrowDown.png"),
        TILE_SIDES("ArrowSides.png");

        private final String file;

        TextureAsset(final String file) {
            this.file = file;
        }
    }

    public AssetProvider() {
        this.manager = new AssetManager();
    }

    @Override
    public void dispose() {
        manager.dispose();
    }

    /**
     * Preloads the requested asset.
     */
    public void load(final TextureAsset asset) {
        manager.load(asset.file, Texture.class);
        manager.update();
    }

    /**
     * Returns the requested asset. It the asset is not loaded yet, it will be
     * loaded now.
     */
    public Texture get(final TextureAsset asset) {
        final TextureLoader.TextureParameter textureParameter = new TextureLoader.TextureParameter();
        textureParameter.minFilter = Texture.TextureFilter.Linear;

        manager.load(asset.file, Texture.class, textureParameter);
        manager.finishLoading();

        return manager.get(asset.file, Texture.class);
    }
}