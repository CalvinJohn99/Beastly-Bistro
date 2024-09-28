package com.csse3200.game.services;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.files.GameState;
import com.csse3200.game.files.FileLoader.Location;
import com.csse3200.game.files.FileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;

public class SaveLoadService {
    private static final Logger logger = LoggerFactory.getLogger(SaveLoadService.class);
    private static final String ROOT_DIR = "saves";
    private static final String SAVE_FILE = "saveFile.json";

    /**
     * Saves the current state of the game into a GameState
     */
    public void save(String path) {
        // Make a new GameState
        GameState state = new GameState();

        state.setMoney(100);
        state.setDay(2);

        FileLoader.writeClass(state, path, Location.LOCAL);

        logger.debug("The current game state has been saved to the file assets/saves/saveFile.json");
    }

    public void save() {
        save(ROOT_DIR + File.separator + SAVE_FILE);
    }
}