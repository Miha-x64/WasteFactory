package waste.factory.object;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import waste.factory.resource.WasteFactoryStrings;

/**
 * Created by miha on 22.04.17
 */
public final class Waste {

    public final Type type;
    private final Texture tx;
    public final Rectangle placement;

    public Waste(Type type, Texture tx) {
        this.type = type;
        this.tx = tx;
        this.placement = new Rectangle(0, 0, tx.getWidth(), tx.getHeight());
    }

    public void draw(SpriteBatch batch) {
        batch.draw(tx, placement.x, placement.y);
    }

    public void draw(SpriteBatch batch, float opacity) {
        batch.setColor(1f, 1f, 1f, opacity);
        batch.draw(tx, placement.x, placement.y);
        batch.setColor(Color.WHITE); // restore colour
    }

    public enum Type {
        PAPER(WasteFactoryStrings.WASTE_PAPER), CARTON(WasteFactoryStrings.WASTE_CARTON), ASEPTIC(WasteFactoryStrings.WASTE_ASEPTIC), SAD(WasteFactoryStrings.WASTE_SAD);

        public final String name;
        public final String capitalizedName;

        Type(String name) {
            this.name = name;
            char[] chars = name.toCharArray();
            chars[0] = Character.toUpperCase(chars[0]);
            capitalizedName = new String(chars);
        }
    }

}
