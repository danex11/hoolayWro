package danieldiv.pseudogames.hulajwro.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import danieldiv.pseudogames.hulajwro.GamePlay;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new GamePlay(), config);
		config.height = 360;
		config.width = 640;

	}
}
