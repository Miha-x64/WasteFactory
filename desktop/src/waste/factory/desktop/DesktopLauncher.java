package waste.factory.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import waste.factory.WasteFactoryGame;

import static waste.factory.resource.WasteFactorySettings.SCREEN_HEIGHT;
import static waste.factory.resource.WasteFactorySettings.SCREEN_WIDTH;
import static waste.factory.resource.WasteFactoryStrings.GAME_NAME;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		WasteFactoryGame game = new WasteFactoryGame();
		config.title = GAME_NAME;
		config.width = SCREEN_WIDTH;
		config.height = SCREEN_HEIGHT;
		new LwjglApplication(game, config);
	}
}
