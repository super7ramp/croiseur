/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.EnumMap;
import java.util.Map;

/**
 * Utility class to switch between scenes.
 */
final class SceneSwitcher {

    /** Scene identifier. */
    enum SceneId {
        WELCOME_SCREEN,
        CROSSWORD_EDITOR
    }

    /** The stage. */
    private final Stage stage;

    /** The registered scenes. */
    private final Map<SceneId, Scene> scenes;

    /**
     * Constructs an instance.
     *
     * @param stageArg the stage
     */
    SceneSwitcher(final Stage stageArg) {
        stage = stageArg;
        stage.setScene(null);
        scenes = new EnumMap<>(SceneId.class);
    }

    /**
     * Registers a scene.
     * <p>
     * If stage has no scene, then the given scene is set as stage scene. In other words, stage's
     * scene is initialized with first registered scene.
     *
     * @param sceneId the scene id
     * @param scene   the scene
     */
    void registerScene(final SceneId sceneId, final Scene scene) {
        scenes.put(sceneId, scene);
        if (stage.getScene() == null) {
            stage.setScene(scene);
        }
    }

    /**
     * Switches to welcome screen scene.
     */
    void switchToWelcomeScreen() {
        switchTo(SceneId.WELCOME_SCREEN);
    }

    /**
     * Switches to crossword editor scene.
     */
    void switchToEditorView() {
        switchTo(SceneId.CROSSWORD_EDITOR);
    }

    /**
     * Switches to the scene identified by the given scene id.
     *
     * @param sceneId the identifier of the scene to switch to
     */
    void switchTo(final SceneId sceneId) {
        final Scene scene = scenes.get(sceneId);
        stage.setScene(scene);
    }
}
