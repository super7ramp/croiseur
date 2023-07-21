/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui;

import com.gitlab.super7ramp.croiseur.api.CrosswordService;
import com.gitlab.super7ramp.croiseur.gui.concurrent.AutoCloseableExecutorService;
import com.gitlab.super7ramp.croiseur.gui.concurrent.AutoCloseableExecutors;
import com.gitlab.super7ramp.croiseur.gui.presenter.GuiPresenter;
import com.gitlab.super7ramp.croiseur.gui.view.model.ApplicationViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.ErrorsViewModel;
import com.gitlab.super7ramp.croiseur.spi.presenter.Presenter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * The Croiseur GUI application.
 */
public final class CroiseurGuiApplication extends Application {

    /** The stage's title. */
    private static final String STAGE_TITLE = "Croiseur";

    /** The stage's min width. */
    private static final int MIN_WIDTH = 510;

    /** The stage's min height. */
    private static final int MIN_HEIGHT = 400;

    /** The application icon name. */
    private static final String ICON_NAME = "application-icon.png";

    /** The application stylesheet. */
    private static final String STYLESHEET = "theme-light.css";

    /**
     * The number of background threads. At least 2 so that dictionaries can be browsed while solver
     * is running.
     */
    private static final int NUMBER_OF_BACKGROUND_THREADS = 2;

    /** Resources to be closed upon application shutdown. */
    private final Collection<AutoCloseable> resources;

    /**
     * Constructor.
     */
    public CroiseurGuiApplication() {
        resources = new ArrayList<>();
    }

    @Override
    public void start(final Stage stage) throws IOException {
        final Executor executor = createExecutor();
        final Map<String, Scene> namedScenes = loadComponents(stage, executor);
        configureStage(stage, namedScenes);
        configureStyleSheet();
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        for (final AutoCloseable resource : resources) {
            resource.close();
        }
    }

    /**
     * Configures the stage.
     * <p>
     * All given scenes will be attached as properties of the stage. The first one will be set as
     * stage's scene.
     *
     * @param stage       the stage to configure
     * @param namedScenes the scenes to load. Need to be a sequenced map, as the first scene of the
     *                    collection will be set as stage's scene
     */
    private static void configureStage(final Stage stage, final Map<String, Scene> namedScenes) {
        stage.getProperties().putAll(namedScenes);
        stage.setScene(namedScenes.values().iterator().next());
        stage.setTitle(STAGE_TITLE);
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        final Image icon = loadIcon();
        stage.getIcons().add(icon);
        // Make sure to close the application even if some views haven't been shown
        stage.setOnCloseRequest(event -> Platform.exit());
    }

    /**
     * Loads the application icon.
     *
     * @return the application icon
     * @throws NullPointerException if icon is not found
     */
    private static Image loadIcon() {
        final InputStream iconLocation =
                CroiseurGuiApplication.class.getResourceAsStream(ICON_NAME);
        Objects.requireNonNull(iconLocation, "Application icon not found");
        return new Image(iconLocation);
    }

    /**
     * Loads application stylesheet and sets it as user agent stylesheet.
     *
     * @throws NullPointerException if stylesheet is not found
     */
    private static void configureStyleSheet() {
        final URL themeUrl = CroiseurGuiApplication.class.getResource(STYLESHEET);
        Objects.requireNonNull(themeUrl, "Application stylesheet not found");
        Application.setUserAgentStylesheet(themeUrl.toString());
    }

    /**
     * Loads the application components.
     *
     * @param executor the background task executor
     * @return the application components named scenes
     * @throws IOException if loading from FXML files fails
     */
    private static Map<String, Scene> loadComponents(final Stage stage, final Executor executor)
            throws IOException {
        // Dependencies for construction: view model <- presenter <- use-cases <- controllers/views
        final ApplicationViewModel applicationViewModel = new ApplicationViewModel();
        loadErrorPopup(applicationViewModel, stage);
        final Presenter presenter = new GuiPresenter(applicationViewModel);
        final CrosswordService crosswordService = CrosswordServiceLoader.load(presenter);
        final Parent editorView =
                loadCrosswordEditor(applicationViewModel, crosswordService, executor);
        final Parent welcomeScreenView =
                loadWelcomeScreen(applicationViewModel, crosswordService, executor);

        final Map<String, Scene> namedScenes = new LinkedHashMap<>();
        namedScenes.put("welcomeScene", new Scene(welcomeScreenView));
        namedScenes.put("editorScene", new Scene(editorView));
        return namedScenes;
    }

    /**
     * Loads the special error popup control logic. Popup can happen on all scenes of the given
     * stage.
     *
     * @param applicationViewModel the application view model
     * @param stage                the application stage
     */
    private static void loadErrorPopup(final ApplicationViewModel applicationViewModel,
                                       final Stage stage) {
        final ErrorsViewModel errorsViewModel = applicationViewModel.errorsViewModel();
        errorsViewModel.currentErrorProperty().addListener((observable, oldError, newError) -> {
            if (newError != null) {
                final Alert errorAlert = new Alert(Alert.AlertType.ERROR, newError);
                errorAlert.initOwner(stage.getScene().getWindow());
                errorAlert.showAndWait();
                errorsViewModel.acknowledgeError();
            }
        });
    }

    /**
     * Loads the crossword editor.
     *
     * @param applicationViewModel the application view model
     * @param crosswordService     the croiseur core library
     * @param executor             the executor
     * @return the editor view
     */
    private static Parent loadCrosswordEditor(final ApplicationViewModel applicationViewModel,
                                              final CrosswordService crosswordService,
                                              final Executor executor) throws IOException {
        final var editorController =
                new CrosswordEditorController(crosswordService, applicationViewModel, executor);
        return ViewLoader.load(editorController);
    }

    /**
     * Loads the welcome screen.
     *
     * @param applicationViewModel the application view-models
     * @param crosswordService     the croiseur core library
     * @param executor             the background task executor
     * @return the welcome screen view
     * @throws IOException if loading from FXML file fails
     */
    private static Parent loadWelcomeScreen(final ApplicationViewModel applicationViewModel,
                                            final CrosswordService crosswordService,
                                            final Executor executor) throws IOException {
        final var welcomeScreenController =
                new WelcomeScreenController(applicationViewModel.puzzleSelectionViewModel(),
                                            applicationViewModel.puzzleEditionViewModel(),
                                            applicationViewModel.puzzleCodecsViewModel(),
                                            crosswordService.puzzleService(), executor);
        return ViewLoader.load(welcomeScreenController);
    }

    /**
     * Creates an executor to run tasks on the background. The created executor will be added to
     * this application closeable {@link #resources}.
     *
     * @return the executor
     */
    private Executor createExecutor() {
        final AutoCloseableExecutorService executor =
                AutoCloseableExecutors.newFixedThreadPool(NUMBER_OF_BACKGROUND_THREADS);
        resources.add(executor);
        return executor;
    }
}