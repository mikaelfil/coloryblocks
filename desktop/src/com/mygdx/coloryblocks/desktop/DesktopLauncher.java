package com.mygdx.coloryblocks.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.coloryblocks.Game;

public class DesktopLauncher {
	public static void main (final String[] arg) {
		final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.height = 400;
        config.width = 240;

		new LwjglApplication(new Game(), config);

	}
}
