package com.csse3200.game.components.cutscenes;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ItemFactory;

public class FiredEnd extends GameArea {
    private static final String[] forestTextures = {
            "images/ingredients/raw_beef.png",
            "images/ingredients/cooked_beef.png",
            "images/ingredients/burnt_beef.png"
    };

    public void trigger() {
        create();
        // list of other functions that'll ultimately have the cutscene working
    }
    @Override
    public void create() {

        spawnBeef("cooked");
    }

    private void spawnBeef(String cookedLevel) {
        Entity newBeef = ItemFactory.createBeef(cookedLevel);
        spawnEntityAt(newBeef, new GridPoint2(3, 3), true, true);
        newBeef.setScale(0.5f,0.5f);
    }

}
