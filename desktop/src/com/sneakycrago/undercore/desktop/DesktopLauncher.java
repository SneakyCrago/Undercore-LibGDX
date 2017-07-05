package com.sneakycrago.undercore.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sneakycrago.undercore.Application;
import com.sneakycrago.undercore.utils.AdsController;

public class DesktopLauncher{
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = Application.TITLE + " v" + Application.VERSION;
		config.height = Application.V_HEIGHT*2;
		config.width = Application.V_WIDTH*2;
		config.resizable = true;
		new LwjglApplication(new Application(), config);
	}
}
