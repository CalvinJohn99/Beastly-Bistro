package com.csse3200.game.components.tutorial;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.csse3200.game.GdxGame;
import com.csse3200.game.screens.TutorialScreen;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.csse3200.game.areas.ForestGameArea.*;

/**
 * Displays a confirmation dialog asking if the user wants to start the tutorial.
 */
public class Confirmationpopup extends Dialog {
    private static final Logger logger = LoggerFactory.getLogger(Confirmationpopup.class);
    private final GdxGame game;

    public Confirmationpopup(String title, Skin skin, Stage stage, GdxGame game) {
        super(title, skin);
        this.game = game;

        text("Start tuto?");
        button("Yes", true);
        button("No", false);
        setModal(true);

        show(stage);
    }

    @Override
    protected void result(Object object) {
        boolean isTrue = (boolean) object;
        if (isTrue) {
//            logger.info("Yes clicked");
            game.setScreen(new TutorialScreen(game));
        }else{
//            logger.info("No clicked");
            game.setScreen(GdxGame.ScreenType.MAIN_GAME);
        }
    }

}