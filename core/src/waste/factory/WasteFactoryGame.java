package waste.factory;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import waste.factory.screen.MainMenuScreen;

import static waste.factory.resource.WasteFactoryStrings.GAME_NAME;

public final class WasteFactoryGame extends Game {

	public final String title = GAME_NAME;

	public BitmapFont smallFont;
	public BitmapFont mediumFont;

	public SpriteBatch spriteBatch;
	public ShapeRenderer shapeRenderer;

	@Override
	public void create () {
		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		mediumFont = new BitmapFont(Gdx.files.internal("medium.fnt"));
		smallFont = new BitmapFont(Gdx.files.internal("small.fnt"));

		setScreen(new MainMenuScreen(this));
	}
	
	@Override
	public void dispose () {
		spriteBatch.dispose();
		shapeRenderer.dispose();
		mediumFont.dispose();
		smallFont.dispose();
	}
}
