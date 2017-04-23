package waste.factory.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import waste.factory.WasteFactoryGame;
import waste.factory.drawing.Fonts;
import waste.factory.drawing.Overlay;
import waste.factory.object.Container;
import waste.factory.object.Waste;
import waste.factory.resource.WasteFactorySettings;

import static java.lang.Math.max;
import static waste.factory.resource.WasteFactorySettings.SCREEN_HEIGHT;
import static waste.factory.resource.WasteFactorySettings.SCREEN_WIDTH;
import static waste.factory.resource.WasteFactoryStrings.*;

/**
 * Created by miha on 22.04.17
 */
public final class GameScreen extends ScreenAdapter {

    private final WasteFactoryGame game;
    private final OrthographicCamera camera;

    private final Texture backgroundTx;
    private final WasteHelper wasteHelper;
    private final Texture paperContainerTx;

    private final Array<Container> containers;

    // game state
    private final long startTime;
    private long penalty;
    private long lastPenaltyTime;
    private boolean win, lose;
    private final Overlay overlay;

    // final text
    private final GlyphLayout levelFailed;
    private final GlyphLayout levelRetry;

    private final GlyphLayout levelSucceed;
    private final GlyphLayout levelOneMoreTime;

    private final Sound winSound;

    public GameScreen(WasteFactoryGame game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        game.resumeMusic();
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);

        backgroundTx = new Texture(Gdx.files.internal("gameBackground.png"));
        wasteHelper = new WasteHelper(camera);
        paperContainerTx = new Texture(Gdx.files.internal("containerPaper.png"));

        // recycle
        containers = new Array<>(1);
        containers.add(new waste.factory.object.Container(Waste.Type.PAPER,
                paperContainerTx,
                new Rectangle(900, 450, 293, 201),
                new Rectangle(900, 450, 293, 180),
                game.smallFont, 10, 150
        ));

        startTime = TimeUtils.millis();

        overlay = new waste.factory.drawing.Overlay(game.shapeRenderer, camera);

        levelFailed = new GlyphLayout(game.mediumFont, LEVEL_FAILED);
        levelRetry = new GlyphLayout(game.mediumFont, LEVEL_RETRY);

        levelSucceed = new GlyphLayout(game.mediumFont, LEVEL_SUCCEED);
        levelOneMoreTime = new GlyphLayout(game.mediumFont, LEVEL_ONE_MORE_TIME);

        winSound = Gdx.audio.newSound(Gdx.files.internal("win.ogg"));
    }

    @Override
    public void render(float delta) {
//        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        SpriteBatch batch = game.spriteBatch;
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(backgroundTx, 0, 0);

        long now = TimeUtils.millis();
        long timeLeft = startTime + WasteFactorySettings.LEVEL_TIME - now - penalty;

        for (int i = 0, size = containers.size; i < size; i++) {
            containers.get(i).draw(game.spriteBatch);
        }

        wasteHelper.draw(game.spriteBatch);

//        game.mediumFont.draw(batch, String.valueOf(Math.round(1f / Gdx.graphics.getDeltaTime())), 20, 50);

        if (!win) {
            if (lastPenaltyTime + WasteFactorySettings.DEFAULT_EFFECT_TIME > now) { // draw '%d seconds left' red or white
                float lightness = Interpolation.circleIn.apply((float) (now - lastPenaltyTime) / WasteFactorySettings.DEFAULT_EFFECT_TIME);
                game.mediumFont.setColor(1, lightness, lightness, 1);
            } else {
                game.mediumFont.setColor(1, 1, 1, 1);
            }

            game.mediumFont.draw(batch, String.format(
                    SECONDS_LEFT_FORMAT, max(0, (timeLeft) / 1000)), 25, SCREEN_HEIGHT - 25);
        }

        batch.end();

        // draw overlay, get fade amount
        float fade = overlay.draw(); // -1f, 0f..1f

        if (timeLeft > 0 && !win) {
            // interacting only when some time left & not already win
            Boolean status = wasteHelper.interact(containers);
            if (status == Boolean.TRUE) {
                checkWin(); // correct drop
            } else if (status == Boolean.FALSE) {
                penalty += WasteFactorySettings.WRONG_WASTE_PENALTY;
                lastPenaltyTime = now;
                overlay.fadeOut(Interpolation.circleIn, 1, 0, 0, .5f);
            }
        } else {
            // win or fail
            if (win) {
                // show 'you win' message
                if (fade > .5f) {
                    batch.begin();
                    game.mediumFont.setColor(1, 1, 1, 1);
                    Fonts.drawTextCenteredX(game.mediumFont, batch, levelSucceed, (SCREEN_HEIGHT + 1.5f * levelFailed.height) / 2);
                    if (fade > .99f) {
                        Fonts.drawTextCenteredX(game.mediumFont, batch, levelOneMoreTime,
                                (SCREEN_HEIGHT - 1.5f * levelRetry.height) / 2);
                    }
                    batch.end();
                }
            } else {
                // show 'you lose' message
                if (!lose) {
                    // overlay not started yet
                    overlay.fadeIn(Interpolation.circleOut, 0, 0, 0, .5f, true);
                    lose = true;
                } else if (fade > .5f) {
                    // overlay is fading in, draw text if necessary
                    batch.begin();
                    game.mediumFont.setColor(1, 1, 1, 1);
                    Fonts.drawTextCenteredX(game.mediumFont, batch, levelFailed, (SCREEN_HEIGHT + 1.5f * levelFailed.height) / 2);
                    if (fade > .99f) {
                        Fonts.drawTextCenteredX(game.mediumFont, batch, levelRetry, (SCREEN_HEIGHT - 1.5f * levelRetry.height) / 2);
                    }
                    batch.end();
                }
            }

            // restart level if
            if (Gdx.input.justTouched()) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        }
    }

    private void checkWin() {
        if (wasteHelper.getAmountOf(Waste.Type.PAPER) == 0) {
            game.pauseMusic();
            win = true;
            winSound.play();
            overlay.fadeIn(Interpolation.circleOut, 0, 0, 0, .5f, true);
            wasteHelper.disappear();
        }
    }

    @Override
    public void dispose() {
        backgroundTx.dispose();
        wasteHelper.dispose();
        paperContainerTx.dispose();
        winSound.dispose();
    }
}
