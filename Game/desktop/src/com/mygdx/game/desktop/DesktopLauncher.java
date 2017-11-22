package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.CardGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = (int) CardGame.HEIGHT;
		config.width = (int) CardGame.WIDTH;
		config.resizable = false;
		config.samples = 3;
		//config.fullscreen = true;
		new LwjglApplication(new CardGame(), config);
	}
}
