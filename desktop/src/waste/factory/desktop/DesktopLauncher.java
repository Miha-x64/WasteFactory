package waste.factory.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import waste.factory.WasteFactoryGame;
import waste.factory.resource.WasteFactorySettings;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		WasteFactoryGame game = new WasteFactoryGame();
		config.title = game.title;
		config.width = WasteFactorySettings.SCREEN_WIDTH;
		config.height = WasteFactorySettings.SCREEN_HEIGHT;
		new LwjglApplication(game, config);
	}
}
