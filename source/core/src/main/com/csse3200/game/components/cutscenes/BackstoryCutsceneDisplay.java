package com.csse3200.game.components.cutscenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.components.maingame.TextDisplay;

/**
 * The BackstoryCutsceneDisplay class manages the user interface for displaying the backstory cutscene.
 * The text display is smaller and placed higher on the screen compared to the base CutsceneScreenDisplay.
 */
public class BackstoryCutsceneDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(BackstoryCutsceneDisplay.class);
    private Table table;
    private TextDisplay textDisplay;
    private Skin skin = null;
    private Image backgroundImage;
    private float viewportWidth;
    private float viewportHeight;

    public BackstoryCutsceneDisplay(Skin skin) {
        super(skin);
        this.skin = skin;
    }

    public BackstoryCutsceneDisplay() {
        super();
    }

    /**
     * Initializes the backstory cutscene UI, sets up buttons and listeners, and starts the cutscene text display.
     */
    @Override
    public void create() {
        super.create();

        if (table == null) {
            table = new Table();  // Initialize table if it's not already
        }

        // Initialize the background image
        backgroundImage = new Image(ServiceLocator.getResourceService().getAsset("images/backstory_background.png", Texture.class));
        backgroundImage.setScaling(Scaling.fill);

        // Retrieve viewport dimensions for resizing
        viewportWidth = Gdx.graphics.getWidth();
        viewportHeight = Gdx.graphics.getHeight();

        // Resize the background image to fit the screen
        resizeBackgroundImage();

        setupUI();
    }

    /**
     * Sets up the cutscene UI components with a smaller text display that appears higher on the screen.
     */
    private void setupUI() {
        // Initialize the text display and add it to the stage, hidden initially
        setupTextDisplay();

        if (skin == null) {
            skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        }

        // Positioning the table for the cutscene
        table.top();  // Move it to the top-center of the screen
        table.setFillParent(true);

        // Create "Skip" button with its functionality
        TextButton skipBtn = new TextButton("Skip Backstory", skin);
        skipBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.info("Backstory skipped.");
                entity.getEvents().trigger("cutsceneEnded");  // Trigger skip
            }
        });
        table.add(skipBtn).padBottom(10f).padRight(30f);

        // Create "Next Scene" button with its functionality
        TextButton nextSceneBtn = new TextButton("Next Scene", skin);
        nextSceneBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                logger.info("Next Scene button clicked.");
                entity.getEvents().trigger("nextCutscene");  // Trigger next cutscene
            }
        });
        table.add(nextSceneBtn).padBottom(10f).padRight(10f);

        // Add the table and background image to the stage
        stage.addActor(backgroundImage);
        stage.addActor(table);
    }

    /**
     * Resizes the background image based on the current viewport dimensions.
     */
    private void resizeBackgroundImage() {
        float scaleFactor = getScalingFactor(viewportWidth, viewportHeight);
        backgroundImage.setSize(viewportWidth * scaleFactor, viewportHeight * scaleFactor);
        backgroundImage.setPosition((viewportWidth - backgroundImage.getWidth()) / 2, (viewportHeight - backgroundImage.getHeight()) / 2);
    }

    /**
     * Calculates the scaling factor for resizing the background image to fit the viewport.
     */
    private float getScalingFactor(float viewportWidth, float viewportHeight) {
        return Math.min(viewportWidth / 1280f, viewportHeight / 720f);
    }

//    public void setupTextDisplay() {
//        textDisplay = new TextDisplay();  // Create an instance of TextDisplay
//
//        // Call the create method to initialize the TextDisplay
//        textDisplay.create();
//
//        // Customize the text display size and position
//        textDisplay.getTable().setScale(0.8f);  // Reduce the scale of the text display
//        textDisplay.getTable().top().center();  // Position the text display at the top center of the screen
//        textDisplay.getTable().padTop(100f);    // Adjust the padding to move it down from the top (if necessary)
//
//        // Add the text display to the stage
//        stage.addActor(textDisplay.getTable());
//
//        // Optionally, set initial text for the display
//        textDisplay.setText("Backstory text goes here...");
//    }

    public void setupTextDisplay() {
        textDisplay = new TextDisplay();  // Create an instance of TextDisplay
        textDisplay.create();  // Initialize the TextDisplay with its default settings

        // Adjust the position of the text box to be higher on the screen
        textDisplay.setPosition(Align.top, 250f, 0f, 0f);  // Align to the top of the screen with padding from the top

        // Adjust the scale to make the text box smaller
        textDisplay.setTableScale(0.5f, 0.5f);  // Reduce the size (adjust values as necessary)

        // Optionally, adjust the font size to make the text smaller
        textDisplay.setFontScale(1.5f);  // Set the font scale (smaller than the default 3.0f)

        // Add the text display table to the stage after modifying it
        stage.addActor(textDisplay.getTable());

        // Optionally, set any initial text for the cutscene
        textDisplay.setText("Backstory text goes here...");
    }

    /**
     * Disposes of the cutscene screen display, clearing any UI elements.
     */
    @Override
    public void dispose() {
        super.dispose();

        if (table != null) {
            table.clear();  // Clear the table safely
        }
        if (textDisplay != null && textDisplay.getTable() != null) {
            textDisplay.getTable().clear();  // Clear the text display table
        }
        if (backgroundImage != null) {
            backgroundImage.clear();
        }
    }

    /**
     * Draw method for rendering the cutscene screen. Handled by the stage.
     * @param batch The sprite batch used for rendering.
     */
    public void draw(SpriteBatch batch) {
        // Drawing is handled by the stage, so no implementation needed here
    }

    /**
     * Gets the stage component for the cutscene display.
     * @return The Stage component for the cutscene.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Sets the stage for the cutscene display.
     * @param stage The stage to be set.
     */
    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Sets the table for the cutscene UI.
     * @param table The table to be set.
     */
    public void setTable(Table table) {
        this.table = table;
    }

    /**
     * Gets the table for the cutscene UI.
     * @return The Table component
     */
    public Table getTable() {
        return table;
    }

    /**
     * Gets the cutscene text display component for the cutscene.
     * @return A CutsceneTextDisplay component for the cutscene.
     */
    public TextDisplay getTextDisplay() {
        return textDisplay;
    }

    /**
     * Sets the Cutscene Text Display Component for the cutscene.
     * @param textDisplay the CutsceneTextDisplay to be set.
     */
    public void setTextDisplay(TextDisplay textDisplay) {
        this.textDisplay = textDisplay;
    }
}