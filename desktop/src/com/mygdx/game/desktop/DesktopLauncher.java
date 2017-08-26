package com.mygdx.game.desktop;

import Helpers.GameInfo;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
                config.width = GameInfo.WIDTH;
                config.height = GameInfo.HEIGHT;
                
		new LwjglApplication(new MyGdxGame(), config);
	}
}
