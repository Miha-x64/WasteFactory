package waste.factory.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import waste.factory.WasteFactoryGame;
import waste.factory.drawing.Fonts;
import waste.factory.resource.WasteFactorySettings;
import waste.factory.resource.WasteFactoryStrings;
import waste.factory.screen.game.GameScreen;

/**
 * Created by miha on 22.04.17
 */
public final class MainMenuScreen extends ScreenAdapter {

    private final WasteFactoryGame game;
    private final OrthographicCamera camera;
    private final GlyphLayout invitationSplash;

    public MainMenuScreen(WasteFactoryGame game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, WasteFactorySettings.SCREEN_WIDTH, WasteFactorySettings.SCREEN_HEIGHT);
        this.invitationSplash = new GlyphLayout(game.mediumFont, WasteFactoryStrings.INVITATION_SPLASH);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.spriteBatch.setProjectionMatrix(camera.combined);

        game.spriteBatch.begin();
        game.mediumFont.setColor(1, 1, 1, 1);
        Fonts.drawTextCentered(game.mediumFont, game.spriteBatch, invitationSplash);
        game.spriteBatch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }
}
