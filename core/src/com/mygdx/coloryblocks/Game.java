package com.mygdx.coloryblocks;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Game extends ApplicationAdapter {
	SpriteBatch spriteBatch;
    Engine engine;
    InputHandler inputHandler;
	
	@Override
	public void create () {
		this.spriteBatch = new SpriteBatch();
        this.engine = new Engine();
        this.inputHandler = new InputHandler(this);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		spriteBatch.begin();
		engine.render(spriteBatch);
        spriteBatch.end();
	}

    public void commitCommand(int currentCommand) {
        engine.commitCommand(currentCommand);
    }
}
