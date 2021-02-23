package pl.op.danex11.hulajwro.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import pl.op.danex11.hulajwro.SpielFahre;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new SpielFahre(), config);
		config.height = 360;
		config.width = 640;
		//to do proper resize on fullscreen
		//config.fullscreen = true;

	}
}
