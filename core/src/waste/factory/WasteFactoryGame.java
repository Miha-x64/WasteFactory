package waste.factory;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import waste.factory.screen.MainMenuScreen;

import static waste.factory.resource.WasteFactorySettings.SCREEN_HEIGHT;
import static waste.factory.resource.WasteFactorySettings.SCREEN_WIDTH;

public final class WasteFactoryGame extends Game {

	private final Vector3 coordinates = new Vector3();

	public BitmapFont smallFont;
	public BitmapFont mediumFont;

	public SpriteBatch spriteBatch;
	public ShapeRenderer shapeRenderer;

    private OrthographicCamera camera;
	private Music music;
	private Texture soundOnTx;
	private Texture soundOffTx;
	private boolean soundOn = true;
	private boolean paused = false;
	private Rectangle soundSwitch;

	@Override
	public void create() {
	    camera = new OrthographicCamera();
	    camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);

		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		mediumFont = new BitmapFont(Gdx.files.internal("medium.fnt"));
		smallFont = new BitmapFont(Gdx.files.internal("small.fnt"));

		music = Gdx.audio.newMusic(Gdx.files.internal("angelo-badalamenti-caitlin-s-theme.mp3"));
		music.setLooping(true);
		music.play();

		soundOnTx = new Texture(Gdx.files.internal("soundOn.png"));
		soundOffTx = new Texture(Gdx.files.internal("soundOff.png"));
		int size = soundOnTx.getWidth();
		soundSwitch = new Rectangle(SCREEN_WIDTH - 2*size, SCREEN_HEIGHT - 2*size, size, size);

		setScreen(new MainMenuScreen(this));
	}

    @Override
    public void render() {
        super.render();

        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);

        spriteBatch.begin();
        Texture soundTx;
        if (soundOn) soundTx = soundOnTx;
        else soundTx = soundOffTx;
        spriteBatch.draw(soundTx, soundSwitch.x, soundSwitch.y);
        spriteBatch.end();

        if (Gdx.input.justTouched()) {
            coordinates.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(coordinates);
            if (soundSwitch.contains(coordinates.x, coordinates.y)) {
                soundOn = !soundOn;
                if (soundOn) music.play();
                else music.pause();
            }
        }
    }

    public void pauseMusic() {
	    if (soundOn && !paused) {
	        music.pause();
	        paused = true;
        }
    }

    public void resumeMusic() {
	    if (soundOn && paused) {
	        music.play();
	        paused = false;
        }
    }

    @Override
	public void dispose() {
		spriteBatch.dispose();
		shapeRenderer.dispose();
		mediumFont.dispose();
		smallFont.dispose();
		music.dispose();
	}
}
