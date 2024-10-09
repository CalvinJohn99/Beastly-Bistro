package com.csse3200.game.components.cutscenes;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.components.cutscenes.Cutscene;
import com.csse3200.game.components.cutscenes.scenes.Scene;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

public class MoralDay1Cutscene extends Cutscene {
    public MoralDay1Cutscene() {
        super();
    }

    @Override
    protected void setupScenes() {
        createScene();
    }

    private void createScene() {
        Array<String> sceneText = new Array<>();
        sceneText.add("Mafia Boss > This is dialogue 1. ");
        sceneText.add("Mafia Boss > This is dialogue 2.");
        sceneText.add("Mafia Boss > This is dialogue 3.");

        String mafiaImage = "images/Cutscenes/Character Artwork/rhino_sprite.png";
        Vector2 mafiaPosition = new Vector2(-5, 1);
        float mafiaScale = 4.0f;

        String playerImage = "images/Cutscenes/moral_icons/drug_ico.png";
        Vector2 playerPosition = new Vector2(4, -5);
        float playerScale = 4.0f;

        // Add scenes with background images, animations, text, and duration
        Scene scene = new Scene("images/Cutscenes/resized_black_image.png");
        scene.setImages(
                new String[]{mafiaImage, playerImage},
                new Vector2[] {mafiaPosition, playerPosition},
                new float[] {mafiaScale, playerScale}
        );

        scene.setSceneText(sceneText);
        scene.setDuration(3.0f);

        scenes.add(scene);
    }

    @Override
    protected void loadAssets() {
        // Load the background images for the cutscene
        textures = new String[] {
                "images/Cutscenes/Day2_Scene.png",
        };

        animations = new String[] {};

        images = new String[] {
                "images/Cutscenes/Character Artwork/rhino_sprite.png",
                "images/Cutscenes/moral_icons/drug_ico.png",
        };

        // Get the resource service to load assets
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(textures);
        resourceService.loadTextures(images);
        resourceService.loadTextureAtlases(animations);
        resourceService.loadAll();
    }

    @Override
    public void createEntities() {
        // Any specific entity creation logic for the intro cutscene
    }
}
