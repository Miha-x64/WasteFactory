package waste.factory.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.TimeUtils;
import waste.factory.interaction.DragHelper;
import waste.factory.interaction.DropHelper;
import waste.factory.object.Container;
import waste.factory.object.Waste;
import waste.factory.resource.WasteFactorySettings;
import waste.factory.util.MutableInt;

import java.util.EnumMap;
import java.util.Random;

import static waste.factory.resource.WasteFactorySettings.SCREEN_HEIGHT;
import static waste.factory.resource.WasteFactorySettings.SCREEN_WIDTH;
import static waste.factory.resource.WasteFactoryStrings.WRONG_WASTE_FORMAT;

/**
 * Created by miha on 23.04.17
 */
/*pkg*/ final class WasteHelper {

    private final Texture milkPackTx;
    private final Texture newspaperTx;
    private final Texture bookTx;
    private final Texture bulbTx;
    private final Texture cableTx;
    private final Texture cdTx;
    private final Texture colouredPaperTx;
    private final Texture eggsCartonTx;
    private final Texture glovesTx;
    private final Texture mapTx;
    private final Texture napkinsTx;
    private final Texture orangePeelTx;
    private final Texture paperCupTx;
    private final Texture receiptTx;
    private final Texture toothpasteTx;

    private final Array<Waste> waste;
    private final ArrayMap<Waste, Long> dyingWaste; // value is animation start time
    private final EnumMap<Waste.Type, MutableInt> wasteAmounts;

    private final DragHelper dragHelper;
    private final DropHelper dropHelper;

    // temporary single-element value containers
    private final waste.factory.object.Container[] activeContainer;
    private final Waste[] activeItem;

    private long disappearAt;

    /*pkg*/ WasteHelper(OrthographicCamera camera) {
        milkPackTx = new Texture(Gdx.files.internal("wasteMilkAseptic.png"));
        newspaperTx = new Texture(Gdx.files.internal("wasteNewspaper.png"));
        bookTx = new Texture(Gdx.files.internal("wasteBook.png"));
        bulbTx = new Texture(Gdx.files.internal("wasteBulb.png"));
        cableTx = new Texture(Gdx.files.internal("wasteCable.png"));
        cdTx = new Texture(Gdx.files.internal("wasteCd.png"));
        colouredPaperTx = new Texture(Gdx.files.internal("wasteColouredPaper.png"));
        eggsCartonTx = new Texture(Gdx.files.internal("wasteEggsCarton.png"));
        glovesTx = new Texture(Gdx.files.internal("wasteGloves.png"));
        mapTx = new Texture(Gdx.files.internal("wasteMap.png"));
        napkinsTx = new Texture(Gdx.files.internal("wasteNapkins.png"));
        orangePeelTx = new Texture(Gdx.files.internal("wasteOrangePeel.png"));
        paperCupTx = new Texture(Gdx.files.internal("wastePaperCup.png"));
        receiptTx = new Texture(Gdx.files.internal("wasteReceipt.png"));
        toothpasteTx = new Texture(Gdx.files.internal("wasteToothpaste.png"));

        waste = new Array<>(WasteFactorySettings.WASTE_ITEM_COUNT * 15);
        wasteAmounts = new EnumMap<>(Waste.Type.class);

        addWaste(WasteFactorySettings.WASTE_ITEM_COUNT, Waste.Type.ASEPTIC, milkPackTx);
        addWaste(WasteFactorySettings.WASTE_ITEM_COUNT, Waste.Type.PAPER, newspaperTx);
        addWaste(WasteFactorySettings.WASTE_ITEM_COUNT, Waste.Type.PAPER, bookTx);
        addWaste(WasteFactorySettings.WASTE_ITEM_COUNT, Waste.Type.SAD, bulbTx);
        addWaste(WasteFactorySettings.WASTE_ITEM_COUNT, Waste.Type.SAD, cableTx);
        addWaste(WasteFactorySettings.WASTE_ITEM_COUNT, Waste.Type.SAD, cdTx);
        addWaste(WasteFactorySettings.WASTE_ITEM_COUNT, Waste.Type.PAPER, colouredPaperTx);
        addWaste(WasteFactorySettings.WASTE_ITEM_COUNT, Waste.Type.CARTON, eggsCartonTx);
        addWaste(WasteFactorySettings.WASTE_ITEM_COUNT, Waste.Type.SAD, glovesTx);
        addWaste(WasteFactorySettings.WASTE_ITEM_COUNT, Waste.Type.PAPER, mapTx);
        addWaste(WasteFactorySettings.WASTE_ITEM_COUNT, Waste.Type.SAD, napkinsTx);
        addWaste(WasteFactorySettings.WASTE_ITEM_COUNT, Waste.Type.SAD, orangePeelTx);
        addWaste(WasteFactorySettings.WASTE_ITEM_COUNT, Waste.Type.SAD, paperCupTx);
        addWaste(WasteFactorySettings.WASTE_ITEM_COUNT, Waste.Type.SAD, receiptTx);
        addWaste(WasteFactorySettings.WASTE_ITEM_COUNT, Waste.Type.SAD, toothpasteTx);

        dyingWaste = new ArrayMap<>(1);

        dragHelper = new DragHelper(waste, camera, 0, SCREEN_WIDTH, 0, SCREEN_HEIGHT);
        dropHelper = new DropHelper();

        activeContainer = new Container[1];
        activeItem = new Waste[1];
    }

    private void addWaste(int number, Waste.Type type, Texture texture) {
        for (int i = 0; i < number; i++) {
            waste.add(findRoom(new Waste(type, texture)));
        }
        MutableInt amount = wasteAmounts.get(type);
        if (amount == null) {
            amount = new waste.factory.util.MutableInt();
            wasteAmounts.put(type, amount);
        }
        amount.value += number;
    }

    private final Random random = new Random();
    private Waste findRoom(Waste victim) {
        Rectangle rect = victim.placement;
        int attempts = 0;
        do {
            rect.x = random.nextInt(SCREEN_WIDTH - (int) rect.width);
            rect.y = random.nextInt((SCREEN_HEIGHT - (int) rect.height) / 2);
            if (attempts++ > 5) break;
        } while (overlapsAny(rect));
        return victim;
    }

    private boolean overlapsAny(Rectangle rect) {
        for (int i = 0, size = waste.size; i < size; i++) {
            Rectangle placement = waste.get(i).placement;
            // ignore self
            if (rect != placement && rect.overlaps(placement)) {
                return true;
            }
        }
        return false;
    }

    /*pkg*/ void draw(SpriteBatch batch) {
        long now = TimeUtils.millis();

        for (int i = 0, size = waste.size; i < size; i++) {
            waste.get(i).draw(batch);
        }

        for (int i = 0, size = dyingWaste.size; i < size; i++) {
            long startTime = dyingWaste.getValueAt(i);
            if (startTime + WasteFactorySettings.DEFAULT_EFFECT_TIME > now) {
                // animation running
                dyingWaste.getKeyAt(i).draw(batch,
                        Interpolation.circleIn.apply(1f -
                                (float) (now - startTime) / WasteFactorySettings.DEFAULT_EFFECT_TIME));
            } else {
                // dead waste
                dyingWaste.removeIndex(i);
                i--;
                size--;
            }
        }

        if (disappearAt != 0 && disappearAt <= now) { // disappear if necessary
            Long Now = now;
            for (int i = 0, size = waste.size; i < size; i++) {
                dyingWaste.put(waste.get(i), Now);
            }
            waste.clear();
            disappearAt = 0;
        }
    }

    // null if no change,
    // true if dropped correctly,
    // false if dropped incorrectly
    /*pkg*/ Boolean interact(Array<Container> containers) {
        long now = TimeUtils.millis();
        dragHelper.onRender();
        dropHelper.findDroppable(containers, waste, activeContainer, activeItem);
        if (activeContainer[0] != null) {
            if (activeContainer[0].type == activeItem[0].type) { // dropped into correct zone
                waste.removeValue(activeItem[0], true);
                wasteAmounts.get(activeItem[0].type).value--;
                dyingWaste.put(activeItem[0], now);
                dragHelper.stopDragging(activeItem[0]);
                return Boolean.TRUE;
            } else {
                // dropped into incorrect zone
                findRoom(activeItem[0]);
                dragHelper.stopDragging(activeItem[0]);
                activeContainer[0].shake();
                activeContainer[0].addMessage(
                        String.format(WRONG_WASTE_FORMAT,
                                activeContainer[0].type.name, activeItem[0].type.capitalizedName), now);
                return Boolean.FALSE;
            }
        }
        return null;
    }

    /*pkg*/ int getAmountOf(Waste.Type type) {
        return wasteAmounts.get(type).value;
    }

    /*pkg*/ void disappear() {
        disappearAt = TimeUtils.millis() + 1_000;
    }

    /*pkg*/ void dispose() {
        milkPackTx.dispose();
        newspaperTx.dispose();
        bookTx.dispose();
        bulbTx.dispose();
        cableTx.dispose();
        cdTx.dispose();
        colouredPaperTx.dispose();
        eggsCartonTx.dispose();
        glovesTx.dispose();
        mapTx.dispose();
        napkinsTx.dispose();
        orangePeelTx.dispose();
        paperCupTx.dispose();
        receiptTx.dispose();
        toothpasteTx.dispose();
    }

}
