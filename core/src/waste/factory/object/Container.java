package waste.factory.object;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import waste.factory.resource.WasteFactorySettings;

/**
 * Created by miha on 22.04.17
 */
public final class Container {

    public final waste.factory.object.Waste.Type type;
    private final Texture texture;
    private final Rectangle placement;
    public final Rectangle dropZone;
    private final BitmapFont messageFont;

    private final int messageOffsetX, messageOffsetY;

    private Array<Message> messages;
    private long shakeStarted;

    public Container(waste.factory.object.Waste.Type type, Texture texture, Rectangle placement, Rectangle dropZone, BitmapFont messageFont,
                     int messageOffsetX, int messageOffsetY) {
        this.type = type;
        this.texture = texture;
        this.placement = placement;
        this.dropZone = dropZone;
        this.messageFont = messageFont;
        this.messageOffsetX = messageOffsetX;
        this.messageOffsetY = messageOffsetY;
    }

    public void draw(SpriteBatch batch) {
        long now =  TimeUtils.millis();
        float offset = 0;
        // shake started && not finished
        if (shakeStarted != 0 && shakeStarted + WasteFactorySettings.DEFAULT_EFFECT_TIME > now) {
            // draw with shakeX
            offset = WasteFactorySettings.DEFAULT_EFFECT_RANGE *
                    WasteFactorySettings.SHAKE_INTERPOLATION.apply((float) (now - shakeStarted) / WasteFactorySettings.DEFAULT_EFFECT_TIME);
        }

        batch.draw(texture, placement.x + offset, placement.y);

        if (messages != null) {
            int offsetY = 0;
            for (int i = 0, size = messages.size; i < size; i++) {
                Message message = messages.get(i);
                if (message.startTime < now && message.startTime + WasteFactorySettings.DEFAULT_TEXT_TIME > now) {
                    messageFont.setColor(1, 1, 1, 1);
                    messageFont.draw(batch, message.message,
                            placement.x + messageOffsetX + offset,
                            placement.y + messageOffsetY - offsetY);
                    offsetY += 35;
                }
            }
        }
    }

    public void addMessage(String message, long startTime) {
        if (messages == null) {
            messages = new Array<>(1);
        }
        messages.add(new Message(message, startTime));
    }

    public void shake() {
        shakeStarted = TimeUtils.millis();
    }

    private static final class Message {
        final String message;
        final long startTime;
        Message(String message, long startTime) {
            this.message = message;
            this.startTime = startTime;
        }
    }

}
