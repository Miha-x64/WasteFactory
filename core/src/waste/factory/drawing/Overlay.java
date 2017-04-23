package waste.factory.drawing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.TimeUtils;
import waste.factory.resource.WasteFactorySettings;

/**
 * Created by miha on 23.04.17
 */
public final class Overlay {

    private final ShapeRenderer shapeRenderer;
    private final OrthographicCamera camera;

    private long fadeStart;
    private boolean in;
    private Interpolation interpolation;
    private float targetR, targetG, targetB, startOrTargetA;
    private boolean forever;

    public Overlay(ShapeRenderer shapeRenderer, OrthographicCamera camera) {
        this.shapeRenderer = shapeRenderer;
        this.camera = camera;
    }

    public void fadeIn(Interpolation interpolation,
                   float targetR, float targetG, float targetB, float targetA, boolean forever) {
        this.in = true;
        this.fadeStart = TimeUtils.millis();
        this.interpolation = interpolation;
        this.targetR = targetR;
        this.targetG = targetG;
        this.targetB = targetB;
        this.startOrTargetA = targetA;
        this.forever = forever;
    }

    public void fadeOut(Interpolation interpolation, float targetR, float targetG, float targetB, float startA) {
        this.in = false;
        this.fadeStart = TimeUtils.millis();
        this.interpolation = interpolation;
        this.targetR = targetR;
        this.targetG = targetG;
        this.targetB = targetB;
        this.startOrTargetA = startA;
        this.forever = false;
    }

    public float draw() {
        long now = TimeUtils.millis();
        if (fadeStart == 0) return -1f;
        int timeGone = (int) (now - fadeStart);
        if (timeGone > WasteFactorySettings.DEFAULT_EFFECT_TIME && !forever) return -1f; // fade have finished

        float fade = (float) Math.min(timeGone, WasteFactorySettings.DEFAULT_EFFECT_TIME) / WasteFactorySettings.DEFAULT_EFFECT_TIME;
        if (!in) fade = 1 - fade; // if fading out, convert 0..1 to 1..0

        // draw
        ShapeRenderer renderer = shapeRenderer;
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(targetR, targetG, targetB, interpolation.apply(fade) * startOrTargetA);
        renderer.box(0, 0, 0, WasteFactorySettings.SCREEN_WIDTH, WasteFactorySettings.SCREEN_HEIGHT, 0);
        renderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        return fade;
    }

}
