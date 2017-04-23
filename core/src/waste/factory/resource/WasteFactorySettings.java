package waste.factory.resource;

import com.badlogic.gdx.math.Interpolation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.Math.sin;

/**
 * Created by miha on 22.04.17
 */
public interface WasteFactorySettings {

    @px int SCREEN_WIDTH = 1280;
    @px int SCREEN_HEIGHT = 720;

    @ms int DEFAULT_EFFECT_TIME = 400;
    @ms int DEFAULT_TEXT_TIME = 2_000;
    @px int DEFAULT_EFFECT_RANGE = 20;

    @ms int LEVEL_TIME = 90_000;
    @ms int WRONG_WASTE_PENALTY = 5_000;

    int WASTE_ITEM_COUNT = 1;

    Interpolation SHAKE_INTERPOLATION = new Interpolation() {
        @Override public float apply(float a) {
            return (float) (sin(20*a) * (1-a));
        }
    };

    @Target(ElementType.FIELD) @Retention(RetentionPolicy.SOURCE) @interface px {}
    @Target(ElementType.FIELD) @Retention(RetentionPolicy.SOURCE) @interface ms {}
}
