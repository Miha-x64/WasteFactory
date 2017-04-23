package waste.factory.drawing;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import waste.factory.resource.WasteFactorySettings;

/**
 * Created by miha on 22.04.17
 */
public final class Fonts {

    @Deprecated
    private Fonts() {
        throw new AssertionError();
    }

    public static void drawTextCenteredX(
            BitmapFont font, SpriteBatch spriteBatch, GlyphLayout layout, float y) {
        font.draw(spriteBatch, layout, (WasteFactorySettings.SCREEN_WIDTH - layout.width) / 2, y);
    }

    public static void drawTextCentered(
            BitmapFont font, SpriteBatch spriteBatch, GlyphLayout layout) {
        font.draw(spriteBatch, layout, (WasteFactorySettings.SCREEN_WIDTH - layout.width) / 2, (WasteFactorySettings.SCREEN_HEIGHT + layout.height) / 2);
    }

}
