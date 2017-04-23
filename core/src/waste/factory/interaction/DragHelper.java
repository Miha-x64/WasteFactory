package waste.factory.interaction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import waste.factory.object.Waste;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Created by miha on 22.04.17
 */
public final class DragHelper {

    private final Array<Waste> elements;
    private final OrthographicCamera camera;

    private final Vector3 point;
    private Waste/*?*/ beingDragged;

    private final int minX, maxX, minY, maxY;

    public DragHelper(Array<Waste> elements, OrthographicCamera camera, int minX, int maxX, int minY, int maxY) {
        this.elements = elements;
        this.camera = camera;

        this.point = new Vector3();

        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public void onRender() {
        if (!Gdx.input.isTouched()) {
            beingDragged = null;
            return;
        }

        // touched
        float oldX = point.x;
        float oldY = point.y;
        point.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(point);
        if (beingDragged == null) {
            // start dragging new item
            for (int i = 0, size = elements.size; i < size; i++) {
                if (elements.get(i).placement.contains(point.x, point.y)) {
                    // start dragging
                    beingDragged = elements.get(i);
                    break;
                }
            }
        } else {
            // continue dragging
            float futureX = beingDragged.placement.x + point.x - oldX;
            float futureY = beingDragged.placement.y + point.y - oldY;
            beingDragged.placement.x = min(max(futureX, minX), maxX - beingDragged.placement.width);
            beingDragged.placement.y = min(max(futureY, minY), maxY - beingDragged.placement.height);
        }
    }

    public void stopDragging(Waste victim) {
        if (beingDragged == victim) {
            beingDragged = null;
        }
    }

}
