package waste.factory.interaction;

import com.badlogic.gdx.utils.Array;
import waste.factory.object.Container;
import waste.factory.object.Waste;

/**
 * Created by miha on 22.04.17
 */
public final class DropHelper {

    public void findDroppable(
            Array<Container> targets, Array<Waste> waste,
            Container[] outActiveTarget, Waste[] outActiveWaste) {
        for (int t = 0, tSize = targets.size; t < tSize; t++) {
            Container target = targets.get(t);
            for (int w = 0, wSize = waste.size; w < wSize; w++) {
                Waste item = waste.get(w);

                if (!target.dropZone.contains(item.placement))
                    continue;

                outActiveTarget[0] = target;
                outActiveWaste[0] = item;
                return;
            }
        }

        outActiveTarget[0] = null;
        outActiveWaste[0] = null;
    }

}
